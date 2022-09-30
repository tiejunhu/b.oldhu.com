+++
draft = false
toc = false
date = "2022-09-30T12:02:28+08:00"
title = "Clojure Protocol"
+++

### Intro

Clojure中的 `defprotocol` 主要是解决 **表达问题(Expression Problem)**

所谓表达问题，就是当应用程序要被扩展的时候，如何确保扩展的新的方法和数据类型，与原有的方法和数据类型，可以一起工作。

所以Clojure通过 `defprotocol, deftype, defrecord` 引入了一个类型系统。这个类型系统，在低层是基于Java(JVM)的类型系统实现的。`defprotocol` 大致相当于Java中的interface

### 基本用法

#### defprotocol

```clojure
(defprotocol P
  (foo [x])
  (bar-me [x] [x y]))
```

`defprotocol`是个宏，它实际上定义了什么呢？可以通过REPL看一下：

```
user> proto/P
{:on proto.P,
 :on-interface proto.P,
 :sigs
 {:foo {:tag nil, :name foo, :arglists ([x]), :doc nil}, :bar-me {:tag nil, :name bar-me, :arglists ([x] [x y]), :doc nil}},
 :var #'proto/P,
 :method-map {:bar-me :bar-me, :foo :foo},
 :method-builders {#'proto/bar-me #function[proto/eval4830/fn--4831], #'proto/foo #function[proto/eval4830/fn--4848]}}
 
user> (type proto/P)
clojure.lang.PersistentArrayMap

user> (type (:on-interface proto/P))
java.lang.Class
```

所以`defprotocol`实际上生成了一个`map`，同时生成了一个Java的interface

#### defrecord

```clojure
;; P的实现
(defrecord R [a]
  P
  (foo [this] 17)
  (bar-me [this] a)
  (bar-me [this y] (+ a 1)))

;; R的实例
(def r (R. 42))

;; 调用protocol中的方法
(foo r) ;; 17
(bar-me r) ;; 42
(bar-me r 2) ;; 43
```

看一下`defrecord`做了什么

```
user> (type proto.R)
java.lang.Class

user> (supers proto.R)
#{clojure.lang.IHashEq
  clojure.lang.IPersistentMap
  clojure.lang.IRecord
  clojure.lang.Counted
  clojure.lang.IPersistentCollection
  java.io.Serializable
  clojure.lang.Associative
  java.lang.Iterable
  clojure.lang.IMeta
  java.util.Map
  clojure.lang.Seqable
  clojure.lang.ILookup
  clojure.lang.IObj
  java.lang.Object
  clojure.lang.IKeywordLookup
  proto.P}
```

可以看到`proto.R`是一个Java Class，同时实现了`proto.P`

#### extend-protocol

可以对一个已经存在的类，通过`extend-protocol`加protocol。比如以下可以给String加一个size方法

```clojure
(defprotocol ISize
  (size [this]))

(extend-protocol ISize
    java.lang.String
  (size [this] (.length this)))

(size "hello") ;; 5
```

这个扩展的原理，是在Protocol的`:impls`中记录了相应的信息。
```
proto> ISize
{:on proto.ISize,
 :on-interface proto.ISize,
 :sigs {:size {:tag nil, :name size, :arglists ([this]), :doc nil}},
 :var #'proto/ISize,
 :method-map {:size :size},
 :method-builders {#'proto/size #function[proto/eval6872/fn--6873]},
 :impls {java.lang.String {:size #function[proto/eval6889/fn--6890]}}}
```

#### reify

临时实现一个接口

```clojure
(foo
 (let [x 42]
   (reify P
     (foo [this] 17)
     (bar-me [this] x)
     (bar-me [this y] (+ x 1))))) ;; 17
```

#### deftype vs. defrecord

Clojure中还提供了一个`deftype`用于实现接口。与`defrecord`的不同[大致有](https://clojure.org/reference/datatypes#_deftype_and_defrecord)：

  * `deftype`允许通过metadata定义字段是可变的
  * `defrecord`会自动生成`hashCode`等方法，`deftype`不会
  * `defrecord`会自动实现完整的map方法，可以当成一个map来用
  * `defrecord`会自动生成`map->RecordName`这样的方法，用于将一个map生成record

### 参考链接

  * https://blog.xiebiao.com/post/2019-11-19-clojure-datatypes/
  * https://flexiana.com/2021/08/on-the-nature-of-clojure-protocols 
  * https://clojure.org/reference/protocols 
  * https://stackoverflow.com/questions/4509782/simple-explanation-of-clojure-protocols
  * https://github.com/cemerick/clojure-type-selection-flowchart/
  * https://stackoverflow.com/questions/13150568/deftype-vs-defrecord
