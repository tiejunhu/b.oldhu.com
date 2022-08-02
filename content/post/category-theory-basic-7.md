+++
tags = ["fp"]
series = "category-theory-basic"
date = "2022-07-29T16:12:12+08:00"
draft = false
title = "范畴论基础(7) - 基于Haskell重写例子"
toc = false
+++

### Haskell简介

> Haskell (/ˈhæskəl/) is a general-purpose, statically-typed, purely functional programming language with type inference and lazy evaluation. Designed for teaching, research and industrial applications, Haskell has pioneered a number of programming language features such as type classes, which enable type-safe operator overloading. Haskell's main implementation is the Glasgow Haskell Compiler (GHC). It is named after logician Haskell Curry

### 基本类型定义

```haskell
-- 定义数据类型
data Option a = Some a | None

-- 实现Show这个type class(类似于接口)
-- 用于展示这个数据类型
instance Show a => Show (Option a) where
  show (Some a) = "Some " ++ show a
  show None = "None"

-- 实现 Functor
-- Functor只要求实现一个fmap函数，函数签名为
-- fmap :: (a -> b) -> Option a -> Option b
instance Functor Option where
  fmap f (Some x) = Some (f x)
  fmap f None = None

-- 实现 Applicative Functor
-- pure :: a -> Option a
-- <*> :: Option (a -> b) -> Option a -> Option b
-- <*> 相当于之前定义的ap函数，只不过是个中缀操作符
instance Applicative Option where
  pure a = Some a
  (<*>) (Some f) (Some x) = Some (f x)
  (<*>) _ _ = None

-- 实现 Monad
-- >>= :: Option a -> (a -> Option b) -> Option b
-- >>= 相当于 flatMap，同时换了两个参数的顺序，是一个中缀操作符
instance Monad Option where
  (>>=) (Some x) f = f x
  (>>=) _ _ = None
```

有了这些定义，Option在Haskell中就是标准的Functor, Applicative Functor和Monad，Haskell会自动生成其它的一些操作符等。

### 使用

Functor

```haskell
functorTest = do
  -- 定义一个加法函数
  -- add :: a -> a -> a
  let
    add x y = x + y

  let
    add2 = add 2            -- add2 :: Integer -> Integer
    add3 = add 3            -- add3 :: Integer -> Integer
    add23 = add2 . add3     -- add23 :: Integer -> Integer

  let
    madd2 = fmap add2       -- madd2 :: Option Integer -> Option Integer
    madd3 = fmap add3       -- madd3 :: Option Integer -> Option Integer
    madd23 = madd2 . madd3  -- madd23 :: Option Integer -> Option Integer

  let
    r1 = add23 <$> Some 2   -- <$> 是fmap的中缀表达，这里相当于 fmap add23 (Some 2)
    r2 = madd23 (Some 2)

  -- r1 和 r2 的结果都是 Some 7 ( 2 + 2 + 3 )
  print (show r1 ++ " == " ++ show r2)
```

Applicative Functor

```haskell
applicativeTest = do
  let
    add x y = x + y         -- add :: a -> a -> a
    optionAdd = Some add    -- optionAdd :: Option (a -> a -> a)
                            -- optionAdd 类型也可以写成 Option(a -> (a -> a))
    mapAdd = fmap add       -- mapAdd :: Option a -> Option (a -> a)

  -- 第一步：Option(a -> (a -> a)) <*> Option a 得到 Option(a -> a)
  -- 第二步：Option(a -> a) <*> Option a 得到 Option a
  let r1 = optionAdd <*> Some 2 <*> Some 3

  -- 第一步：Option a -> Option (a -> a) 传入参数 Option a 得到 Option(a -> a)
  -- 第二步：Option(a -> a) <*> Option a 得到 Option a
  let r2 = mapAdd (Some 2) <*> Some 3

  -- 第一步：(a -> (a -> a)) <$> Option a 得到 Option(a -> a)
  -- 第二步：Option(a -> a) <*> Option a 得到 Option a
  let r3 = add <$> Some 2 <*> Some 3

  -- r1 和 r2 的结果都是 Some 5 ( 2 + 3 )
  print (show r1 ++ " == " ++ show r2 ++ " == " ++ show r3)
```

Monad

```haskell
monadTest = do
  -- 定义一个返回 Option a的函数
  -- alwaysAdd :: a -> a -> Option a
  let alwaysAdd x y = do
        if x > 0 && y > 0
          then Some (x + y)
          else None

  let
    optionAdd2 = alwaysAdd 2    -- optionAdd2 :: a -> Option a
    optionAdd3 = alwaysAdd 3    -- optionAdd3 :: a -> Option a

  -- 第一步的输入是 Option a，通过 >>= 给一个 a -> Option a的函数，返回一个Option a
  -- (没有返回嵌套Option，被flatMap了)
  -- 第二步与第一步一样
  let r1 = Some 4 >>= optionAdd2 >>= optionAdd3

  -- 在 Haskell 的 do notation 里，有 <- 语法糖支持
  -- 以下计算过程与上面的 r1 本质一样，都是调用了 Monad 的相关函数，但看起来更清楚
  let r2 = do
        x <- Some 4
        r1 <- optionAdd2 x
        optionAdd3 r1

  -- 结果都是 Some 9 (4 + 2 + 3)
  print (show r1 ++ " == " ++ show r2)
```