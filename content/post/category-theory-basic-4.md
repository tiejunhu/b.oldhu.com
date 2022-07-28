+++
tags = ["fp"]
date = "2022-07-25T22:30:12+08:00"
draft = false
title = "范畴论基础(4) - Monad"
toc = false
+++

### JavaScript中的flatMap方法(本质上)是什么？

```typescript
// [ [ 1, 1 ], [ 2, 2 ], [ 3, 3 ] ]
const val1 = [1, 2, 3].map(i => [i, i]);

// [ 1, 1, 2, 2, 3, 3 ]
const val2 = [1, 2, 3].flatMap(i => [i, i]);
```
这里的 `i => [i, i]`方法，输入一个数字，返回一个容器(array)。

还记得map的签名吗(T是容器的类型)？`map :: (A -> B) -> T[A] -> T[B]` ，map将一个`A -> B`方法，转换为`T[A] -> T[B]`

所以如果传入的是一个`A -> T[B]`，那map转换的结果就是`T[A] -> T[T[B]]`

这就是上面例子中的`val1`，容器套容器

从flatMap这个方法的名字可以看出，其实是先map，然后flat。如果写出其方法签名，应该是：

`flatMap :: (A -> T[B]) -> T[A] -> T[B]`

并可以进一步简化成：

`flatMap :: (A -> R) -> T[A] -> R`


### 自己定义一个Option类型
没有flatMap的时候，Option类型定义如下：

```typescript
class SomeClass<T> {
  constructor(private readonly _value: T) {
    //
  }

  public isSome(): this is SomeClass<T> {
    return true;
  }

  public isNone(): this is NoneClass {
    return false;
  }

  get value(): T {
    return this._value;
  }
}

class NoneClass {
  constructor() {
  }

  public isSome(): this is SomeClass<NoneClass> {
    return false;
  }

  public isNone(): this is NoneClass {
    return true;
  }
}

type Option<T> = SomeClass<T> | NoneClass

// 辅助方法，让写起来更简单
function Some<T>(v: T): SomeClass<T> {
  return new SomeClass(v)
}

// None只需要一个常量，不需要反复创建
const None = new NoneClass()
```
### 使用Option类型

假设要数一个英文句子中所有的字母数量，我们写两个方法：

```typescript
// 将一个字符串按空格切分为数组
// 如果v为空或长度为0，返回None
function splitString(v: String): Option<String[]> {
  if (v == null || v.length === 0) return None;
  return Some(v.split(" "));
}

// 对数组中的每一个字符串取长度并求和
// 如果v不是数组，返回None
function countChars(v: String[]): Option<Number> {
  if (!Array.isArray(v)) return None;
  const length = v.map((v) => v.length).reduce((s, a) => s + a, 0);
  return Some(length);
}
```
在没有flatMap的情况下，如果要组合这两个方法，只能这样：

```typescript
function splitAndCount(str: String) {
  const strArray = splitString(str);
  if (strArray.isSome()) {
    const count = countChars(strArray.value);
    if (count.isSome()) {
      console.log(count.value);
      return;
    }
  }
  console.log("None");
}
```
注意这是一个有副作用的方法，并不是一个Pure Function。

这里需要嵌套两层if，才能把最终的结果取出来。可以想象如果还要继续调用更多的方法，要嵌套多少层。这不够优雅，可以考虑flatMap的引入

下面开始定义flatMap

```typescript
class SomeClass<T> {
  // ......
  // flatMap的定义很简单，只需要将func返回的结果直接返回(不再包一个SomeClass的容器)
  // (参考前面flatMap的函数类型定义)
  public flatMap<R>(func: (v: T) => R): R {
    return func(this._value);
  }
  // ......
}

class NoneClass {
  // ......
  public flatMap(): NoneClass {
    return None;
  }
  // ......
}
```
有了这个定义，上面的方法都可以改写成：

```typescript
function splitAndCountFlatMap(str: String) {
  splitString(str)
    .flatMap(countChars)
    .flatMap((v) => {
      console.log(v);
      return None;
    });
}
```
可以看到这里避免了所有的`if`

在有语法糖的scala中，可以写成：

```scala
for {
  strs <- splitString(str)
  count <- countChars(strs)
} yield count
```
### 以上，就是一个Monad