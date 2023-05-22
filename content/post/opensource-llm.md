+++
draft = false
toc = false
date = "2023-05-22T14:15:50+08:00"
title = "Open Source LLM"
+++

### 排名

基于[lmsys arena](https://chat.lmsys.org/?arena)

| Rank | Name                    |
|------|-------------------------|
| 1    | vicuna-13b              |
| 2    | koala-13b               |
| 3    | RWKV-4-Raven-14B        |
| 4    | oasst-pythia-12b        |
| 5    | chatglm-6b              |
| 6    | stablelm-tuned-alpha-7b |
| 7    | alpaca-13b              |
| 8    | fastchat-t5-3b          |
| 9    | dolly-v2-12b            |
| 10   | llama-13b               |

### 继承关系

```plantuml
@startmindmap
* LLM
** LLaMA (Meta AI)
*** Alpaca (Standford)
**** Alpaca.cpp
**** Alpaca-LoRA
**** Chinese-LLaMA-Alpaca
**** GPT4-x-Alpaca \n基于GPT4生成语料(GPTeacher)对LLaMA 30B的微调
*** Vicuna \n(UC Berkeley, CMU, Stanford, and UC San Diego)\n使用ShareGPT的语料，对LLaMA 13b微调
**** Chinese-Vicuna\n参考Alpaca架构，基于LLaMA的LoRA
**** gpt4-x-vicuna-13b \n使用GPTeacher数据和其它GPT4数据集的微调
**** StableVicuna \n使用RHLF微调的Vicuna v0 13b
*** Koala \n伯克利对LLaMA 13B的微调，由100个人类进行验证
*** WizardLM \n使用LLM自动生成语料，对LLaMA 7b微调
*** llama.cpp
*** GPT4All \n基于800k GPT3.5-turbo生成语料的微调
*** Baize \n基于ChatGPT和Alpaca数据的微调
*** GPTQ-for-LLaMA \nLLaMA的4 bit版本
*** StackLLaMA \n使用StackExchange数据微调
** Pythia by EleutherAI
*** Dolly v2
*** OpenAssistant 
** StableLM \nStability的LLM
** ChatGLM-6B 清华
** 其它模型
*** GPT-J (EleutherAI)
**** GPT4All-J
*** OpenLLaMA \nOpen Reproduction of LLaMA 
**** OpenAlpaca \nbased on OpenLLaMA
*** BLOOM \nBigScience的开源大模型
**** BLOOM-LoRA
**** Petals \n使用176B BLOOM的微调
*** starcoder \n使用Github代码和issue训练的模型
*** MPT \nMosaicML训练的GPT基础模型
*** RedPajama \n另一个开源LLaMA模型
*** RWKV \n一个有Transformer性能的RNN模型
*** FLAN (Google)
**** FastChat-T5 \n基于Flan-t5-xl(3b)的微调
*** PaLM (Google)
@endmindmap






```
