+++
categories = ["programming"]
date = "2022-07-24T21:46:12+08:00"
draft = false
title = "范畴论基础(3) - Functor"
toc = false
+++

### JavaScript中的map方法(本质上)是什么？

```typescript
const array1 = [1, 2, 3]
const array2 = array1.map(i => i + 1)
const array3 = array1.map(i => String.fromCharCode(96 + i))
```
array2是`[2, 3, 4]`，array3是`['a', 'b', 'c']`

array是一个容器，`i => i + 1`这个方法无法直接作用于array上，通过map方法，将方法作用于array容器中的元素上，并将结果重新放回容器中

这里的map是一个面向对象的写法，如果改写成函数式的，那map可以写成：

```javascript
const map = (func, array) => array.map((v) => func(v));
```
如果func的输入类型是A，输出类型是B，容器类型是T，那map的函数签名就是：

`map :: (A -> B) -> T[A] -> T[B]`

从函数签名的角度去理解map，可以理解为：map接受一个`A->B`的函数，返回一个`T[A] -> T[B]`的函数。

这样的map，其实是一个Functor

### Functor的定义
![image](/images/functor.png)

Functor本质上是将一个Category映射到另一个Category，并保持Category的结构不变。如果源Category和目标Category是同一个，那这个Functor叫Endofunctor

### 基于Functor重新理解map
先用JavaScript重新定义一下curry和compose

```javascript
function curry(func1, arg1) {
  return (arg2) => func1(arg1, arg2);
}

function compose(func1, func2) {
  return (arg) => func2(func1(arg))
}
```
这时候，基于之前的map, curry, compose定义：

```javascript
const arr = [1, 2, 3]

// 单个参数的函数
const add1 = (x) => x + 1;

// map(fun)的返回值是一个函数。这时候，arrayAdd1可以接受一个array参数，返回一个array结果
const arrayAdd1 = curry(map, add1)

// ret1 = [2, 3, 4]
const ret1 = arrayAdd1(arr);

map(console.log, ret1); // 2 3 4

// 基于Functor的定义，可知道：
// map(f . g) = map(f) . map(g)

// 第2个函数
const multiple2 = (x) => x * 2;

// compose的结果是一个新的函数：multiple2 after add1
const add1multiple2 = compose(add1, multiple2);

// 这里相当于执行 map(f . g)
const ret2 = map(add1multiple2, arr);
map(console.log, ret2); // 4 6 8

// 将multiple2映射到array空间去
const arrayMultiple2 = curry(map, multiple2);

// 这里相当于 map(f) . map(g)
const arrayAdd1Multiple2 = compose(arrayAdd1, arrayMultiple2);

// 这里的执行结果与上面map(f . g)的结果相同
const ret3 = arrayAdd1Multiple2(arr);
map(console.log, ret3); // 4 6 8
```

### map在Functor的定义下是什么

回到最开始的问题，map到底是什么？在Functor的定义下(如果我们将Category看成编程里的类型)，map方法本质上是一个在Category(类型)之间转换函数的函数。

两个类型A与B之间的map，可以将类型A中的函数`f`和`g`，变换为类型B中的函数`f' = map(f)`和`g' = map(g)`。同时如果`f`和`g`可以compose，那`f'`和`g'`也可以compose，并且 $ map(f \circ g) = f' \circ g' $







