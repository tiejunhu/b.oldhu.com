+++
draft = false
toc = false
date = "2022-09-18T22:55:03+08:00"
title = "Lambda Calculus(λ-calculus)"
+++

来源[链接](https://glebec.github.io/lambda-talk/)
参考：[Lambda Calculus in JavaScript](https://ahrjarrett.com/posts/lambda-calculus-in-javascript)

## 介绍
  * 1930年代，由数学家Church完善
  * 同一时代的 Turing ，提出了图灵机
  * Church 早于 Turing 几个月，用λ演算 证明了David Hilbert的一个著名的 Decision Problem
  * Turing 证明了 图灵机 与 λ演算 是等价的
  * 图灵机与基于变量可变编程模型相关，λ演算与函数式编程有关

## 定义

以下是λ-calculus的完整定义。

```
expression ::= variable
             | expression expression
             | 𝜆 variable . expression
             | ( expression )
```

这里的定义如图灵机一样简单，有非常丰富的表达能力。简单理解一下，以上定义了一个λ表达式的4种可能的形式：

  1. 变量定义
  2. 函数调用。后面的表达式的值，代入第一个表达式
  3. 相当于函数定义，中间一个点分开变量和表达式。后面的表达式中可以引用变量。
  4. 括号来控制计算的优先级
  
以下是一些合法的LC(Lambda Calculus)表达式

| 表达式 | 含义 | 
| ----- | ----- |
| `𝜆a.b`  | 输入a，返回b的一个函数。用js的语法可写为 `a => b` |
| `𝜆a.b x` | 输入a，返回`b x`这个表示式的一个函数。`b x`表示`b(x)`。所以整体是输入a，返回b(x)。`a => b(x)` |
| `(𝜆a.b) x`|先定义一个函数a => b，然后用参数x去调用 `(a => b)(x)`|
| `𝜆a.𝜆b.a `|输入a，返回`𝜆b.a`。`a => b => a`|

对表达式进行逐步简化的过程，称为`β-Reduction`

下面测试一下对LC的理解：对如下表达式 `((𝜆a.a)𝜆b.𝜆c.b)(x)𝜆e.f` 进行 `β-Reduction`

  1. 先看 `((𝜆a.a)𝜆b.𝜆c.b)` 这一部分，将`𝜆b.𝜆c.b`作为a，代入`𝜆a.a`，得到 `(𝜆b.𝜆c.b)`。整个表达式变为 `(𝜆b.𝜆c.b)(x)𝜆e.f`
  2. 将x作为b，代入`𝜆b.𝜆c.b`，变成 `𝜆c.x`。整个表达式变为 `(𝜆c.x)𝜆e.f`
  3. 将`𝜆e.f`作为c，代入`𝜆c.x`，得到x
  
所以，整体表达式化简为x，这时候已经无法再继续化简了，这个叫`β-Normal Form`

## Combinators(算子)

LC里有这样几个著名的算子

|算子名|LC 表示|JS 表示| 含义 |
| ----- | ----- | ----- | ---- |
|Identity|`I := λx.x`|`I = x => x`| 直接返回输入 |
|Mockingbird|`Mockingbird := M := ω := λf.ff`|`M = f => f(f)`| 重复调用两次|
|Kestrel|`K := λab.a`|`K = a => b => a`| 从两个输入里，返回第一个|
|Kite|`KI := λab.b`|`KI = a => b => b`| 从两个输入里，返回第二个|
|Cardinal|`Cardinal := C := flip := λfab.fba`|`C = f => a => b => f(b)(a)`| 将函数的两个参数对调|

`KI` 是个有趣的算子，`KI`实际上等于`K(I)`。将`a => a`作为a，代入`a => b => a`，得到了 `b => (a => a)`。这实际上就是Kite

## 用LC(函数)实现bool

### 定义true/false

用函数来定义true/false，可以将 `if (x) then ... else ...` 看成函数，那true对应的就是 `then => else => then`，false对应的就是 `then => else => else`

```javascript
T = a => b => a
F = a => b => b
```

所以其实true就是Kestrel，false就是Kite

### 定义NOT

由于true/false本身现在是函数，所以NOT可以定义为：

```javascript
NOT = x => x(F)(T)
````

x是一个输入的bool值，当其为T时，会选择后面两个参数里的第一个F，当其为F时，会选择T。这就是NOT的行为

### 定义AND与OR

  * AND 的定义：p AND q, 当 p 为 F 的时候，结果为 F ，当 p 为 T 的时候，结果为 q
  * OR 的定义：p OR q，当 p 为 T 的时候，结果为 T，当 p 为 F 的时候，结果为 q

所以，可以定义为：

```javascript
AND = p => q => p(q)(F)
OR = p => q => p(T)(q)
```

### 定义相等

bool 相等的定义：p 和 q

  * p = T 时，q = T，结果为 T，q = F，结果为 F
  * p = F 时, q = T，结果为 F，q = F，结果为 T

```javascript
BEQ = p => q => p ( q (T) (F) ) ( q (F) (T) )
```
