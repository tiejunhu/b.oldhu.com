+++
draft = false
toc = false
date = "2022-09-16T08:50:37+08:00"
title = "CQRS与Event Sourcing"
+++

### 介绍

CQRS = Command and Query Responsibility Segregation 命令和查询责任分离

Martin Fowler的介绍：[link](https://martinfowler.com/bliki/CQRS.html)

传统架构（不分离的架构）

![](/images/cqrs-single-model.png)

应用中有一个Model层，有一个Service层，前端所有读写都通过Service操作Model，然后转换为对DB的读写。

引入CQRS之后，变成：

![](/images/cqrs.png)

用户的写，转换为Command Model，写入数据库。用户的读，通过Query Model，进行读取。

数据库也可以分开

![](/images/cqrs-2db.png)

数据库分开后，Command与Query就无法保证严格一致了，只能保证最终一致。

### Event Sourcing

当Command Model数据库与Query Model数据库分开的时候，就需要通过Command去更新Query数据库，这通常是通过每一个Command产生一个Event，然
后在Event处理函数中去更新Query数据库。这就可以演变为Event Sourcing模式。

Command Model数据库中记录系统中的所有事件，事件只能增加，不能删除，不能修改。整个Query Model数据库成为所有历史Event积累到当前时刻的一个状态。这样就有可能基于Event数据库重放整个历史，重新构建Query Model数据库。

![](/images/cqrs-es.png)
