+++
tags = ["fp"]
series = "category-theory-basic"
date = "2022-07-28T12:47:12+08:00"
draft = false
title = "范畴论基础(6) - Applicative Functor"
toc = false
+++

### 遗留问题

> 有一个遗漏了的函数签名，是 `T[A -> B]`。(在函数式编程里，函数是一等公民，函数本身也是一个值，所以也可以放进容器里)

有时候，一个方法也会被包装在容器中，还是以 `Option<T>` 为例：

假设一个业务，要根据年龄使用不同的规则，那就可以有一个函数，根据年龄，返回不同的方法。由于输入不一定都合法，所以有时候会返回 `None`

```typescript
function calcRule1(income: number) {
  return income * 0.1;
}

function calcRule2(income: number) {
  return income * 0.2;
}

function getRuleByAge(age: number) {
  if (age <= 5 || Number.isNaN(age)) return None;
  if (age <= 16) return Some(calcRule1);
  return Some(calcRule2);
}
```

这个`getRuleByAge`的类型定义是：`number -> Option[number -> number]`。这里相当于返回了一个包在`Option`中的方法。

如果要将这个`Option`中的方法，应用到`Option<number>`上，那就又要双层嵌套if :

```typescript
function calc(age: number, income: Option<number>) {
  const ruleFunc = getRuleByAge(age);
  if (ruleFunc.isSome()) {
    if (income.isSome()) {
      const result = ruleFunc.value(income.value);
      console.log(result);
    } else {
      console.log("not valid income")
    }
  } else {
    console.log("not valid age");
  }
}
```

### 引入一个辅助的ap方法

```typescript
function ap<T1, T2>(func: Option<(v: T1) => T2>, v: Option<T1>): Option<T2> {
  if (v.isNone() || func.isNone()) return None;
  return Some(func.value(v.value));
}
```

这个方法接受两个参数，第一个是一个包装在Option里的方法，输入T1，返回T2，第二个是包装在Option里的T1，返回是包装在Option里的T2。

方法的实现就是，无论第一个参数为None，还是第二个参数为None，都返回None。如果两个参数都是Some，那么将Option中包含的函数，应用到Option中包含的值上，并将返回值包装进Option中，返回

有了这个方法，就可以将上面的嵌套if写成这样：

```typescript
function calcWithAp(age: number, income: Option<number>) {
  ap(
    getRuleByAge(age), // Option包装的函数
    income // Option包装的Number
  ).flatMap((v) => {
    console.log(v)
  })
}
```

由于Option支持了ap这个函数，这时候，我们就说Option是 **Applicative Functor**