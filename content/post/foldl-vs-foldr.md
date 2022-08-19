+++
tags = ["haskell"]
date = "2022-08-18T18:05:12+08:00"
draft = false
title = "Haskell中foldl与foldr的区别"
toc = false
+++

- 大部分其它语言中的reduce，都是从最左面的元素开始迭代的，相当于foldl
- foldr是lazy的，可以用于无限的流式数据
- foldr支持短路运算，foldl不支持，以下代码可以正确返回False，如果是foldl就会死循环
```haskell
foldr (&&) False (repeat False)
```
- foldr相当于括号都在右面的函数展开，foldl相当于括号都在左面的函数展开

```
foldr (-) 0 [1,2,3,4]

(1 - (2 - (3 - (4 - 0)))) = -2

  -
 / \
1   -
   / \
  2   -
     / \
    3   -
       / \
      4   0
```
```
foldl (-) 0 [1,2,3,4]

((((0 - 1) - 2) - 3) - 4) = -10

        -
       / \
      -   4
     / \
    -   3
   / \
  -   2
 / \
0   1
```