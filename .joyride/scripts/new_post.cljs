(ns new-post
  (:require ["fs" :as fs]
            ["path" :as path]
            ["vscode" :as vscode]
            [joyride.core :as joyride]
            [promesa.core :as p]
            [clojure.string :as str]))


(def root-path (-> (first vscode/workspace.workspaceFolders) .-uri .-fsPath))

(def content-template "+++
date = \"{date}\"
draft = false
title = \"{title}\"
toc = false
+++
")

(defn padding-zero [v]
  (if (= v 0) "00"
      (if (< v 10) (str "0" v) (str v))))

(defn abs-value [v]
  (if (< v 0) (- v) v))

(defn get-now-string []
  (let [now (js/Date.)
        year (.getFullYear now)
        month (inc (.getMonth now))
        day (.getDate now)
        hour (.getHours now)
        minute (.getMinutes now)
        second (.getSeconds now)
        offset (.getTimezoneOffset now)
        offset-hour (int (/ (abs-value offset) 60))
        offset-minute (mod (abs-value offset) 60)]
    (str year "-" (padding-zero month) "-" (padding-zero day)
         "T"
         (padding-zero hour) ":" (padding-zero minute) ":" (padding-zero second)
         (if (> offset 0) "-" "+") (padding-zero offset-hour) ":" (padding-zero offset-minute))))

(defn get-content [title]
  (-> content-template
      (str/replace "{date}" (get-now-string))
      (str/replace "{title}" title)))

(defn get-available-file-name [slug]
  (loop [i 0]
    (let [file-name (str slug (if (= i 0) "" (str "-" i)) ".md")
          file-name (path/join root-path "content/post" file-name)]
      (if (fs/existsSync file-name)
        (recur (inc i))
        file-name))))

(defn create-post []
  (p/let [title (vscode/window.showInputBox #js {:title "New Post",
                                                 :prompt "Input title of the post"})
          splits (str/split title #" ")
          slug (str/join "-" (map str/lower-case splits))
          file-name (get-available-file-name slug)]
    (fs/writeFileSync file-name (get-content title))
    (p/let [open-path (.file vscode/Uri file-name)
            doc (.openTextDocument vscode/workspace open-path)
            _ (.showTextDocument vscode/window doc)
            _ (.executeCommand vscode/commands "cursorBottom")]
      (str "create " title " success"))))


(when (= (joyride/invoked-script) joyride/*file*)
  (create-post))