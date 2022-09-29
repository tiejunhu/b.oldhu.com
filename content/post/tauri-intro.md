+++
draft = false
toc = false
date = "2022-09-27T12:39:21+08:00"
title = "Tauri介绍"
+++

### 安装与运行

先安装好rust, node, yarn

```bash

# package管理可选yarn
yarn create tauri-app 

# cd app
yarn tauri dev
```

### 架构

#### 代码架构

  * 主配置文件，在 `src-tauri/tauri.conf.json`
  * 整个应用入口，在 `src-tauri/src/main.rs`
  * 前端入口由配置文件中的`tauri -> windows`中的[配置](https://tauri.app/v1/api/config#tauriconfig.windows)决定 ，在 dev 环境下，会默认读取主配置文件中的 `build -> devPath` [配置](https://tauri.app/v1/api/config#buildconfig.devpath)信息

#### 进程架构

每个tauri应用有一个核心进程，核心进程启动一个或多个WebView进程加载html。在核心进程与WebView进程之间用Event和Command机制进行通讯。

WebView在不同的平台上，会使用不同的内核。在Windows上使用Microsoft Edge WebView2，在macOS上使用WKWebView，在Linux上使用webkitgtk。

### 进程间通讯机制

#### 1. Command

Command是JS调用Rust的方式，在`main.rs`中有一个例子：

```rust
#[tauri::command]
fn greet(name: &str) -> String {
    format!("Hello, {}! You've been greeted from Rust!", name)
}
```

`tauri::command`是一个扩展的[attribute](https://doc.rust-lang.org/book/ch19-06-macros.html#attribute-like-macros)。使用这个属性之后，greet方法就可以与JS通信。通信方式是在JS中引入 `@tauri-apps/api/tauri`，然后调用 `tauri.invoke` 方法，以方法名为第一个参数，以json格式的参数为第二个参数。

```clojure
(ns app.core
  (:require ["@tauri-apps/api/tauri" :as tauri]))
  
(.invoke tauri "greet" #js {:name name})
```

#### 2. Event

event是双向的。 以下是从Rust到JS，反向类似。

Rust侧：
```rust
use tauri::Window;

#[derive(Clone, serde::Serialize)]
struct Payload {
  message: String,
}

#[tauri::command]
fn greet(window: Window, name: &str) -> String {
    window.emit("my-event", Payload { message: "Tauri is awesome!".into() }).unwrap();
    format!("Hello, {}! You've been greeted from Rust!", name)
}
```

JS(ClojureScript)侧：

```clojure
(ns app.core
  (:require ["@tauri-apps/api/window" :as window])

(defn listen-window-event []
  (let [win (.-appWindow window)
        my-event-handler (fn [evt]
                           (js/console.log evt))]
    (.listen win "my-event" my-event-handler)))
```

控制台上输出：

```json
{event: "my-event", 
 windowLabel: "main", 
 payload: {message: "Tauri is awesome!"}, 
 id: 11364764986439348000}
```

### JS API

tauri通过JS API提供了大量在rust侧实现的功能，包括：

  * `@tauri-apps/api/clipboard` 用于访问系统剪贴板
  * `@tauri-apps/api/dialog` 用于使用系统本地化的ask，confirm，message，open，save对话框
  * `@tauri-apps/api/fs` 用于读写本地文件
  * `@tauri-apps/api/http` 是一个rust实现的http客户端
  * `@tauri-apps/api/shell` 用于启动外部进程

### 其它功能

  * 支持系统菜单
  * 支持system tray
  * 支持多窗口
  * 内置升级逻辑
  * release版本时，使用了自定义协议 `tauri://` 来访问主页面
  * Windows版本打包时，可将[WebView2安装程序一起打包](https://tauri.app/v1/guides/building/windows#webview2-installation-options)
  
### 需要关注的点（有可能有问题的地方）

主进程与WebView之间没有传输大规模数据的[方案](https://github.com/tauri-apps/tauri/discussions/1336#discussioncomment-456047)
