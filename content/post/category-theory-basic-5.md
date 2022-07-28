+++
tags = ["fp"]
date = "2022-07-27T23:18:12+08:00"
draft = false
title = "范畴论基础(5) - Functor与Monad小结"
toc = false
+++

### Functor与Monad到底在做什么？

Functor与Monad，是Category Theory中的概念。但是在实际编程中，这两个东西到底是什么？

通过之前的内容，可以给出两个在编程中非常不严谨的Functor与Monad定义：

 * 有map方法的类型，就是一个Functor
 * 有flatMap方法的类型，就是一个Monad

> 为了便于理解，到现在还没有引入Functor和Monad的形式化定义。后面有机会再引入。

所以，问题就转换为：

### map和flatMap到底在做什么？

先给出答案：

 * map 可以将一个 `A -> B`的函数，**转换**为 `T[A] -> T[B]` 的函数
 * flatMap 可以将一个 `A -> T[B]` 的函数，**转换**为 `T[A] -> T[B]` 的函数

### 为什么要做对函数进行转换？

简单一句话：为了compose

复杂解释：

  * 在函数式编程的世界里，有时候不得不将一个值，放进一个容器类中，比如之前的`Option<T>`，或者其它常见的`Maybe<T>`，`Result<T>`等。
  * 这样，就会有以下可能的函数签名：
    * A -> B
    * A -> T[B]
    * T[A] -> T[B]
    * ......
  * 有了map和flatMap，就可以将这些函数进行变换成能互相compose的签名(全都转换到 `T[]` 这个容器中)

这里有一个遗漏了的函数签名，是 `T[A -> B]`。(在函数式编程里，函数是一等公民，函数本身也是一个值，所以也可以放进容器里)