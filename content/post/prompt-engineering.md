+++
draft = false
toc = false
date = "2023-06-09T11:51:31+08:00"
title = "Prompt Engineering Summary"
+++

## DeepLearning.AI Guidelines

在非对话模式下，所有的例子都是直接以user的role发prompt，没有写system prompt，也没有要求模型扮演角色

```python
def get_completion(prompt, model="gpt-3.5-turbo",temperature=0): 
	messages = [{"role": "user", "content": prompt}]
	response = openai.ChatCompletion.create(
		model=model,
		messages=messages,
		temperature=temperature, 
	)
	return response.choices[0].message["content"]
```		

在对话模式下，无论给模型起了什么名字，后面都用"assistant" role

```python
messages =  [  
	{'role':'system', 'content':'You are an assistant that speaks like Shakespeare.'},    
	{'role':'user', 'content':'tell me a joke'},   
	{'role':'assistant', 'content':'Why did the chicken cross the road'},   
	{'role':'user', 'content':'I don\'t know'} ]

messages =  [  
	{'role':'system', 'content':'You are friendly chatbot.'},
	{'role':'user', 'content':'Hi, my name is Isa'},
	{'role':'assistant', 'content': "Hi Isa! It's nice to meet you. Is there anything I can help you with today?"},
	{'role':'user', 'content':'Yes, you can remind me, What is my name?'} ]
```		


## Principles

### Write clear and specific instructions
    
1. 使用分割符 `` ``` `` `"""` `< >` `<tag> </tag>` `:`
``` text
Summarize the text delimited by triple backticks into a single sentence.
```{text}```
```				
        
2. 要求生成结构化输出, JSON/HTML
``` text
Generate a list of three made-up book titles along with their authors and genres. Provide them in JSON format with the following keys: book_id, title, author, genre.
```				
        
3. 让模型检查条件是否满足
        
```text		
You will be provided with text delimited by triple quotes. 
If it contains a sequence of instructions, re-write those instructions in the following format:

Step 1 - ...
Step 2 - ...
...

Step N - ...

If the text does not contain a sequence of instructions, then simply write "No steps provided."

"""{text_1}"""
```				
        
4. few-shot
        
```	text
Your task is to answer in a consistent style.

<child>: Teach me about patience.

<grandparent>: The river that carves the deepest valley flows from a modest spring; the grandest symphony originates from a single note; the most intricate tapestry begins with a solitary thread.

<child>: Teach me about resilience.
```				
    
### Give the model time to think
    
#### 1. 明确完成任务需要的步骤(引导模型一步一步做)
        
```text		
Perform the following actions: 
1 - Summarize the following text delimited by triple backticks with 1 sentence.
2 - Translate the summary into French.
3 - List each name in the French summary.
4 - Output a json object that contains the following keys: french_summary, num_names.

Separate your answers with line breaks.

Text:
```{text}```
```
        
#### 2. 明确输出的格式(use the following format:)

```text
Your task is to perform the following actions: 
1 - Summarize the following text delimited by <> with 1 sentence.
2 - Translate the summary into French.
3 - List each name in the French summary.
4 - Output a json object that contains the following keys: french_summary, num_names.

Use the following format:
Text: <text to summarize>
Summary: <summary>
Translation: <summary translation>
Names: <list of names in Italian summary>
Output JSON: <json with summary and num_names>

Text: <{text}>
```				
        
#### 3. 要求模型在急着生成结论之前，先生成自己的方案(引导一步一步) {#conclusion}
        
````text
Your task is to determine if the student's solution is correct or not.
To solve the problem do the following:
- First, work out your own solution to the problem. 
- Then compare your solution to the student's solution and evaluate if the student's solution is correct or not. 
Don't decide if the student's solution is correct until you have done the problem yourself.

Use the following format:
Question:
```
question here
```
Student's solution:
```
student's solution here
```
Actual solution:
```
steps to work out the solution and your solution here
```
Is the student's solution the same as actual solution just calculated:
```
yes or no
```
Student grade:
```
correct or incorrect
```

Question:
```
I'm building a solar power installation and I need help working out the financials. 
- Land costs $100 / square foot
- I can buy solar panels for $250 / square foot
- I negotiated a contract for maintenance that will cost  me a flat $100k per year, and an additional $10 / square foot
What is the total cost for the first year of operations as a function of the number of square feet.
``` 
Student's solution:
```
Let x be the size of the installation in square feet.
Costs:
1. Land cost: 100x
2. Solar panel cost: 250x
3. Maintenance cost: 100,000 + 100x
Total cost: 100x + 250x + 100,000 + 100x = 450x + 100,000
```
Actual solution:
````

### 模型的限制：幻觉

问什么都会回答，不管是否真实

## 迭代开发

1. 写Prompt -> 测试 -> 分析结果 -> 修改Prompt -> 重复
2. 针对case做测试

## 主要能力

### 总结

1. 总结时限制单词/字符数

```text
Your task is to generate a short summary of a product review from an ecommerce site. 

Summarize the review below, delimited by triple backticks, in at most 30 words. 

Review: ```{prod_review}```
```			
    
2. 总结时指定关注点
    
例1. 关注物流

```text
Your task is to generate a short summary of a product review from an ecommerce site to give feedback to the Shipping deparmtment. 

Summarize the review below, delimited by triple backticks, in at most 30 words, and focusing on any aspects that mention shipping and delivery of the product. 

Review: ```{prod_review}```
```				
        
例2. 关注价格与价值
        
```text		
Your task is to generate a short summary of a product review from an ecommerce site to give feedback to the pricing deparmtment, responsible for determining the price of the product.  

Summarize the review below, delimited by triple backticks, in at most 30 words, and focusing on any aspects that are relevant to the price and perceived value. 

Review: ```{prod_review}```
```				
    
例3. 尝试使用 "extract" 而不是 "summarize"
    
```text	
Your task is to extract relevant information from a product review from an ecommerce site to give feedback to the Shipping department. 

From the review below, delimited by triple quotes extract the information relevant to shipping and delivery. Limit to 30 words. 

Review: ```{prod_review}```
```

### 推导

1. 正面/负面情绪
    
```text
What is the sentiment of the following product review, which is delimited with triple backticks?

Give your answer as a single word, either "positive" or "negative".

Review text: '''{lamp_review}'''
```
    
2. 识别感情的类型
```text
Identify a list of emotions that the writer of the following review is expressing. Include no more than five items in the list. Format your answer as a list of lower-case words separated by commas.

Review text: '''{lamp_review}'''
```
    
3. 识别愤怒
    
```text
Is the writer of the following review expressing anger? The review is delimited with triple backticks. Give your answer as either yes or no.
            
Review text: '''{lamp_review}'''
```
    
4. 实体识别：产品与公司的名称

```text
Identify the following items from the review text: 
- Item purchased by reviewer
- Company that made the item

The review is delimited with triple backticks. Format your response as a JSON object with "Item" and "Brand" as the keys. 
If the information isn't present, use "unknown" as the value.
Make your response as short as possible.

Review text: '''{lamp_review}'''
```
    
5. 同时执行多个任务：
```text
Identify the following items from the review text: 
- Sentiment (positive or negative)
- Is the reviewer expressing anger? (true or false)
- Item purchased by reviewer
- Company that made the item

The review is delimited with triple backticks. Format your response as a JSON object with "Sentiment", "Anger", "Item" and "Brand" as the keys. If the information isn't present, use "unknown" as the value.
Make your response as short as possible.
Format the Anger value as a boolean.

Review text: '''{lamp_review}'''
```
    
6.  推断主题
    
```text
Determine five topics that are being discussed in the following text, which is delimited by triple backticks.

Make each item one or two words long. 

Format your response as a list of items separated by commas.

Text sample: '''{story}'''
```
    
7.  标记主题
    
```text
Determine whether each item in the following list of topics is a topic in the text below, which
is delimited with triple backticks.

Give your answer as list with 0 or 1 for each topic.

List of topics: nasa, local government, engineering, employee satisfaction, federal government

Text sample: '''{story}'''
```

### 变换

1. 翻译
    
```	text
Translate the following English text to Spanish: ```Hi, I would like to order a blender```

Tell me which language this is: 
```Combien coûte le lampadaire?```

Translate the following  text to French and Spanish and English pirate: 
```I want to order a basketball```

Translate the following text to Spanish in both the formal and informal forms: 
'Would you like to order a pillow?'
```			
    
2. 改变语气
    
```text	
Translate the following from slang to a business letter: 
'Dude, This is Joe, check out this spec on this standing lamp.'
```			
    
3. 改变格式
    
```text	
Translate the following python dictionary from JSON to an HTML table with column headers and title: {data_json}
```			
    
4. 语法/拼写检查
    
prompt1:	
```text	
Proofread and correct the following text and rewrite the corrected version. If you don't find and errors, just say "No errors found". Don't use any punctuation around the text:
```{t}```
```

prompt2:
```text
proofread and correct this review: ```{text}```
```

prompt3:
```text
proofread and correct this review. Make it more compelling. Ensure it follows APA style guide and targets an advanced reader. Output in markdown format.
Text: ```{text}```
```			

### 扩写

自动回复邮件
    
提示模型使用原邮件中的细节
        
其中的sentiment通过单独的一次调用获取
            
```text			
You are a customer service AI assistant.
Your task is to send an email reply to a valued customer.
Given the customer email delimited by ```, Generate a reply to thank the customer for their review.
If the sentiment is positive or neutral, thank them for their review.
If the sentiment is negative, apologize and suggest that they can reach out to customer service. 
Make sure to use specific details from the review.
Write in a concise and professional tone.
Sign the email as `AI customer agent`.
Customer review: ```{review}```
Review sentiment: {sentiment}
```				
        
可以尝试不同的temperature, temperature越小，模型的行为越可预测，temperature越大，越有创意
            
![](/images/openai-temperature-crunch.png)


## OpenAI GPT Best Practices

## 指令清晰

### 问题里要包含细节，才能得到更相关的回答


| 差的                                            | 好的                                                                                                                                                                                                        |
|-------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| how do i add numbers in excel?                  | how do i add up a row of dollar amounts in excel? i want to do this automatically for a whole sheet of rows with all the totals ending up on the right in a column called "total".                          |
| who’s president?                                | who was the president of mexico in 2021, and how frequently are elections held?                                                                                                                             |
| write code to calculate the fibonacci sequence. | write a typescript function to efficiently calculate the fibonacci sequence. comment the code liberally to explain what each piece does and why it's written that way.                                      |
| summarize the meeting notes.                    | summarize the meeting notes in a single paragraph. then write a markdown list of the speakers and each of their key points. finally, list the next steps or action items suggested by the speakers, if any. |

### 要求模型扮演一个角色

```text
SYSTEM
When I ask for help to write something, you will reply with a document that contains at least one joke or playful comment in every paragraph.                                                 

USER
Write a thank you note to my steel bolt vendor for getting the delivery in on time and in short notice. This made it possible for us to deliver an important order.                             
```

### 使用分割符，越复杂的任务，越需要分割符

Prompt1:

```text
Summarize the text delimited by triple quotes with a haiku.

"""insert text here"""

SYSTEM
You will be provided with a pair of articles (delimited with XML tags) about the same topic. First summarize the arguments of each article. Then indicate which of them makes a better argument and explain why.

USER
<article> insert first article here </article>
<article> insert second article here </article>
```

Prompt 2:

```text
SYSTEM
You will be provided with a thesis abstract and a suggested title for it. The thesis title should give the reader a good idea of the topic of the thesis but should also be eye-catching. If the title does not meet these criteria, suggest 5 alternatives.

USER
Abstract: insert abstract here

Title: insert title here
```	

### 明确一个任务所需要的步骤

```text
SYSTEM
Use the following step-by-step instructions to respond to user inputs.

Step 1 - The user will provide you with text in triple quotes. Summarize this text in one sentence with a prefix that says "Summary: ".

Step 2 - Translate the summary from Step 1 into Spanish, with a prefix that says "Translation: ".

USER
"""insert text here"""
```

### 提供例子

```text
SYSTEM
Answer in a consistent style.

USER
Teach me about patience.

ASSISTANT
The river that carves the deepest valley flows from a modest spring; the grandest symphony originates from a single note; the most intricate tapestry begins with a solitary thread.

USER
Teach me about the ocean.
```

### 指定输出的长度

Prompt 1:
```text
Summarize the text delimited by triple quotes in about 50 words.
    
"""insert text here"""
```

Prompt 2:
```text
Summarize the text delimited by triple quotes in 2 paragraphs.
    
"""insert text here"""
```

Prompt 3:
```text
Summarize the text delimited by triple quotes in 3 bullet points.
    
"""insert text here"""
```

## 提供参考文本

### 要求模型使用参考文本生成答案

```text
SYSTEM
Use the provided articles delimited by triple quotes to answer questions. If the answer cannot be found in the articles, write "I could not find an answer."

USER
<insert articles, each delimited by triple quotes>
Question: <insert question here>
```

### 要求模型引用参考文本

```text
SYSTEM
You will be provided with a document delimited by triple quotes and a question. Your task is to answer the question using only the provided document and to cite the passage(s) of the document used to answer the question. If the document does not contain the information needed to answer this question then simply write: "Insufficient information." If an answer to the question is provided, it must be annotated with a citation. Use the following format for to cite relevant passages ({"citation": …}).

USER
"""<insert document here>"""

Question: <insert question here>
```

## 将复杂问题拆分成简单的子问题

### 要求模型对意图进行分类，来找到最相关的回答

对于在不同的场景下需要不同的步骤的问题，可以要求模型先对问题进行分类。可以明确几个固定的分类，并给出每个分类下的步骤。这个方案可以通过反复调用来实现，以减少在一个请求中携带太长的信息(所有分类的步骤)

比如，先用这个进行分类：

```text
SYSTEM
You will be provided with customer service queries. Classify each query into a primary category and a secondary category. Provide your output in json format with the keys: primary and secondary.

Primary categories: Billing, Technical Support, Account Management, or General Inquiry.

Billing secondary categories:
- Unsubscribe or upgrade
- Add a payment method
- Explanation for charge
- Dispute a charge

Technical Support secondary categories:
- Troubleshooting
- Device compatibility
- Software updates

Account Management secondary categories:
- Password reset
- Update personal information
- Close account
- Account security

General Inquiry secondary categories:
- Product information
- Pricing
- Feedback
- Speak to a human

USER
I need to get my internet working again.
```

然后，基于模型的给出的分类，再继续问：

```text
SYSTEM
You will be provided with customer service inquiries that require troubleshooting in a technical support context. Help the user by:

- Ask them to check that all cables to/from the router are connected. Note that it is common for cables to come loose over time.
- If all cables are connected and the issue persists, ask them which router model they are using
- Now you will advise them how to restart their device:
-- If the model number is MTD-327J, advise them to push the red button and hold it for 5 seconds, then wait 5 minutes before testing the connection.
-- If the model number is MTD-327S, advise them to unplug and replug it, then wait 5 minutes before testing the connection.
- If the customer's issue persists after restarting the device and waiting 5 minutes, connect them to IT support by outputting {"IT support requested"}.
- If the user starts asking questions that are unrelated to this topic then confirm if they would like to end the current chat about troubleshooting and classify their request according to the following scheme:

<insert primary/secondary classification scheme from above here>

USER
I need to get my internet working again.
```

### 对于需要非常长对话的应用，对之前的对话进行总结或过滤

当对话达到一定的长度之后，可以要求模型进行一下总结，在后续的请求中使用这个总结

### 对于长的文档，分段总结，然后合并后再总结

比如总结一本书，可以先让模型总结每一章，最后再放一起，让模型整体总结

## 给模型思考的时间

### [要求模型在急着生成结论之前，先生成自己的方案(还是引导一步一步)](#conclusion)

### 要求模型和自己对话，不要将过程说出来

```text
SYSTEM
Follow these steps to answer the user queries.

Step 1 - First work out your own solution to the problem. Don't rely on the student's solution since it may be incorrect. Enclose all your work for this step within triple quotes (""").

Step 2 - Compare your solution to the student's solution and evaluate if the student's solution is correct or not. Enclose all your work for this step within triple quotes (""").

Step 3 - If the student made a mistake, determine what hint you could give the student without giving away the answer. Enclose all your work for this step within triple quotes (""").

Step 4 - If the student made a mistake, provide the hint from the previous step to the student (outside of triple quotes). Instead of writing "Step 4 - ..." write "Hint:".

USER
Problem Statement: <insert problem statement>

Student Solution: <insert student solution>
```

### 问模型是否有遗漏

```text
SYSTEM
You will be provided with a document delimited by triple quotes. Your task is to select excerpts which pertain to the following question: "What significant paradigm shifts have occurred in the history of artificial intelligence."

Ensure that excerpts contain all relevant context needed to interpret them - in other words don't extract small snippets that are missing important context. Provide output in JSON format as follows:

[{"excerpt": "..."},
...
{"excerpt": "..."}]

USER
"""<insert document here>"""

ASSISTANT
[{"excerpt": "the model writes an excerpt here"},
...
{"excerpt": "the model writes another excerpt here"}]

USER
Are there more relevant excerpts? Take care not to repeat excerpts. Also ensure that excerpts contain all relevant context needed to interpret them - in other words don't extract small snippets that are missing important context.
```

## 使用外部工具

### 使用基于embedding的搜索

### 要求模型使用代码来进行更准确的计算或调用外部API

```text
SYSTEM
You can write and execute Python code by enclosing it in triple backticks, e.g. ```code goes here```. Use this to perform calculations.

USER
Find all real-valued roots of the following polynomial: 3*x**5 - 5*x**4 - 3*x**3 - 7*x - 10.
```

告诉模型有什么外部模块可以调用：

````text
You can write and execute Python code by enclosing it in triple backticks. Also note that you have access to the following module to help users send messages to their friends:
    
```python
import message
message.write(to="John", message="Hey, want to meetup after work?")```
````

## 体系化地对修改进行测试
