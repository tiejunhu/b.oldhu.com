+++
tags = ["fp"]
series = "category-theory-basic"
date = "2022-08-09T15:42:12+08:00"
draft = false
title = "范畴论基础(8) - Reader Monad"
toc = false
+++

### 提出问题

假设有这样一个数据类型，用于存储一个应用的配置信息

```haskell
data Config = Config
  { selectFirst :: Bool,
    greet :: String
  }
```

有两个函数都依赖这个配置

```haskell
-- selectUserToGreet 根据 Config 的 selectFirst 决定选择哪个user
selectUserToGreet user1 user2 cfg =
  if selectFirst cfg
    then user1
    else user2

-- greetUser 将 Config 中的 greet 与 user 拼在一起
greetUser user cfg =
  greet cfg ++ ", " ++ user
```

这样，调用这两个函数时，就都需要传入Config类型的参数

```haskell
main =
  let config = Config True "Hello"
      user = selectUserToGreet "Tom" "Jerry" config
      result1 = greetUser user config
  in print result1
```

带来的问题是，这样不够简洁，而且selectUserToGreet和greetUser如何compose ?

### 换个视角看这两个函数

从函数签名的角度看，这两个函数的签名分别是

```haskell
selectUserToGreet :: [Char] -> [Char] -> Config -> [Char]
greetUser :: [Char] -> Config -> [Char]
```

可以转换为

```haskell
selectUserToGreet :: [Char] -> [Char] -> (Config -> [Char])
greetUser :: [Char] -> (Config -> [Char])
```

所以，如果将 `Config ->` 看成一个容器，那这个容器里就放了值 `[Char]`，如果这个容器是一个Monad，那是不是所有的问题都能解决了？

### `Config -> a` 是个什么Monad

这个就是Reader Monad。

从后面的两个函数签名的角度理解，可以看成函数的输入参数里不再有Config，其返回值也不是简单的一个值类型 `a`，而是返回一个 `Config -> a` 的函数(一个包含着a这个值的容器)。

最终做完了所有的函数组合，计算之后，将确定下来的 Config 传给最终的 `Config -> a` Monad，就得到了我们需要的结果 a

### 定义Functor Reader，Applicative Functor Reader和Monad Reader

其实Reader不只是一个Monad，它也同时是Functor, Applicative Functor。

##### 首先来定义Reader这个类型

```haskell
-- 定义 Reader 为一个用 e 包装 a 的容器，提供一个 runReader 方法
-- 当调用 runReader 方法的时候，提供 e，返回 a
newtype Reader e a = Reader {runReader :: e -> a}
```

这样，`Reader e`就是一个容器，其中放了值类型 `a` (就像之前的Option a一样)。在未确定 e 之前，可以将 `Reader e` 看成一个整体。最终我们通过 runReader 方法，就能将 `e` 传入，拿到值 `a` 。

##### 定义Functor Reader

```haskell
instance Functor (Reader e) where
  -- fmap :: (a -> b) -> Reader e a -> Reader e b
  fmap f ra = Reader (\e -> f $ runReader ra e)

```

只要实现一个fmap函数即可。这里只要读懂了fmap的签名，其实现就很容易理解。

- fmap 是要对 ra 中包含的值 a 执行函数 f，然后再包回到 Reader e 中。
- ra 是 Reader e a 类型，有一个 runReader :: e -> a 方法
- 重新构造一个Reader e b，需要在Reader中包含一个方法，这个方法输入 e，返回 b 也就是 f a
- 这里用了一个lambda，先对 e 执行 ra 中包含的 runReader，得到 a，然后再调用 f

以上其实等价于对 e 先执行 runReader，再执行 f ，相当于 compose 。 所以也可以写成：

```haskell
instance Functor (Reader e) where
  fmap f ra = Reader (f . runReader ra)
```

##### 定义 Applicative Functor Reader

```haskell
instance Applicative (Reader e) where
  -- pure :: a -> Reader e a
  -- pure a 的含义是，生成一个包含 a 的 Reader 容器，也就是无论输入是什么，都返回 a
  pure a = Reader (\_ -> a)

  -- <*> :: Reader e (a -> b) -> Reader e a -> Reader e b
  -- 一个包含在 Reader e 中的 a -> b 函数，应用到 包含在 Reader e 中的 a 中，
  -- 返回一个包含在 Reader e 中的 b
  (<*>) rf ra = Reader (\e -> runReader rf e (runReader ra e))
```

##### 定义 Monad Reader

```haskell
instance Monad (Reader e) where
  -- >>= :: Reader e a -> (a -> Reader e b) -> Reader e b
  (>>=) ra f =
    let fea = runReader ra -- :: e -> a
        ferb = f . fea -- :: a -> Reader e b (after) e -> a == e -> Reader e b
        feb = \e -> runReader (ferb e) e -- :: e -> b
     in Reader feb
```

#### 如何在函数中获取到 e 的值？

这样定义之后，在返回 Reader 的函数中，我们如何访问到 Reader e 中的 e 呢？

这里定义这样两个神奇的函数

``` haskell
-- ask :: Reader a a
-- ask 本身是一个 a -> a 的 Reader，也就是说，值类型为 a，包装类型为 Reader a
ask = Reader id

-- asks :: (a -> b) -> Reader a b
asks f = do
  x <- ask -- ask 作为一个 Monad (Reader a 类型)，这里会把其中的值 (a) 取出来，给 x
  return (f x) -- 将 f x 的结果 b ，包装回 Monad (Reader a 类型)，并返回 (do 语法糖)
```

### 使用Reader Monad

```haskell
selectUserToGreet user1 user2 = do
  -- selectFirst 本身是一个 Config -> Bool 的方法，
  -- 调用 asks 后，成为了一个 Reader Config Bool 的 Monad
  -- 用 <- 可以将 bool 读出来
  select <- asks selectFirst
  if select
    then return user1
    else return user2

greetUser user = do
  greetMsg <- asks greet
  return (greetMsg ++ ", " ++ user)

selectAndGreet = do
  user <- selectUserToGreet "Tom" "Jerry"
  greet <- greetUser user
  return greet

main =
  let config = Config True "Hello"
   in print (runReader selectAndGreet config)
```