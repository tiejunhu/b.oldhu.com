+++
draft = false
toc = false
date = "2022-10-13T12:55:30+08:00"
title = "Miller-Rabin质数测试"
+++

参考：http://www.matrix67.com/blog/archives/234

### 费马小定理

费马在1636年发现这个定理。如果p是质数，a是小于p的正整数，则 $ a^{p-1} \bmod p = 1 $

#### 证明

先证明一个定理：如果p是质数，则对于任意一个小于p的a，$ a, 2a, 3a, ..., (p - 1)a $ 除以p的余数正好是 $ 1, 2, ..., p - 1 $

反证法：

* 假设结论不成立，则有两个小于p的数m和n，使得 ma 和 na 除以 p 的余数相同。
* 若 $ n > m $，则 $ na-ma = (n - m)a $可被p整除。
* 由于p是质数，则必须是a或者n - m可被p整除。
* 但a和n - m都小于p，因此不可能。

基于这个定理，可知：$ 1 \times 2 ... \times (p - 1) = (p - 1)! $ 与 $ a \times 2a ... \times (p - 1)a = a^{p-1} \times (p - 1)!$ 除以p的余数相同。

将两个数同时除以 $ (p - 1)! $，即证明费马小定理。

### 费马小定理的逆命题

逆命题不一定成立，1819年发现了第一个反例：a = 2，p = 341，满足 $ 2^{340} \bmod 341 = 1 $，但341不是质数($ 11 \times 31 $)。a = 2的时候的这些反例叫伪质数。

在前10亿个自然数中共有50,847,534个质数，而满足$2^{n-1} \bmod n = 1$的合数n有5597个。如果将这些数做一个表，也可以快速判断小于10亿的质数。

另外，也可以扩展a，比如同时测试a = 2和a = 3，前10亿个自然数中同时以2和3为底的伪质数只有1272个。

随机选择若干个小于待测数的正整数作为底数a进行若干次测试，只要有一次没有通过测试就知道这个数是合数。这就是Fermat素性测试。

如果考虑了所有小于n的底数a，出错的概率是否就可以降到0呢？

Carmichael第一个发现这样极端的伪素数，称作Carmichael数。第一个Carmichael数仅仅是一个三位数，561。前10亿个自然数中Carmichael数也有600个之多。

### Miller-Rabin测试

为了解决伪质数的问题，Miller和Rabin两个人引入了新的方法。

定理：如果p为质数，x为任意正整数，并且 $ x^2 \bmod p = 1 $，则 $ x \bmod p = 1 $ 或 $ x \bmod p = p - 1 $

证明：

* $ x^2 \bmod p = 1 $ 等价于 $ x^2 - 1 \bmod p = 0 $
* 等价于 $ (x - 1)(x + 1) \bmod p = 0 $
* 由于p是质数，所以只能是
   1. x - 1被p整除，此时$ x \bmod p = 1 $；
   2. 或 x + 1 被p整除，此时 $ x \bmod p = p - 1 $

基于这个定理，在341这个数上做测试

* 因为 $ 2^{340} \bmod 341 = 1 $，相当于 $ (2^{170})^2 \bmod 341 = 1 $，此时需要检查 $ 2^{170} \bmod 341 $ 的结果是否是1或340。
* $ 2^{170} \bmod 341 = 1 $，继续检查 $ 2^{85} \bmod 341 $的结果
* $ 2^{85} \bmod 341 = 32 $，因此 341不是质数

注意待验证的质数p，肯定是个奇数，所以p - 1是个偶数。这样 `p - 1`就可以表示为 $ d \times 2^r $

这样Miller-Rabin测试就将费马测试增强为：

如果p是质数，则：
* 或者 $ a^d \bmod p = 1 $
* 或者存在`i`，使得 $ a^{(d \times 2^i)} \bmod p = p - 1 $ 。(当 `i = 0` 时，这个式子相当于 $ a^d \bmod p = p - 1 $)

Miller-Rabin测试同样是不确定算法，可以通过以a为底的Miller-Rabin测试的合数称作以a为底的强伪质数(strong pseudoprime)。

第一个以2为底的强伪质数为2047。第一个以2和3为底的强伪质数是1,373,653

如果被测数小于4,759,123,141，那么只需要测试三个底数2, 7和61就足够了。如果你每次都用前7个素数(2, 3, 5, 7, 11, 13和17)进行测试，所有不超过341,550,071,728,320的数都是正确的。如果选用2, 3, 7, 61和24251作为底数，那么 $ 10^16 $ 内唯一的强伪素数为46,856,248,255,981。