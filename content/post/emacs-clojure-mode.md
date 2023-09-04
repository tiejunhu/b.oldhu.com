+++
draft = false
toc = false
date = "2023-09-04T10:43:21+08:00"
title = "Clojure-mode作者与emacs-devel吵架"
+++

1. Stallman说Clojure现在(2023/8/24)非常重要了，想要[内置clojure的支持，加一个major-mode](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg00954.html)。

2. Clojure-mode已经存在了十几年，这个包的作者不愿意将包纳入GNU授权管理，只愿意以Non-GNU的方式发布
3. 有人提出这没有意义，因为clojure-mode和cider是所有[clojure开发者都会使用的](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01022.html)
4. 尤其不应该使用clojure-mode这个名字，因为会对现有的[生态产生影响](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01023.html)
5. Stallman[不以为然](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01063.html)，而且觉得这正好证明了他的观点：[emacs用户需要一个内置的clojure包](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01234.html)
6. clojure-mode作者Batsov[参与进来](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01137.html)，也是说不明白emacs-devel要做什么
7. 有人提出没必要做emacs内置的clojure支持，只要加一个auto suggest的功能就好，Stallman觉得这会让用户[混淆GNU和NonGNU的包，不同意](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01344.html)
8. elgot的作者Joao跳出来，说做个clojure支持没什么难的，[三行就行](https://lists.gnu.org/archive/html/emacs-devel/2023-08/msg01287.html)
9. Batsov觉得这很[搞笑](https://twitter.com/bbatsov/status/1698255369200394687?s=61&t=W3cLhw-urgfGB_vMRL6EqA)
10. [joao的回复彻底激怒了batsov](https://twitter.com/bbatsov/status/1698364626709172271?s=61&t=W3cLhw-urgfGB_vMRL6EqA)


