![](https://i.postimg.cc/nhyVbYCy/image.png)

# Data Fabric

Data Fabric 是多层次的可视化接口生成与业务逻辑编排系统，旨在加快简单重复开发的效率；其构建理念类似于 [Legoable](https://github.com/wx-chevalier/Legoble)，都是希望将部分代码层的工作外化给可视化配置，让更多的业务人员参与进来，让业务语言与代码相统一。

# Features | 特征

## 跨数据库查询接口

提供基于配置的、半自动化的、可供非技术人员使用的、交互友好的、支持权限与密钥配置的跨异构数据存储的动态接口生成服务。其逻辑很类似于 ETL 的过程，包括了数据的输入、抽取、转换、合并、输出等等操作，不过 Data Fabric 同时需要考虑到提供可供访问的接口、对于 JSON/XML 格式的支持、对于接口权限控制的支持等等。

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2017/2/1/QQ20170207-0fdfasdfasdfasdfsd.png)

我们面临的最终提交给消费者使用的 API 往往是由多个表、多个库，乃至于多个异构存储数据合并转换而来，借鉴自然语言处理或者编译原理的概念，我们可以选择自顶向下的来递归划分处理 API，也可以自底向上的进行合并处理。本文中笔者介绍的过程会以自底向上的合并为主，即

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2017/2/1/QQ20170207-01111.png)

![](https://coding.net/u/hoteam/p/Cache/git/raw/master/2017/2/1/QQ20170207-0asadas.png)

## 简单的接口 FaaS 编排

## 构建 SOP 处理流

## 构建决策流程

## 构建算法模型

# About

## Motivation & Credits

- [OpenWhisk #Project#](https://github.com/apache/incubator-openwhisk-runtime-nodejs): Apache OpenWhisk Runtime NodeJS supports Apache OpenWhisk functions written in JavaScript for NodeJS

- [Cube.js](https://cube.dev/): A complete open source analytics solution: visualization agnostic frontend SDKs and API backed by analytical server infrastructure.
