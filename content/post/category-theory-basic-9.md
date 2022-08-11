+++
tags = ["fp"]
series = "category-theory-basic"
date = "2022-08-11T16:05:12+08:00"
draft = false
title = "范畴论基础(9) - Writer Monad"
toc = false
+++

### 提出问题

假设有一个half函数

```haskell
half x = x `div` 2
```

这时候要连续执行两次就很容易，可以compose

```haskell
quarter = half . half
```

如果函数除了要除2，还要写一条日志，那怎么办？

> 不能在函数内直接写文件或控制台，因为这样引入了副作用。也让函数本身与IO产生了关系，IO的异常会导致函数出现异常。

可以在函数的返回值里，同时返回一条日志

```haskell
half x = (x `div` 2, "I just halved " ++ show x ++ "!")
```

函数返回了一个tuple，其中第一项是数据结果，第二项是日志

这时候，函数无法再轻易地组合了。

### 解决方案，引入一个Writer Monad

```haskell
-- 一个包装了 out 和 a 的数据类型，通过 runWriter可以返回 out 和 a
newtype Writer out a = Writer {runWriter :: (out, a)}

instance Functor (Writer out) where
  -- fmap :: (a -> b) -> Writer out a -> Writer out b
  -- 下面定义中的 (Writer (w, a))，可以直接将 w 和 a 解构出来
  fmap f (Writer (w, a)) = Writer (w, f a)

-- Monoid 是一个支持 mempty (空值) 和 mappend (连接两个值) 的类型
-- 这里是要求 w 是一个 Monoid
instance Monoid w => Applicative (Writer w) where
  -- pure a 要返回一个 Writer w a，这时的 w 可以直接是 empty
  pure a = Writer (mempty, a)
  -- <*> :: Writer w (a -> b) -> Writer w a -> Writer w b
  (<*>) (Writer (out1, f)) (Writer (out2, a)) = Writer (out1 `mappend` out2, f a)

instance Monoid w => Monad (Writer w) where
  -- >>= :: Writer w a -> (a -> Writer w b) -> Writer w b
  (>>=) (Writer (out1, a)) f =
    let Writer (out2, b) = f a
     in Writer (out1 `mappend` out2, b)
```

引入一个tell函数，将要输出的内容生成一个Writer Monad

```haskell
tell out = Writer(out, ())
```

这样带日志的half函数就可以写成

```haskell
half x = do
  tell $ "before half " ++ show x ++ "!"
  let val = x / 2
  tell $ "I just halved " ++ show x ++ "!"
  return val
```

这里的do notation是一个语法糖

相当于：

```haskell
half2 x =
  pure x
    >>= \x1 ->
      Writer ("before half " ++ show x ++ "!", x1)
    >>= \x2 ->
      Writer (mempty, x2 / 2)
    >>= \x3 ->
      Writer ("I just halved " ++ show x ++ "!", x3)
```

计算过程利用do notation就可以写成：

```haskell
calc = do
  val1 <- half 8
  val2 <- half val1
  return val2
```

也可以写成：

```haskell
calc2 = do
  half 8 >>= half
```

