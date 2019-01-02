**由于 Smart QQ 从 2019 年 1 月 1 日起停止服务，所以该项目也已经停止维护了！**

# [XiaoV](https://github.com/b3log/xiaov) [![Build Status](https://img.shields.io/travis/b3log/xiaov.svg?style=flat)](https://travis-ci.org/b3log/xiaov)

## 简介

[XiaoV](https://github.com/b3log/xiaov)（小薇）是一个用 Java 写的 QQ 聊天机器人 Web 服务，可以用于社群互动：

* 监听多个 QQ 群消息，发现有“感兴趣”的内容时通过图灵|百度|茉莉机器人进行智能回复
* 监听到的 QQ 群消息可以配置推送到其他系统接口上
* 其他系统调用小薇进行 QQ 消息推送，比如论坛有新帖时自动推送到 QQ 群
* 加小薇为好友后可通过暗号（key）让她群推消息

总之，如果你需要一个连通 QQ 群和其他系统的机器人，小薇是个不错的选择！

## 启动

1. 安装好 **JDK1.8**、Maven 3
2. Clone 本项目，并在项目根目录上执行 `mvn install`
3. 执行 `mvn jetty:run`

这样小薇就启动了，然后根据输出提示进行扫码登录。

另外，如果你一定要在服务器上启动小薇（但真心不建议，请参考 FAQ 第二条）：

1. 打 war 包部署自己的容器，需要修改 latke.props 中的 `serverHost` 为你服务器的公网 IP、`serverPort` 为你容器监听的端口，如果你用了反向代理，那么 `serverHost` 可能就是你绑定的域名、`serverPort` 是 80。简而言之，这两个值是你最终访问接口时候的值
2. 通过 /login 地址来访问二维码

## 配置

配置文件主要是 src/main/resources/xiaov.properties：

* turing.api & turing.key 定义了图灵机器人的 API 地址和口令
* baidu.cookie 定义了百度机器人访问需要的 Cookie（登录百度，然后抓包）
* qq.bot.type 定义了机器人类型，1 是使用图灵机器人，2 则使用百度机器人
* qq.bot.name 定义了机器人的名字，这个主要是用于识别群消息是否“感兴趣”，比如对于群消息：“小薇，你吃过饭了吗？”包含了机器人的名字，机器人就对其进行处理
* qq.bot.key 定义了管理 QQ 或其他系统发过来的消息群推的口令，需要消息开头是这个口令，验证过后才会群推后面的消息内容
* qq.bot.pushGroups 定义了群推的群名，用 `,` 分隔多个群；也可以配置成 `*` 推送所有群
* qq.bot.pushGroupUserCnt 定义了群推时群人数的下限，只有大于等于这个人数的群才推送
* bot.follow.keywords 定义了监听群消息时的关键词，碰到这些词就做处理，比如对于群消息：“如何能在 3 天内精通 Java 呢？”包含了关键词 Java，机器人就对其进行处理
* bot.follow.keywordAnswer 定义了监听群消息时出现了关键词后的回复模版
* third.api & third.key 定义了其他系统 API 地址和口令，小薇会将所有监听到的消息通过该 API 推送到这个系统

## FAQ

### 如何正确地使用小薇 QQ 机器人？

论坛帖子[传送门](https://hacpai.com/article/1467011936362)。

### 为什么要单独做成一个 Web 服务，而不是一个依赖 jar？
 
做成依赖库的话会随应用部署，从开发的角度是比较方便，但有个致命的问题是应用一般是部署在云端，而登录扫码是在本地，这样会造成 QQ 的异地登录，导致很多问题。

所以需要将小薇部署在本地，保证用手机和小薇启动后 QQ 不出现异地登录。但是这也需要解决一个问题，即需要为小薇提供“内网穿透”的能力，比如使用 ngrok，具体可参考[这里](https://hacpai.com/article/1458787368338)。

### 为什么会出现“发送失败，Api返回码[1202]”？

这个问题是因为 QQ 服务器判断消息有问题时的返回，具体可关注这个 [issue](https://github.com/ScienJus/smartqq/issues/11)。

### 出现“Api返回码[103]”怎么破？

先关闭小薇，登录 [w.qq.com](http://w.qq.com) 后在设置中退出登录，最后再次启动小薇，这时扫码后应该就不会 103 了。

### 报错 Group list error [groupId=xxxx], please report this bug to developer... 怎么破？

同 103 错误处理步骤。

### 为什么输出日志是乱码？

是由于控制台编码造成，可以在将 src/main/resources/log4j.properties 中加入 log4j.appender.stdout.Encoding=UTF-8 来解决。

### 发现问题该怎么反馈？

* [论坛发帖](https://hacpai.com/tag/xiaov)（推荐做法）
* [New Issue](https://github.com/b3log/xiaov/issues/new) 

## API

### 其他系统推送 QQ 群

* 功能：小薇提供给其他系统调用的 HTTP 接口，用于将其他系统的内容推送到 QQ 群
* URL：/qq
* Method：POST
* Body：key={qq.bot.key}&msg={msgcontent}
* 例如：/qq?key=123456&msg=Hello

### QQ 群推送其他系统

* 功能：由其他系统提供给小薇调用的 HTTP 接口，用于将 QQ 群消息推送到这个系统（这个接口所需系统是实现的，这里是给出小薇的调用方式和参数）
* URL：{third.api}
* Method：POST
* Body：key={third.key}&msg={msgcontent}&user={hexuserid}
* 例如：/xiaov?key=123456&msg=Hello&user=0a

## 鸣谢

小薇的诞生离不开以下开源项目/产品服务：

* [Smart QQ Java](https://github.com/ScienJus/smartqq)：封装了 SmartQQ（WebQQ）的 API，完成 QQ 通讯实现
* [图灵机器人](http://www.tuling123.com)：赋予了小薇抖机灵的能力
* [百度机器人](https://baidu.com)：再次赋予了小薇抖机灵的能力
* [茉莉机器人](http://www.itpk.cn)：再再次赋予了小薇抖机灵的能力（据说很污 :joy:）
* [Latke](https://github.com/b3log/latke)：简洁高效的 Java Web 框架 
