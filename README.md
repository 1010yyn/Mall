# Mall
这是一个简易购物系统的客户端。总系统利用Android技术+MySQL5.7数据库的架构实现。在Android studio上进行客户端的开发，在eclipse上进行服务器的开发，使用Tomcat服务器接收处理数据，数据库使用MySQL。


### Android客户端通信实现
主要使用http协议来进行客户端与服务器的通信，具体实现上使用HttpUrlConnection来进行通信。
![通信](http://imglf3.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2cmU3VXFwOVlqYXVqZ0dyd3NkTGpTaFV3VUpJaWpjbU13PT0.png?imageView&thumbnail=500x0&quality=96&stripmeta=0)

### 客户端功能设定
根据用户类型，分为顾客与商户两种，不同类型的用户所需功能不同。
![总体设计](http://imglf5.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2c2VTODN2V2dDNXlZNVlVV1dhK0dldVNNNXRER3hkNzVBPT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)

### 顾客功能设定
顾客主要在系统中进行购物，所需功能基本可分为 登录/注册、管理个人信息、商品/商店信息检索、浏览商品/商店信息、购物车管理、查看订单、收藏夹/黑名单以及基本的检索商品/商店功能。

![客户基本需求](http://imglf4.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2c0tnMGYvZ2RzcHpRUHVwWnZMN2pYblp5NE9SZ3hSZEZBPT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)

### 商户功能设定
商户在系统中主要进行商品经营，所需的功能基本可为 登录/注册、管理商店内商品、查看订单信息以及基本的检索商品功能。
商户与顾客登陆注册基本相同。

![商户基本需求](http://imglf3.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2aTN6UURrVEhJNEhLdjV5bDZPM08wWWNXQ2VYdURvR2dRPT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)

1. 登录/注册功能

    顾客登录/注册功能，主要是顾客利用用户名与密码来进行登录。在客户端的实现上，主要利用文本框来获取用户输入的信息，注册时则将信息直接添加到用户数据库中，登陆时则对数据库中的信息进行比对，相符则跳转进入系统主界面，否则提示登陆失败，并通过toast给出具体失败原因，例如密码错误或者是用户名不存在等。

![登录注册](http://imglf3.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2cHp2YzhWOCs2WTgyK2ZWTk83UUI5Ynl6VHUxaHJ5ekx3PT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)

2. 个人信息管理功能

    顾客在成功登陆时，服务器将用户的个人信息一并返回，在跳转至主界面时加载出来。信息的显示主要使用imageview以及text文本框。用户在个人中心可以对个人信息例如昵称，电话号码，地址等，进行修改。修改后的信息立刻在数据库中进行修改。

![个人信息](http://imglf5.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2b3VJZEdJWWtmR204OStWc2ovZ1ZoV2M5OURFMVB1eGRnPT0.png?imageView&thumbnail=500x0&quality=96&stripmeta=0)

3. 商品/商店信息检索功能

    系统主界面上方设置了一个搜索框，使用searchview来实现。用户在搜索框中输入关键词，确认搜索。系统获取关键词，向服务器发送检索请求，然后在数据库中针对关键词检索相关的商品和商店，返回结果，在搜索结果界面显示。
    
![检索](http://imglf4.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2ZzYvOWU4bXllVThmaGh4SDU4d21yMXlxNkU1QlFQNUFBPT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)
![检索界面](http://imglf6.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2bm50MXFpYlgwdGZDY3IrTkRQVEQyWkxNemJLMmFKeVZ3PT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)

4. 购物车管理功能

    用户在浏览商品时可将商品加入购物车，并在购物车内一并处理商品。
    购物车中的商品列表使用recycleveiw组件来进行实现，列表分为商店title和商品项目两种，根据数据属性的不同采用不同的布局。商店title采用textveiw来显示商店名文本，商品项目则采用另一种布局方式将商品图片、价格和商品数量和并显示在一个列表项中。由此来实现一个大的购物车列表中包含多个不同的商店商品列表的效果。
    
![购物车](http://imglf6.nosdn0.126.net/img/K2JMZkxjQi9SLzY4SzdwZlBBaTB2Z2lUNEtLMnhNNGFhWlovT2lVMzh4SWtIU3B5eExTMWJ3PT0.png?imageView&thumbnail=1680x0&quality=96&stripmeta=0)
