+++
draft = false
toc = false
date = "2023-03-27T18:10:49+08:00"
title = "Yew如何工作"
+++

## Yew基本原理

Yew应用由一个html(空白)页面加上一个Web Assembly组成。Web Assembly以Virtual DOM的方式运行，负责管理所有的HTML DOM。

## Yew与HTML的交互

Yew与HTML的交互主要通过以下几个crate: web_sys, js_sys, wasm_bindgen, wasm_bindgen_futures。

* js_sys提供了在Web Assembly中访问JavaScript的能力。主要是支持浏览器的全局对象和方法。
* web_sys提供了在Web Assembly中访问Web API的能力。主要是支持浏览器的DOM对象和方法。
* wasm_bindgen_futures提供了在Web Assembly中编写异步代码的能力。
* wasm_bindgen提供Web Assembly与JavaScript更底层的交互能力。

## html!宏

html!宏支持类似jsx的语法。展开后，会变成一系列的Yew函数调用。这些函数调用会生成一个Virtual DOM树。这个树会被传递给Web Assembly，Web Assembly会根据这个树来更新HTML DOM(调用web_sys)。

## Hooks

Yew支持以下几种Hooks:

* use_effect
* use_context
* use_node_ref
* 状态管理相关
  * use_mut_ref
  * use_state, use_state_eq
  * use_reducer, use_reduecer_eq
  * use_callback
  * use_memo

## 组件

支持functional component和class component。

## SSR

Yew支持SSR。在SSR模式下，Yew应用会编译为native binary。
