+++
draft = false
toc = false
date = "2022-09-15T11:43:18+08:00"
title = "双因素认证(2FA)与HOTP/TOTP"
+++

### 名词定义

HOTP: HMAC-based One-Time Password

TOTP: 基于时间的一次性密码算法（Time-based One-Time Password）

2FA: 双因素认证（Two-Factor Authentication）

### 使用方法

如何使用两步验证：
 * 开启两步验证，然后出现一个二维码，使用支持两步验证的移动应用扫一下二维码。这时候应用会出现一个6位数的一次性密码，首次需要输入验
   证从而完成开启过程。
 * 以后在登陆的时候，除了输入用户名和密码外，还需要把当前的移动应用上显示的6位数编码输入才能完成登陆。

### TOTP算法

现在基本能见到的2FA，都是TOTP算法。TOTP是基于HOTP的。

HOTP算法步骤：

  1. 客户端与服务器端共享一个密钥K，一个计数器C
  2. 用HMAC-SHA-1算法对K和C进行计算（HMAC-SHA-1本身初始化需要一个Key，就是K，需要一个负载，就是C），得到HS。HS长度为固定20字节。
  3. 取20字节的最后一个字节的低4位，用这个值作为10进制数字偏移量offset，从HS中以offset为起点，取4个字节，然后取这4个字节的后31位
     （忽略符号位），得到DT
  4. 将DT转化为10进制，然后对 10^n 取模。n是最终要保留的位数。

TOTP算法：

  1. 双方共享一个起始EPOCH时间T0（单位秒），共享一个步长X（默认30秒），得到 `T = (current epoch seconds - T0) / X`
  2. 用T替换HOTP算法中的C

### 二维码

初始化的时候，二维码里编码了什么信息？

二维码里编码了一个`otpauth://`开头的URL，格式为：

```
otpauth://totp/<服务名称>?secret=<密钥K明文>&algorithm=SHA1&digits=6&period=30
```
  * secret可以由服务器生成，直接明文传输
  * algorithm默认为SHA1（对应HMAC-SHA1），可以选SHA256（对应HMAC-SHA256），SHA512（对应HMAC-SHA512）
  * digits默认为6
  * period默认为30秒，是上面算法中的X

扫描二维码后，就拿到了密钥K，T0由客户端获取当前的EPOCH时间得到，X对应参数中的period，如果参数中没有传递，则为30秒。所有的参数客户端应该保存在一个安全的位置。
