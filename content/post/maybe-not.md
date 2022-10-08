+++
draft = false
toc = false
date = "2022-10-08T09:55:53+08:00"
title = "Maybe Not (Rich Hickey) 相关"
+++


### 演讲的核心内容

* null本身是个巨大的错误，为了解决null的问题，引入了Maybe/Optional这样的类型
* 为什么要使用null/Maybe/Optional
  * 可选的参数
  * 有条件的返回值
  * 管理不完整的信息(一个数据结构中有可选字段)
* Maybe的问题：会破坏现有的API
* Clojure的解决方案：用Map
  * Clojure中的map是纯函数
  * 不需要引入范畴论
  * 用map表达可选值：只要不设置即可
  
### 积极的反馈

* from [lobsters](https://lobste.rs/s/zdvg9y/maybe_not_rich_hickey#c_povjwe)
  * 这里引入了一个Context观点非常重要。Maybe是有上下文的，只有在特定的函数(执行环境下)，Maybe才有意义。
  * 结构中的一个字段，对于一个函数是可选的，对另一个函数不一定。
  * 这也是protobuf3中删除optional的[原因](https://stackoverflow.com/questions/31801257/why-required-and-optional-is-removed-in-protocol-buffers-3)，Google花了沉重的代价学到了这个教训。
  * 对于一个要长期维护的复杂系统来说，这个观点很正确


### 负面的反馈

* from [Reddit Haskell](https://www.reddit.com/r/haskell/comments/a1ofh2/maybe_not_rich_hickey/)
  * Rich的观点，更多源自于感情而不是理性
  * 他在演讲中对Sheep例子的使用，对Haskell record可以命名的忽略，花了很长时间批评类型系统并不能表达所有的信息，然后5秒钟后，介绍Clojure Spec的时候，又说不能表达所有的信息是可以接受的。这一切都是在撒谎。
  * Rich有严重的NIH(Not Invented Here)综合症：社区有了[schema](https://github.com/plumatic/schema)，Rich非要做一个spec；社区有了lein，Rich非要做一个clj
