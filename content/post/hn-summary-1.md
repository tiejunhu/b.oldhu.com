+++
draft = false
toc = false
date = "2023-04-20T08:11:15+08:00"
title = "HackerNews上值得关注的新闻(1)"
series = "hn"
+++

### Stability AI发布了StableLM

Stability AI公司本身是Stable Diffusion模型背后的公司，与MidJourney的竞争差异就是开放与封闭。StableLM也是开放的大语言模型，本次发布
了3b与7b的版本，后面会继续发布15b与65b的版本。授权采用CC BY-SA-4.0，可免费用于商业用途。

StableLM是与EleutherAI合作发布的。EleutherAI本身也是一个非盈利的研究机构，本身也做很多模型的训练与发布，同时也提供免费的
[Pile](https://pile.eleuther.ai/)数据集。

StableLM使用了比公开的Pile三倍大的一个实验性的数据集，(声称)使得它在7b这个规模下，达到了非常好的效果。

https://stability.ai/blog/stability-ai-launches-the-first-of-its-stablelm-suite-of-language-models

https://github.com/Stability-AI/StableLM

7b版本的在线demo: https://huggingface.co/spaces/stabilityai/stablelm-tuned-alpha-chat

HN评论：

Garcia98用MMLU benchmark做了测试，效果不理想：

- stablelm-base-alpha-3b (3B params): 25.6% average accuracy
- flan-t5-xl (3B params): 49.3% average accuracy
- flan-t5-small (80M params): 29.4% average accuracy


vikp总结的pros/cons

pros:

- 4096 context width (vs 2048 for llama, gpt-j, etc)
- 3B to 65B released or in progress
- RL tuned models available
- Trained on more tokens than existing non-llama models
- 128 head dim, so can use flash attention (unlike GPT-J)

cons:

- No benchmarks released, or details about the model
- Somewhat restrictive license on the base models, and NC license on the RL models
- Small models only trained on 800B tokens, compared to 1T for llama-7B, and potentially more for other upcoming alternatives (RedPajama, etc).  I'd like to see their loss curves to see why they chose 800B.


### LlamaAcademy 针对API文档场景用GPT4/3.5生成训练数据，对Vicuna-13B进行fine tune

https://github.com/danielgross/LlamaAcademy

思路很好，值得关注

另外，在向量数据库的使用上，它保存了API文档的向量，同时又让GPT 3.5 Turbo做了summary，然后又保存了一份。

### Agents are the next AI killer app after ChatGPT

原文链接：https://www.latent.space/p/agents

Agent这个模式，个人认为是有问题的。hn上的争论也很多。可以先看一个这个作者的背景 [swyx](https://substack.com/profile/89230629-swyx)，
是个写手，可以假定他就是那种跟风各种buzz word的人

不过从他的文章里，还是可以获取不少有用的信息：

- Jasper AI going 0 to $75m ARR in 2 years
- [Auto-GPT](https://github.com/Significant-Gravitas/Auto-GPT)和[BabyAGI](https://github.com/yoheinakajima/babyagi)是现在两个比较受关注的Agent类项目
- LangChain估值200m
- 向量数据库：Pinecone (now worth $700m), Weaviate ($200m), and Chroma ($75m).
- Text to SQL: Perplexity AI ($26m Series A), Seek AI ($7.5m Seed)

### [Astral](https://astral.sh/)

用Rust改变Python工具链

因为[ruff](https://github.com/charliermarsh/ruff)的成功，产生了一家公司，准备继续在Python tooling领域用rust重写工具。

https://astral.sh/blog/announcing-astral-the-company-behind-ruff

raise了$4m，上一家类似的公司是JavaScript领域的[rome](https://rome.tools/)，[raise了$4.5m](https://rome.tools/blog/2021/05/04/announcing-rome-tools-inc/)

### [AI Incident Database](https://incidentdatabase.ai/)

记录各种与AI有关的事故（各种AI相关的负面新闻）


### [20年来最严重的的大米短缺](https://www.cnbc.com/2023/04/19/global-rice-shortage-is-set-to-be-the-largest-in-20-years-heres-why.html)

https://news.ycombinator.com/item?id=35627301

2023年全球大米减产

### 4chan's used car buying guide

https://news.ycombinator.com/item?id=35630618

最有趣的评论：Seeing Rust as a con in a Hackernews post, never thought I’d see the day! 第一次在hn上看到Rust是缺点。。。

### [动画演示Load Balancing](https://samwho.dev/load-balancing/)

### [Firefly](https://github.com/GetFirefly/firefly) BEAM的另一个实现

Firefly由两部分组成：

- Firefly Compiler，一个编译器，可以将Erlang代码编译成目标平台的native代码，目标平台支持x86, arm, WebAssembly
- Firefly Runtime，用Rust实现的一个运行时，支持OTP

Firefly与BEAM的区别：

- 支持编译到独立的可执行文件
- 支持编译到WebAssembly
- 支持AoT编译，也支持编译到bytecode
- 牺牲了一些功能，换回更多的优化。比如不支持(也没有计划去支持)erlang生态比较重要的hot code reloading。

hn评论里提到一个BEAM VM上的新语言[Gleam](https://github.com/gleam-lang/gleam)，由fly.io资助，看起来有点意思

### [My Emacs eye candy](https://xenodium.com/my-emacs-eye-candy/)

里面提到的eye candy我都用了，改天可以写一下我的emacs eye candy

### [An example of LLM prompting for programming](https://martinfowler.com/articles/2023-chatgpt-xu-hao.html) by Martin Fowler

Martin Fowler竟然写了一篇关于ChatGPT的文章。想想之前他写过的关于敏捷，重构，DDD，微服务...等主题的影响力。。。

### [The Age of the Crisis of Work, by Erik Baker](https://harpers.org/archive/2023/05/the-age-of-the-crisis-of-work-quiet-quitting-great-resignation/)

以下由BingChat总结：

- 工作正面临一场危机，表现为大规模的辞职和工作投入度下降，但这些现象并不均匀地分布在不同的行业和阶层中。
- 辞职现象主要发生在低收入的服务业，而白领阶层的辞职率并没有显著增加。辞职并不意味着对工作的彻底拒绝，而更多地是为了寻求更好的待遇和条件。
- 工作投入度下降主要发生在白领阶层，尤其是远程办公的员工。这可能与工作的无意义感、无尽感和无力感有关，也可能与工作与生活之间的界限模糊有关。

现在的失业率高，有主动的因素？

### RedPajama: Reproduction of LLaMA with friendly license

LLaMA是商业不友好的，facebook在[发布时说到](https://ai.facebook.com/blog/large-language-model-llama-meta-ai/): "we are releasing our model under a noncommercial license focused on research use cases"

[RedPajama](https://www.together.xyz/blog/redpajama)试图复制一个LLaMA，但是使用更加友好的协议

其中提到：

- LLaMA, Alpaca, Vicuna, and Koala都是半开放的
- Pythia, OpenChatKit, Open Assistant and Dolly是完全开放的

### Show HN: AI Playground by Vercel Labs

https://play.vercel.ai/

