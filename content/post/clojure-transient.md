+++
draft = false
toc = false
date = "2022-11-18T16:12:11+08:00"
title = "Clojure Transient Data Structure"
+++

参考 https://sq.sf.163.com/blog/article/220973851641442304

Clojure中的数据结构(list, vector, map)等，有persistent和transient两种存在形态。

默认数据是[persistent](https://en.wikipedia.org/wiki/Persistent_data_structure)状态，这是最“安全”的形态，数据是immutable的，任何修改都不会在原数据上改动，而是保留原来的版本，产生一个新的版本。

对于这样的代码：

```clojure
(reduce conj [] (range 10))
```

reduce的每一次迭代，都会产生一个新的vector，前一个vector又没有用了，因此会带来一些不必要的浪费。clojure针对这种情况，提供了transient类型的数据结构。使用transient类型，可以将以上代码改为：

```clojure
(persistent!
  (reduce conj! (transient []) (range 10)))
```

  * 这里的transient函数，可以将一个persistent数据结构改成transient类型，时间复杂度可认为是O(1)
  * 修改transient数据结构的函数，要加一个感叹号，从conj改成conj!
  * 最终的结果，用persistent!转换回persistent类型，时间复杂度也是O(1)


transient数据结构，相对于persistent数据结构，性能有较大的提升。

但transient不是mutable。如果一个变量名，指向一个transient数据结构，不等于这个变量是可变的：

以下看起来t成为了一个mutable变量

```clojure
 (let [t (transient {})]
      (dotimes [i 8]
        (assoc! t i i))
      (persistent! t))
    ;;=> {0 0, 1 1, 2 2, 3 3, 4 4, 5 5, 6 6, 7 7}
```

但当重复更多次数时，不一定t还能指向最后一个transient数据结构的头部。这是因为clojure底层的存储树结构可能发生了变化，root改变了。

```clojure
(let [t (transient {})]
      (dotimes [i 9]
        (assoc! t i i))
      (persistent! t))
    ;;=> {0 0, 1 1, 2 2, 3 3, 4 4, 5 5, 6 6, 7 7}
	;; 8 8 没了
```
