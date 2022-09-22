+++
draft = false
toc = false
date = "2022-09-21T14:52:21+08:00"
title = "Clojure Macro介绍"
+++

## Clojure语言

Clojure是一门Lisp方言，Lisp这个名称是"LISt Processor"的缩写。这里有一个深层次的含义：Lisp可以处理List数据，同时Lisp代码本身也是List数据。

与其它编程语言不同，Clojure(Lisp)的代码并不是由字符拼成的语法(关键字，空格，分号，大括号等，比如 `private final String func1() {} `)组成。Clojure的代码本身就是一个list数据结构。

比如`(+ 1 2)`这段代码，本身是一个list，三个元素分别是 +, 1, 2。

这样的设计，导致Clojure是一个非常简单的语言，整个语言语法，用一页纸就可以写完。除了少量的[特殊形式](https://clojure.org/reference/special_forms)，其它的所有代码都用统一的规则处理。

Clojure代码的执行过程，主要由编译和执行两个部分组成。在编译期，由Reader负责将代码处理解析为list数据结构，然后将其转化为JVM字节码，然后执行。

Clojure中的Reader相当于其它语言中的parser，Reader处理之后的结果，是一个数据结构(代码即数据)。数据结构会被执行：
  * 字面量(整数，vector，字符串)执行的结果还是自己
  * List以函数调用的方式执行
  * 符号按变量处理
  
可以被调用的List，叫表达式(expression)，也叫形式(form)。有三种form：函数，宏，特殊形式(form)

函数是对值进行处理，将值变换为另一个值。Macro是对代码进行处理，将代码变换为另一段代码。

Clojure本身也很擅长处理list数据结构，那有没有可能用Clojure代码生成用来表达调用的list数据结构？用代码生成代码(用代码生成数据，用数据生成数据)？这就是Clojure中的宏(Macro)。

## 与Macro有关的数据类型

#### quote

`quote`是个特殊的函数，将这个函数作用于一个list上，Clojure将不执行这个list结构。

比如 `(+ 1 1)`的执行结果是 2，而 `(quote (+ 1 1))` 的执行结果是 `(+ 1 1)`。`(eval (quote (+ 1 1)))`的结果是2

`quote`可简写为 `'`。 `(= '(+ 1 1) (quote (+ 1 1)))`的执行结果是true

#### syntax quote

有一个特殊的quote，叫syntax quote，符号是 `。这个特殊的quote，与普通quote的最大区别。是它会将表达式中的函数，加入namespace前缀。

```clojure
`(+ 1 1) ;; 等价于 '(clojure.core/+ 1 1)
```

#### unquote

在syntax quote的表达式(expr)中，可以用 `~` 来表示某个表达式要计算(类似于javascript[模板字符串](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Template_literals)里的`${expr}`)。

```clojure
`(+ 1 ~(+ 1 1)) ;; 等价于 '(clojure.core/+ 1 2)
(def quoted-fn `(+ 1 1))
(eval `(+ 2 ~quoted-fn)) ;; 4
```

syntax quote与unquote可重复执行在一个值上，执行的时候从左到右顺序执行比如:

```clojure
(let [y '(- x)]
  (println ``y)   ;; (quote user/y)
  (println `~y)   ;; (- x)
  (println ``~y)) ;; user/y，先对y quote两次，然后unquote一次
```

#### unquote slice

可以在syntax quote中，用 `~@` 去掉一层 list ()，比如：

```clojure
`(+ 1 ~@'(1 1)) ;; 等价于`(clojure.core/+ 1 1 1)
```

## 定义macro

定义macro，就是用`defmacro`定义一个可执行体。在编译期执行并返回一个list，然后在执行期转换并运行。macro与function的一个非常重要的区别是函数在调用时对参数进行求值，Macro不对参数进行求值。

比如下面的例子：

```clojure
(defn func-1 [v]
  (println v)
  (println v))

(defmacro macro-1 [v]
  `(do (println ~v)
       (println ~v)))

;; 执行结果是: step1, step2, step2
(func-1 (do (println "step1")
            "step2"))

;; 执行结果是: step1, step2, step1, step2
(macro-1 (do (println "step1")
             "step2"))
```

定义Macro的过程，就是定义一个生成代码结构的过程。一个Macro的返回值，一定是一个list，而且这个list是代码，可以执行。


## 定义几个简单有用的Macro

#### `infix`

```clojure
(defmacro infix
  [[param1 op param2]]
  `(~op ~param1 ~param2))

(infix (1 + 1)) ;; 相当于(+ 1 1)
```


#### `unless`

```clojure
(defmacro unless [condition & body]
  `(if (not ~condition)
     (do ~@body)))
```

#### `dbg`

调试输出表达式

```clojure
(defmacro dbg[x] 
  `(let [x# ~x] 
     (println "dbg:" '~x "=" x#) 
	 x#))
```

#### `doseq-indexed`

自动给seq加一个索引变量

```clojure
(defmacro doseq-indexed [index-sym [item-sym coll] & body]
  `(doseq [[~item-sym ~index-sym]
           (map vector ~coll (range))]
     ~@body))

(with-out-str
  (doseq-indexed i [x [10 100 1000]]
                         (println "i: " i "x: " x)))
```
