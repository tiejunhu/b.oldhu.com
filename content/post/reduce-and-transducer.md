+++
draft = false
toc = false
date = "2022-08-29T15:17:38+08:00"
title = "Reduce与Transducer"
+++

### reduce/foldl/foldr

reduce是函数式编程中用于处理列表(数组、序列等可迭代数据结构)一个最重要的函数，haskell中是foldl和foldr。其中foldl与reduce含义相同，是从列表的最左面一个元素开始执行，foldr是从最右面一个元素开始执行。

在haskell中，foldl的类型定义为：

```haskell
(b -> a -> b) -> b -> t a -> b
```

这里涉及三个类型：a, b, t。t是一个可迭代的容器，比如是个列表。a是容器内的数据类型，b是foldl整个执行完成后生成的结果数据类型。

函数接受三个参数

 1. 一个函数，接受一个b类型的中间计算结果和一个a类型的元素，返回一个新的b类型计算结果
 2. 一个结果数据类型的初始值
 3. 包含a类型数据的t类型容器
 
 foldl从左到右将每一个a类型的数据和计算到当前的b类型的中间结果，调用函数进行计算，并将函数返回的b类型的结果记录下来，用于下一个a元素的下一次计算。最后一个元素经过函数计算后的结果，作为整体执行的结果返回

Clojure中的reduce函数与foldl功能一至，支持两种形式

```clojure
(reduce f coll)
(reduce f val coll)
```

如果没有设置初始值，则用coll的第一值作为初始值

### 基于reduce实现常见的集合相关函数

#### 1. 计算长度

```clojure
(defn length' [xs]
  (reduce (fn [accu _]
            (+ accu 1))
          0
          xs))
```

#### 2. 计算所有元素的积

```clojure
(defn product' [xs]
  (reduce #(* %1 %2) 1 xs))
```

#### 3. 计算所有元素的和

```clojure
(defn sum' [xs]
  (reduce + xs))
```

#### 4. 基于reduce定义一个map

```clojure
(defn map' [f xs]
  (reduce (fn [accu item]
            (conj accu (f item)))
          [] xs))
```

将中间的lambda函数提取出来，可以写作

```clojure
(defn map-reducer [f]
  (fn [accu item]
    (conj accu (f item))))

(defn map' [f xs]
  (reduce (map-reducer f) [] xs))
```

`map-reducer`函数接受一个`f`为参数，返回一个调用`f`并使用`conj`进行连接的新函数。

#### 5. 基于reduce定义一个filter

```clojure
(defn filter' [pred xs]
  (reduce (fn [accu item]
            (if (pred item)
              (conj accu item)
              accu))
          [] xs))
```

同样可以将lambda提取出来，写成：

```clojure
(defn filter-reducer [pred]
  (fn [accu item]
    (if (pred item)
      (conj accu item)
      accu)))

(defn filter' [pred xs]
  (reduce (filter-reducer pred) [] xs))
```

### transducer的引入

观察上面基于reduce定义的map和filter，其中有两个点是对`vector`这个数据类型有依赖的，一个是用于连接的`conj`函数，另一个是初始化的`[]`。

这两个对数据类型的依赖，会导致这个map函数只能用于`vector`。

把`conj`抽象出来，命名为`reducer`，可以定义

```clojure
(defn map-transducer [f]
  (fn [reducer]
    (fn [accu item]
      (reducer accu (f item)))))

(defn filter-transducer [pred]
  (fn [reducer]
    (fn [accu item]
      (if (pred item)
        (reducer accu item)
        accu))))
```

这样，以下调用定义了一个对每个元素加1的transducer，但如何遍历、如何连接、初始值等，都还未确定。

```clojure
(map-transducer inc)
```

神奇的事情这时候出现了

```clojure
(def inc-transducer (map-transducer inc))
(def larger-than-2-transducer (filter-transducer #(> % 2)))
(def transducer (comp inc-transducer larger-than-2-transducer))

;; 这里最终确定了
;;    如何遍历: reduce
;;    如何连接: conj
;;    初始值: []
(reduce (transducer conj) [] [1 2 3])
```

用map-transducer生成的transducer和用filter-transducer生成的transducer，它们的参数和返回值都是函数，所以可以compose到一起。

最终调用的时候，确定了前面几个未确定的内容：如何遍历、如何连接、初始值

这样，就是一个transducer
