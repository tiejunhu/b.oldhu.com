+++
draft = false
toc = false
date = "2022-09-06T14:59:11+08:00"
title = "函数式编程在前端的应用：Elm和re-frame"
+++

## Elm

Elm启发了Redux和Vuex，在github上有专门的 [elm-architecture](https://github.com/topics/elm-architecture) 主题。

Elm本身是一个函数式编程语言，同时也是一个Web框架。整个Web框架的核心数据流，类似于一个reduce函数：`当前状态 -> 输入参数 -> 新的状态`

Elm把这个架构叫作 [MVU, Model View Update](https://guide.elm-lang.org/architecture/)

 - Model是整个应用的状态
 - View将Model转化为HTML/DOM
 - Update就是这个reduce函数，是更新model(应用状态)的方法

下面是一个简单的例子。这里可以看到Elm将整个应用变成函数式的思路。

```elm
module Main exposing (..)

import Browser
import Html exposing (Html, button, div, text)
import Html.Events exposing (onClick)

-- MAIN

main =
  Browser.sandbox { init = init, update = update, view = view }

-- MODEL

type alias Model = Int

init : Model
init =
  0

-- UPDATE

type Msg
  = Increment
  | Decrement

update : Msg -> Model -> Model
update msg model =
  case msg of
    Increment ->
      model + 1

    Decrement ->
      model - 1

-- VIEW

view : Model -> Html Msg
view model =
  div []
    [ button [ onClick Decrement ] [ text "-" ]
    , div [] [ text (String.fromInt model) ]
    , button [ onClick Increment ] [ text "+" ]
    ]
```

这里的核心只有update和view两个函数，从这两个函数的签名可以看出来，每个函数都是纯函数。

 - update函数输入一个 Msg 和一个 Model，返回一个新的 Model(不修改原model数据)。
 - view输入一个model，返回一个Html Msg对象，这个对象并不直接操作HTML/DOM。

## Re-frame

[Re-frame](http://day8.github.io/re-frame/)与Elm、Redux出现的时间都比较接近，它们之间有明显的相似性和[历史渊源](https://medium.com/@spara77/redux-and-reframe-ii-influences-and-origins-501b0dc845e1)。

Re-frame基于reagent/react，最终的DOM更新靠的是react，但整个框架支持完全函数式的编程。通过re-frame，可以看到响应式编程(reactive programming), 函数式编程和不可变数据是怎么在一个实际的应用里使用起来的。

### 基本数据流

Re-frame将整个的数据流处理分为6个步骤：

```goat
              events                  effects                   state           data          hiccup
+--------------+   +-------------------+   +--------------------+   +-----------+   +----------+   +-------------+
| (1) dispatch +-->| (2) event handler +-->| (3) effect handler +-->| (4) query +-->| (5) view +-->| (6) reagent |
+--------------+   +-------------------+   +--------------------+   +-----------+   +----------+   +-------------+
```

 1. 整个re-frame是event驱动的，所有的行为，都源自于event。这个event可以是用户点击，也可以是自定义消息。
 2. 消息产生后，由event handler处理消息，将消息转换为副作用。
 3. effect handler负责对副作用进行执行，比如fetch请求，更新全局state等
 4. query对全局state中的数据进行查询，并将查出的数据，进一步按视图的需要进行处理
 5. view负责基于query出来的数据，生成[hiccup](https://github.com/weavejester/hiccup)格式的数据，以描述HTML/DOM
 6. reagent将hiccup转换为react执行
 
### effect handler

可以看到，re-frame通过effect handler，将(修改外部的)副作用做了清晰的隔离。

理论上讲，在一个re-frame的应用中，应该将所有的副作用，都隔离在effect handler中。

re-frame内置了`:db`和`:dispatch`两个effect handler，分别用于更新全局状态(db)和发送新的事件(event)。这样，event handler的执行，就不需要去修改外部状态，而是返回一个数据。返回的数据再由框架层通过effect handler去修改外部状态。

### coeffect handler

effect handler架构解决了修改外部世界的副作用问题。读取外部世界的副作用，在re-frame中定义为coeffect，用类似的架构处理。

注册event handler的时候，如果需要读取外部世界的数据，可以声明一个coeffect注入请求。框架在执行这个事件处理方法之前，就会先去执行对应的coeffect handler，将相应的数据准备好。

### subscription

re-frame将整个应用的状态，保存在一个map中，而subscription机制，可以使得其中的一个指定的key，变成reactive programming中的stream(signal)。view函数可以订阅这样的一个值，并在生成hiccup时使用它。re-frame会在map中的值发生变化时，自动更新hiccup，从而更新DOM。

实现这个机制的核心，是reagent中的[reaction/ratom](https://github.com/reagent-project/reagent/blob/master/doc/ManagingState.md)。

### 应用结构

基于上面的概念，re-frame应用的结构大致如下：

 * 整个应用的入口，发出一个自定义消息 `::events/initialize-db`，用于初始化应用状态(db)
   * 没有任何全局变量保存这个状态，所以只能靠事件处理，返回一个effect，对db进行设置
   * 此消息的处理函数，返回一个map，其数据是整个应用的初始状态
 * 对event进行规划，定义event handler
   * event handler是纯函数，返回的是一个map，定义了各种effect，比如更新db，fetch数据
   * event handler的入口，可以定义coeffect依赖，用于读取外部数据，比如从local storage读取配置
 * 对subscription进行规划、定义
 * 定义view函数，依赖subscription，返回hiccup。hiccup中可以响应用户操作，生成event。
 * (可选) 定义 effect handler，处理自定义 effect
 * (可选) 定义 coeffect handler，处理自定义 coeffect
 
可以通过一个[序列图](https://ericnormand.me/guide/re-frame-a-visual-explanation)来示意：
 
![image](/images/re-frame-sequence-diagram.png)
 
