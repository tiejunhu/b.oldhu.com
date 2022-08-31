+++
draft = false
toc = false
date = "2022-08-31T16:49:03+08:00"
title = "ClojureScript中的goog.DEBUG"
+++

### goog.DEBUG 谁来设置？

shadow-cljs会自动设置，参考[这里](https://shadow-cljs.github.io/docs/UsersGuide.html?#:~:text=goog.DEBUG)

### ^boolean 是什么

re-frame的template中会自动生成一个变量，其中使用了 `^boolean` 这个type hint

```clojure
(def debug?
  ^boolean goog.DEBUG)
```

这个^boolean的type hint比较[特殊](https://clojurescript.org/about/differences#_hinting)，可以防止 if 被编译为 checked evaluation (因为0和""在ClojureScript中是true，在JavaScript中是false，所以正常的编译需要编译为 checked evaluation)

具体来说

```clojure
(when js/goog.DEBUG
  (js/console.log "hello world"))
```

会被编译为

```javascript
if(cljs.core.truth_(goog.DEBUG)){
  console.log("hello world");
} else {
}
```

这样后面的优化器在goog.DEBUG为false时，无法消除这段代码。

而

```clojure
(when ^boolean js/goog.DEBUG
  (js/console.log "hello world"))
```

会被编译为

```javascript
if(goog.DEBUG){
  console.log("hello world");
} else {
}
```

这样babel等js的优化器，就能做优化了
