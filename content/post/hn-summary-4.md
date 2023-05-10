+++
draft = false
toc = false
date = "2023-05-10T19:13:09+08:00"
title = "HackerNews上值得关注的新闻(4)"
series = "hn"
+++

### [See this page fetch itself, byte by byte, over TLS](https://news.ycombinator.com/item?id=35884437)

基于一个[纯JS的tls库](https://github.com/jawj/subtls)，在浏览器里展示出TLS客户端从握手到获取数据的过程。

### [Language models can explain neurons in language models](https://news.ycombinator.com/item?id=35877402)

OpenAI试图用GPT-4来解释GPT-2里面的神经元。实际效果并不好。

> "... our technique works poorly for larger models, possibly because later layers are harder to explain."

### [Is sequential IO dead in the era of the NVMe drive?](https://news.ycombinator.com/item?id=35878961)

在SSD的介质下，随机读的性能非常高，顺序读已经没有必要，而随机写的写惩罚还是很高(虽然与机械硬盘的惩罚不同)，依然需要优化

### [Thunderbird Is Thriving: Our 2022 Financial Report](https://news.ycombinator.com/item?id=35880504)

Thunderbird的99.9%收入来自于用户捐献，2021年的用户捐献约为2.8M$，2022年为6.44M$，最大的收入增长来自于年底发布的Thunderbird 102，加入了in-app捐献功能，导致2022年仅12月的捐献收入就达到了近4M$。

日活30万，捐献情况：

- Top 3给的是3000 Euro, 2500 Euro，还有几人给了1000 Euro/USD
- 76人给了 500 USD
- 5%的捐献者给了超过100 USD
- 43%的捐献者是$5 - $20 USD

2022年开始时有员工15人，当前有24人，全年花费约3.56M$，其中80%为人员工资。

### [CheerpJ 3.0: a JVM replacement in HTML5 and WASM to run Java on modern browsers](https://news.ycombinator.com/item?id=35873552)

> With this and Ruffle, we will soon return to the days of Internet Explorer 6 but with 4k monitors!


### [Learning DNS in 10 Years](https://news.ycombinator.com/item?id=35870654)

- https://jvns.ca/blog/2023/05/08/new-talk-learning-dns-in-10-years/
- https://www.nslookup.io/learning/

### [Google “We have no moat, and neither does OpenAI”](https://news.ycombinator.com/item?id=35813322)

Google内部文档，认为开源对LLM的未来更有影响力

### [Replit's new Code LLM: Open Source, 77% smaller than Codex, trained in 1 week](https://news.ycombinator.com/item?id=35803435)

- Repo: https://github.com/replit/ReplitLM/tree/main/replit-code-v1-3b
- HuggingFace: https://huggingface.co/replit/replit-code-v1-3b
- Demo: https://huggingface.co/spaces/replit/replit-code-v1-3b-demo
- Early benchmark results: https://twitter.com/amasad/status/1651019556423598081

### [macOS Internals](https://news.ycombinator.com/item?id=35847715)

https://gist.github.com/kconner/cff08fe3e0bb857ea33b47d965b3e19f

### [I want to talk about WebGPU](https://news.ycombinator.com/item?id=35800988)

WebGPU随着Chrome 113正式发布，有可能对Web生态，对AI生态，对游戏生态都产生巨大的影响

### [Mojo – a new programming language for AI developers](https://news.ycombinator.com/item?id=35790367)

Mojo由Chris Lattner创建，这个人是LLVM, MLIR, Clang编译器和Swift的co-founder(by Wikipedia)。

Mojo代码的后缀名可以是.mojo，也可以是 .🔥

Mojo的长期目标是提供一个Python的超集，现在离这个目标还差不少，比如class还未支持。 当前直接支持CPython的运行时，可以在Mojo中直接使
用(无需重新编译)CPython的class和object

https://docs.modular.com/mojo/why-mojo.html

### [Ink: React for interactive command-line apps](https://news.ycombinator.com/item?id=35863837)

Node.js下的TUI框架

- https://github.com/vadimdemedes/ink
- Go: https://github.com/charmbracelet/bubbletea
- Python: https://github.com/textualize/textual
- Rust: https://github.com/fdehau/tui-rs

### [OpenLLaMA: An Open Reproduction of LLaMA](https://news.ycombinator.com/item?id=35798888)

LLaMA的License是不允许商用的，这里试图用一个更友好的License(APL 2.0)提供一个开源的LLaMA

