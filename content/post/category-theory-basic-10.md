+++
draft = false
toc = false
date = "2022-09-05T12:54:10+08:00"
title = "范畴论基础(10) - Kleisli Arrow"
+++

一个返回容器类的函数，签名是 `f :: a -> m b`，这里的类型 `m`，如果是个 `Monad`，那这个函数可以被称为 `monadic function`。

如果再来一个 `monadic function`，签名是 `g :: b -> m c`，这时候如果想要将 `f` 和 `g` 组合到一起，怎么办？

如果 `m` 已经是个 `monad`，那可以直接使用 `kleisli arrow`。定义如下：

```haskell
<=< :: (b -> m c) -> (a -> m b) -> (a -> m c) 
>=> :: (a -> m b) -> (b -> m c) -> (a -> m c)
```

可以看到 `<=<` 的含义更接近 $ \circ $，对于上面的 `f` 和 `g`，要写作 `g <=< f`

`>=>` 的含义更接近数据的流动，上面的两个函数“组合”可以写为 `f >=> g`

`<=<` 和 `>=>` 因为长的比较象鱼，所以一般会叫 `fish operator`

回到之前的 `monad` 的例子，可以用 `fish operator` 对 `monadic function` 进行组合

```haskell
monadTest =
  let alwaysAdd x y =
        if x > 0 && y > 0
          then Some (x + y)
          else None

      optionAdd2 = alwaysAdd 2
      optionAdd3 = alwaysAdd 3

      r1 = Some 4 >>= optionAdd2 >>= optionAdd3

      r2 = do
        x <- Some 4
        r1 <- optionAdd2 x
        optionAdd3 r1

      -- fish operator 的使用
      composedAdd = optionAdd2 <=< optionAdd3

      r3 = composedAdd 4

   in print (show r1 ++ " == " ++ show r2 ++ " == " ++ show r3)
```
