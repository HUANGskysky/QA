1.登录注册问题
    1） 注册 (LoginController.reg())
        userService.register（username,password）:
        1.1 检测待注册的用户名和密码是否为空，为空则返回map提示
        1.2 检测用户名是否已经被注册了
        1.3 创建User，利用UUID创建长度为5的salt，使用MD5加密（密码+salt）
        1.4 利用userId创建一个ticket

        1.5 在map中提取键“ticket”所对应的值
        1.6 根据上面提取出来的键值对创建一个cookie并返回response.addCookie(cookie)

    2） 登录(LoginController.login())
        userService.login（username,password）
        2.1 检测待注册的用户名和密码是否为空
        2.2 检测用户名是否存在，检测密码是否正确，若否，则返回
        2.3 创建Ticket ticket
        2.4 利用ticket作为值创建Cookie返回给客户端


    3） 拓展
        3.1 Cookie和Session
            3.1.1 因为HTTP协议是一种无状态的协议，所以需要使用Cookie/Session进行会话跟踪
            3.1.2 Cookie是服务器发送到用户浏览器并保存在本地的一小块数据，它会在浏览器之后向同一个服务器在此发起请求被携带上，
                  用于告知服务器端两个请求是否来自同一个浏览器
            3.1.3 除了可以将用户信息通过Cookie存储在客户端，也可以利用Session存储在服务器端，存储在服务器端的信息更加安全
            3.1.4 本项目正是采用这种方式，用户在注册/登录之后都会创建一个ticket作为SessionId
            3.1.5 本项目维护用户登录状态的过程如下：
                  用户登录时，用户提交包含用户名和密码的表单，放入HTTP请求报文中；
                  服务器验证该用户名和密码
                  如果正确则创建Ticket，该ticket会关联userId，创建Cookie字段
                  服务器返回的响应报文的Set-Cookie首部字段包含了这个ticket返回给客户端
                  之后客户端每次向服务器发起请求时都会包含这个Cookie值，在服务器响应之前会首先被拦截器拦截，根据ticket获得用户信息。
        3.2 用户数据安全性
            本项目使用的
            3.2.1 不存放明文密码在数据库，通过salt加密用户密码，但是加盐也有泄露的可能。




1.基于redis的消息队列
    常用的消息队列有RabbitMQ,ActiveMQ,Kafka等，这些都是开源的功能强大的消息队列，适合在企业项目中应用。

    Redis实现消息队列提供了两种方式：
    1) 生产者-消费者模式：让一个或者多个客户端监听消息队列，一旦消息到达，使用抢占式的方式，如果队列中没有消息，则消费者继续监听。
    2) 发布-订阅者模式：一个或者多个客户端订阅消息频道，只要发布者发布消息，所有的订阅者都能够收到消息，通过公平的方式。类似于设计模式的观察者模式。

    在包 com.nowcoder.async中，下面讲一下几个类
    @Service JedisAdapter jedisAdapter 是用于连接redis，类似于SQL的DAO层，
        里面的函数有：集合元素增加：sadd()，集合元素删除：srem(),集合元素统计个数：scard(),
        集合元素判断是否包含：sismember,链表添加：lpush,brpop等
        使用了连接池技术JedisPool，并且用到了jedis的事务概念

    EventModel:事件模型
    EventHandler：接口，实现者有FollowHandler，LikeHandler
    EventProducer:事件的生产者，其中fireEvent函数中，将传进来的eventModel通过JSONObject转化为字符串，并且通过lpush将事件放进消息队列
    EventConsumer:事件的消费者，该类实现了 InitializingBean, ApplicationContextAware
         Spirng的InitializingBean为bean提供了定义初始化方法的方式。InitializingBean是一个接口，它仅仅包含一个方法：afterPropertiesSet
         afterPropertiesSet()函数用于初始化，将所有的Handler作为值放进config,而key为事件的类型，并创建线程，取出消息队列的Handler，会发生阻塞等待的，进行相应的处理操作

    redis的事务概念：
        基本事务的实现用到命令multi和exec，这种事务可以让一个客户端在不被其他客户端打断执行多个命令，和关系型数据库那种可以在执行过程中进行回滚的事务不同，
        在redis中，被mutli命令和exec命令包围的所有命令会一个接一个地执行，知道所有的命令都执行完毕为止，当一个事务执行完毕之后，redis才会处理其他客户端的命令。

  2. 拦截器（2个）：
        登录拦截：判断用户是否在登录的状态，如果不是重定向至登录的页面
        设置HostHolder：拦截用户的cookie，通过cookie来获取用户，并把该用户添加至HostHolder，用以全局

        拦截器的执行流程：
        在request被响应之前->prHandle->request被响应之后和视图渲染之前->postHandle->以及request全部结束之后->afterCompletion

        实现自定义拦截器只需要3步：
        1、创建我们自己的拦截器类并实现 HandlerInterceptor 接口。
        2、创建一个Java类继承WebMvcConfigurerAdapter，并重写 addInterceptors 方法。
        2、实例化我们自定义的拦截器，然后将对像手动添加到拦截器链中（在addInterceptors方法中添加）。


  3. ThreadLocal的使用


  4. 数据库的安全性
        1）不写明文的密码，使用MD5加密，使用salt
        2）



  5. solr
     步骤：
        1）建立索引：加入索引的字段，并标明索引类型（在managed-schema文件配置 ）
        2）建立中文分词器（在managed-schema文件配置 ）
        3）配置文件solr-data-config.xml，建立solr与mysql数据库的连接，数据库与刚才步骤1建立的索引字段关联
        4）从数据库中导入question内容（配置solrconfig.xml）


      使用solr的好处：
        1）如果不使用solr其实也可以通过sql语句的模糊搜索"like"，但是这达不到分词的效果，所以使用solr可以达到分词的效果？



   6. 单元测试
       1） 初始化数据
       2） 执行要测试的业务
       3） 验证测试的数据
       4） 清理数据









