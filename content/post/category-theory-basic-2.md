+++
categories = ["programming"]
date = "2022-07-24T16:56:12+08:00"
draft = false
title = "范畴论基础(2) - 函数式编程基本概念"
toc = false
+++

### 函数签名
`f :: A -> B` 表示一个函数`f`，输入类型`A`，返回类型`B`

`f :: A -> B -> C` 可以表示两个等价的含义：

* 一个函数`f`，输入类型`A` 和`B`，返回`C`
* 一个函数`f`，输入`A`，返回一个函数`g :: B -> C`

### 函数组合
`f :: A -> B`和`g :: B -> C`，可以组合为 $g \circ f :: A \rightarrow C$，读作`g after f`

函数组合操作$\circ$本身也可以被看作一个函数，这个函数的定义是：

$\circ :: (A \rightarrow B) \rightarrow (B \rightarrow C) \rightarrow (A \rightarrow C)$

$\circ$接受两个函数作为参数，返回一个新的函数

但$\circ$与普通函数的不同之处在于，它是一个中缀函数(infix)

### Pure Function vs. Partial Function
Pure Function是指无副作用的方法，比如

```typescript
function add(a: number, b: number): number {
  return a + b;
}
```
就是一个典型的Pure Function，没有任何副作用。给确定的输入，一定会产生确定的输出。这个方法的类型是：`number -> number -> number`

Partial Function是指函数并不能接受任意的参数，对于一部分参数，可能会无法返回，或抛出异常，比如：

```typescript
function divide(a: number, b: number): number {
  if (b === 0) throw new Error("DivideByZero");
  return a / b;
}
```
这个函数，当 `b === 0`的时候，不能正常返回结果，这样的方法，就叫Partial Function

### Curry
以上面的`add`方法为例，这个方法的类型是 `number -> number -> number`，那么，如果只传入一个参数，是否能返回一个`number -> number`？

这种调用方式，叫Curry

对于TypeScript来说，有两种方式实现Curry

一种方法是将add实现为以下。这种定义方式，与上面的方法类型完全能对应上。

```typescript
const add = (a: number) => (b: number) => {
  return a + b;
}

console.log(add(1)(2));

const add1 = add(1);
console.log(add1(2));
```
另一种方法是实现一个curry方法

```typescript
function add(a: number, b: number): number {
  return a + b;
}

function curry<T>(f: (_x: T, _y: T) => T, a: T) {
  return (b: T) => f(a, b);
}

const add1 = curry(add, 1);

console.log(add1(2))
```
curry接受的第一个参数是一个(双参数的)方法f，第二个参数a是要传给方法f的第一个参数，返回一个新的方法，接受一个新的参数b，并使用a和b去调用f

