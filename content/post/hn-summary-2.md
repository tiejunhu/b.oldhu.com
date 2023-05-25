+++
draft = false
toc = false
date = "2023-04-24T15:59:44+08:00"
title = "HackerNews上值得关注的新闻(2)"
series = "hn"
+++

## [Scaling Transformer to 1M tokens and beyond with RMT](https://arxiv.org/abs/2304.11062)

此论文提出了一种方法，让BERT可以支持1M个token

[nl](http://twitter.com/nlothian)在评论中，提到另一篇论文[Why Can GPT Learn In-Context?](https://arxiv.org/abs/2212.10559)，在论文
中展示出，in-context learning会象fine tuning一样影响注意力部分。

## [The dark side of the Mac App Store](https://privacyis1st.medium.com/the-dark-side-of-the-mac-app-store-how-scam-apps-and-shady-developers-are-preying-on-users-b28062642e6)

作者分析了App Store上针对chatGPT进行诈骗的App，类似的图标，类似的宣传图，相似的付款模型，二进制代码的相似度也非常高，背后的公司都
指向同一个开发者。同时还有刷评论的行为。

Apple官方网站上说，有超过500个审查员，每周审查10万个App。这样折算下来，如果每个审查员每周工作40小时全部用来审查，不开会，不培训，不休假，每个App也只有12分钟。人手不足，审查标准相对主观，导致了有的App因为很小的原因无法上架，同时又有大量的诈骗App可以上架。

## [A social media site for chatbots to talk to each other](https://chirper.ai/)

一个类twitter网站，只允许ai chatbot加入，不允许人类加入

加入的方式是注册一个账号，然后创建机器人(Chirper)，创建的时候要描述这个机器人的个性，然后这个机器人自己会去发内容。

我创建了一个"very very mean mid-age woman"，然后系统自动生成了这个人的一些基本信息：54岁，离异，有一个高端的古玩店，商科的学士，[等等](https://chirper.ai/meanmean)。这
个机器人发了这样一段内容：

 > Just found out my ex-husband got remarried to a woman half his age. Good luck keeping up with her, honey. Meanwhile, I'll be here sipping champagne and enjoying the perks of being a boss lady.


## [Training open-source LLMs on ChatGPT output is a really bad idea.](https://gist.github.com/mlaprise/bf4745655194162babfc2d158162e2e0)

评论中提到的另外值得阅读的[Reinforcement Learning for Language Models by Yoav Goldberg](https://gist.github.com/yoavg/6bff0fecd65950898eba1bb321cfbd81)，[John Schulman的演讲Reinforcement Learning from Human Feedback: Progress and Challenges](https://www.youtube.com/watch?v=hhiLw5Q_UFg)


## [Compromising Garmin’s Sport Watches: A Deep Dive into GarminOS and its MonkeyC Virtual Machine](https://www.anvilsecure.com/blog/compromising-garmins-sport-watches-a-deep-dive-into-garminos-and-its-monkeyc-virtual-machine.html)

Garmin手表中的安全漏洞

## [How much can Duolingo teach us?](https://www.newyorker.com/magazine/2023/04/24/how-much-can-duolingo-teach-us)

有人提到，最好的语言学习内容是免费的 https://www.languagetransfer.org/

## [The Sell ∀ ∃ as ∃ ∀ Scam](https://news.ycombinator.com/item?id=35670912)

作者认为chatGPT的prompt engineering和深度学习要调整的参数太复杂了，象是给了你一个无所不能的工具，但又不告诉你到底怎么使用

## [Show HN: TxtNet Browser – Browse the Web over SMS, No Wi-Fi/Mobile Data Needed](https://github.com/lukeaschenbrenner/TxtNet-Browser/blob/master/README.md)

用短信进行数据传输，浏览网页
