+++
draft = true
toc = false
date = "2022-12-20T11:20:30+08:00"
title = "fp-ts介绍"
+++

## 基本工具

`flow`与`pipe`

先准备几个基本方法

```typescript
function return1(): number {
  return 1
}

function add1(x: number): number {
  return x + 1;
}

function multiply2(x: number): number {
  return x * 2;
}

function numberToString(x: number): String {
  return `ret: ${x}`
}
```

`flow`和`pipe`可以用于对方法进行组合，相当于函数的compose

```typescript
import { flow, pipe } from "fp-ts/function";

const func1 = flow(add1, multiply2);

pipe(1, add1, multiply2, console.log);

pipe(1, add1, multiply2, numberToString, console.log);

// 以下会报错类型错误
// console.log(pipe(1, add1, numberToString, multiply2));

console.log(func1(1))
pipe(1, func1, numberToString, console.log)
flow(return1, func1, console.log)()
```

## Semigroup

定义`Semigroup`之前，先定义`Magma`

### Magma

定义了concat函数的一个类型，就是`Magma`，对`concat`这个函数，没有任何其它要求

```typescript
export interface Magma<A> {
  readonly concat: (x: A, y: A) => A
}
```

比如：

```typescript
const MagmaNumberSub: Magma<number> = {
  concat: (x: number, y: number): number => x - y
}
```

有了`concat`，就可以定义一个`concatAll`(相当于`reduce`, `fold`)函数

```typescript
export declare const concatAll: <A>(M: Magma<A>) => (startWith: A) => (as: readonly A[]) => A
```

### 将Magma限制为Semigroup

如果concat满足结合率，这样的Magma可称为Semigroup。满足了结合率的情况下，多个数据需要concat，就可以并行。比如：

```
a * b * c * d * e * f * g * h = ((a * b) * (c * d)) * ((e * f) * (g * h))
```

Semigroup的定义也是一个interface。注意这里无法在**类型系统**中，将结合率这个限制表达出来。

```ts
interface Semigroup<A> extends Magma<A> {}
```

在Semigroup下的concatAll函数，更有价值一些。

可以理解为一个Semigroup的实例，实际上是定义了对于一个数据类型进行连接、合并(输入两个值，输出一个值)的一个方式，然后就可以使用concatAll对多个值进行运算。

### 反向(对等)Semigroup

可以定义一个函数，自动基于一个Semigroup的实例，生成另一个反向的Semigroup:

```ts
import { Semigroup } from 'fp-ts/Semigroup'

// This is a Semigroup combinator
const reverse = <A>(S: Semigroup<A>): Semigroup<A> => ({
  concat: (first, second) => S.concat(second, first)
})
```

### 自动生成乘积(结构)类型的Semigroup

```ts
import * as S from 'fp-ts/Semigroup'
import * as N from 'fp-ts/number'

type Point = {
  x: number;
  y: number;
}

const semigroupPoint: S.Semigroup<Point> = S.struct({
  x: N.SemigroupSum,
  y: N.SemigroupSum,
})
```

### 完全序(total order)数据类型的Semigroup

total order是指在一个类型中，任何两个数据都可比较大小，可排序。在这个情况下，可以有最小和最大两种Semigroup实例：

```ts
import { Semigroup } from 'fp-ts/Semigroup'

const SemigroupMin: Semigroup<number> = {
  concat: (first, second) => Math.min(first, second)
}

const SemigroupMax: Semigroup<number> = {
  concat: (first, second) => Math.max(first, second)
}
```

## Monoid

Semigroup再多定义一个empty，就是Monoid

```ts
import { Semigroup } from 'fp-ts/Semigroup'

interface Monoid<A> extends Semigroup<A> {
  readonly empty: A
}
```

empty要满足两个要求：
 * Right identity: `concat(a, empty) = a`
 * Left identity: `concat(empty, a) = a`
 
 这时候如果再使用`concatAll`，可以不用提供初始值。
 
 ```ts
 export declare const concatAll: <A>(M: Monoid<A>) => (as: readonly A[]) => A
```

## 函数式错误处理

返回错误或抛出异常的函数，不是一个完全函数(total function)，是一个部分函数(partial function)。但可以通过返回一个Option类型，将这样的函数，转换为total function

Option类型可简单地定义为：

```ts
// represents a failure
interface None {
  readonly _tag: 'None'
}

// represents a success
interface Some<A> {
  readonly _tag: 'Some'
  readonly value: A
}

type Option<A> = None | Some<A>
```

可以定义一个match方法，用于处理none和some的两种情况：

```ts
const match = <R, A>(onNone: () => R, onSome: (a: A) => R) => (
  fa: Option<A>
): R => {
  switch (fa._tag) {
    case 'None':
      return onNone()
    case 'Some':
      return onSome(fa.value)
  }
}
```

### 比较两个Option类型

如果要比较两个Option类型是否相等，则要分别比较 none none; none some; some none; some some 这样四种情况

```ts
declare const o1: Option<string>
declare const o2: Option<string>

const result: boolean = pipe(
  o1,
  match(
    () => // onNone o1
      pipe(
        o2,
        match(
          () => true, // onNone o2
          () => false // onSome o2
        )
      ),
    (s1) => // onSome o1
      pipe(
        o2,
        match(
          () => false, // onNone o2
          (s2) => s1 === s2 // onSome o2
        )
      )
  )
)
```

fp-ts中提供了相应的函数，可生成这样的方法：

```ts
import * as E from 'fp-ts/Eq'
import * as N from 'fp-ts/number'
import * as O from 'fp-ts/Option'
import * as S from 'fp-ts/string'

type MyTuple = readonly [string, number]

const EqMyTuple = E.tuple<MyTuple>(S.Eq, N.Eq)

const EqOptionMyTuple = O.getEq(EqMyTuple)

const o1: O.Option<MyTuple> = O.some(['a', 1])
const o2: O.Option<MyTuple> = O.some(['a', 2])
const o3: O.Option<MyTuple> = O.some(['b', 1])

console.log(EqOptionMyTuple.equals(o1, o1)) // => true
console.log(EqOptionMyTuple.equals(o1, o2)) // => false
console.log(EqOptionMyTuple.equals(o1, o3)) // => false
```

### Semigroup和Monoid的Option类型
