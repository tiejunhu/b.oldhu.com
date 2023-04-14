+++
draft = false
toc = false
date = "2023-04-14T10:53:46+08:00"
title = "Why Janet"
+++

https://ianthehenry.com/posts/why-janet/ 的简单翻译

Janet是一个Lisp方言，作者非常喜欢，写了一本关于Janet的书 https://janet.guide

### Janet很简单

这一点是所有Lisp语言的共同点，Janet只有8个内置关键字：`do`, `def`, `var`, `set`, `if`, `while`, `break`, `fn`(还有5个用于支持macro的关键字：
`quote`, `unquote`, `quasiquote`, `splice`, `upscope`)，其余的都由函数和macro实现。

### Janet可分发

Janet可编译为单个可执行程序，编译的方式是将Janet代码编译为字节码，然后将Janet运行环境，垃圾回收和字节码解释器打包到一起。

### Janet处理文本很方便

Janet内置了一个Parsing Expression Grammars的模块，可以写出解析单行、多行甚至二进制数据的解析器。语法非常强大。

https://janet.guide/pegular-expressions/

https://janet-lang.org/docs/peg.html

### Janet非常容易管理子进程

靠一个叫sh的第三方模块，https://github.com/andrewchambers/janet-sh

Janet可以象写普通的shell脚本一样调用外部工具

### Janet可被嵌入

Janet的引擎是一个.c文件，可被其它程序轻松引入，甚至是Web Assembly 

https://github.com/ianthehenry/toodle.studio

### Janet有不可变集合和可变集合

同时支持可变与不可变数据

### Macro

Macro是所有Lisp的核心优势

### Janet允许将编译时的值传到运行时

任意Janet的值，可以被序列化到磁盘然后再读回来

### Janet的语法很舒服

匿名函数，值的spread，不定长参数，用first而不是car，用do而不是progn等等。
