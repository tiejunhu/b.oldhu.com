+++
draft = false
toc = false
date = "2022-09-22T19:35:59+08:00"
title = "世界上最美的程序"
+++

题目比较标题党，其实只是在用Lisp在写一个Lisp解释器

视频链接: [link](https://www.youtube.com/watch?v=OyfBQmvr2Hc)

用Lisp写Lisp解释器，直接使用内置的list match函数，解决了parser部分，然后主要就是解决一个lambda定义，其余都比较简单。

lambda定义中，主要解决的是传入的参数怎么解析。

以下是Clojure版本。

```clojure
(ns repl
  (:require [clojure.core.match :refer (match)]))

(defn eval-expr [expr env]
  (match expr
    (n :guard number?) n
    (x :guard symbol?) (env x)
    (['add1 e] :seq) (inc (eval-expr e env))
    (['sub1 e] :seq) (dec (eval-expr e env))
    (['zero? e] :seq) (zero? (eval-expr e env))
    (['* e1 e2] :seq) (* (eval-expr e1 env) (eval-expr e2 env))
    (['if t c a] :seq) (if (eval-expr t env) (eval-expr c env) (eval-expr a env))
    (['lambda ([x] :seq) body] :seq) (fn [arg]
                                       (eval-expr body (fn [y]
                                                         (if (= x y) arg (env y)))))
    ([orator orand] :seq) ((eval-expr orator env) (eval-expr orand env))))
```
