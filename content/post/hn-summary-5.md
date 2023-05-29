+++
draft = false
toc = false
date = "2023-05-29T13:47:24+08:00"
title = "HackerNews上值得关注的新闻(5)"
+++

## How to Get Started with Tree-Sitter

[news](https://www.masteringemacs.org/article/how-to-get-started-tree-sitter) [comments](https://news.ycombinator.com/item?id=36106421)

emacs 29中将内置tree-sitter，这里介绍如何在emacs中使用tree-sitter。大致路径：

* 设置 treesit-language-source-alist
* 设置 major-mode-remap-alist，用各个 *-ts-mode 代替原来的 *-mode
* 修改对应的各种 mode-hook
* 对于elisp-mode，现在还没有 elisp-ts-mode，可以用 `(add-hook 'emacs-lisp-mode-hook #'(lambda () (treesit-parser-create 'elisp)))` 来启用treesit
* 找一个基于treesit的结构化编辑插件，享受treesit带来的好处

## Octopuses are building small “cities” off the coast of Australia (2017)

[article](https://arstechnica.com/science/2017/09/why-octopuses-are-building-small-cities-off-the-coast-of-australia/)

章鱼们在丢弃的贝壳中建造了一个小型村庄，称为Octlantis。这个村庄由10到15只章鱼组成，他们在几代人的努力下建造了一个由贝壳堆积而成的巢穴。

## The Never Married, a New Normal

[article](https://www.psychologytoday.com/us/blog/magnetic-partners/202305/the-never-married-a-new-normal)

根据Pew Research Center的Fry和Parker（2021）的报告，2021年，38％的成年人没有配偶，而1990年为29％，主要原因是婚姻率下降 。在过去的
50年中，婚姻率已经下降了约60％（Pandey，2023）。2021年，有4735万男性从未结婚，女性为4181万（Duffin，2022）。在过去的二十年中，从未
结婚的人数已经从21％上升到35％，增加了14个百分点（Wang，2020）

## Mastering CSS Blend Modes

[article](https://www.kodingkitty.com/blog/blend-modes/)

评论中有人提供的更好的 [文章](https://garden.bradwoods.io/notes/css/blend-modes)

## NO_COLOR

[article](https://no-color.org/)

文章建议所有的CLI，支持NO_COLOR=1这个环境变量，在设置时，不显示ANSI颜色

## Why I Left Rust

[article](https://www.jntrnr.com/why-i-left-rust/) 文章作者 JT 是基于Rust的[nushell](https://github.com/nushell/nushell) 的核心贡献者，也是Rust leadership group(4人小组)的成员，曾经也是TypeScript core的成员。

原因是在RustConf上，JeanHeyd Meneide被Rust Leadership Group推选为keynote speaker，但Rust team中，有人对此有强烈的不满。这时Rust Leadership Group中有一个人，在没有重新投票的情况下，通知RustConf将JeanHeyd降级为普通speaker，导致JeanHeyd拒绝参加RustConf

JT立即决定离开Rust Leadership Group。

JeanHeyd的[说明](https://thephd.dev/i-am-no-longer-speaking-at-rustconf-2023)


## Node.js built-ins on Deno Deploy

在deno的边缘结点上，直接支持import node.js的内置模块。

## A man sued Avianca Airline – his lawyer used ChatGPT

[article](https://www.nytimes.com/2023/05/27/nyregion/avianca-airline-lawsuit-chatgpt.html) [comments](https://news.ycombinator.com/item?id=36095352)

一个律师，使用chatGPT生成的内容(截图)作为证据，提交给法庭，不仅影响了案件，而且可能导致自己被disbar

## Everything you always wanted to know about mathematics (2013) [pdf]

CMU的CS和数学系第一学期的数学书，当前版本是：https://infinitedescent.xyz/

## Meta AI announces Massive Multilingual Speech code, models for 1000+ languages

Facebook的Whisper

## Paper Airplane Designs

[article](https://www.foldnfly.com/#/1-1-1-1-1-1-1-1-2)

link from comments: http://www.10paperairplanes.com/how-to-make-paper-airplanes/08-the-champ.html

## The tiny corp raised $5.1M

一个著名的黑客 [George Hotz](https://en.wikipedia.org/wiki/George_Hotz) ，募集了 5.1M$ ，计划使用AMD的 RDNA3 指令集，构建一个项目 [tinygrad](https://github.com/geohot/tinygrad)，让AMD进入机器学习的[性能竞赛](https://mlcommons.org/en/training-normal-21/)

可以在这里跟踪进度：https://github.com/users/geohot/projects/2

## Whistleblower Drops 100 Gigabytes Of Tesla Secrets To German News Site

> The files contain over 1,000 accident reports involving phantom braking or unintended acceleration--mostly in the U.S. and Germany.

## PrivateGPT

https://github.com/imartinez/privateGPT

Built with LangChain, GPT4All, LlamaCpp, Chroma and SentenceTransformers.

## Kanboard: free and open source Kanban project management software

https://kanboard.org/


