  1 开发工具Git，IntelliJ(版本控制的意识) 
  2 Spring Boot,Velocity
    (控制反转，依赖注入，
    思考初始化过程，面向切面的编程思想和用处，
    MVC的分层好处，使用springboot框架和直接写的不同，
    velocity前后端分离，如何渲染，流程)  
  3 myBatis （数据库前后的读取，xml配置）   
  4 登录/注册  
    （数据安全：salt;拦截器的思想；
     底层如何实现登录注册:token；
     token本项目中先放在mysql数据库中；
     后期改进：可以放在redis中，设计一个分布式的统一登录系统:当qq登录之后，再登录qq的网站就不需要再次填写登录信息;
     邮件激活）  
  5 前缀树（敏感词过滤，与普通的文本搜索，其优点在于其复杂度，为什么不用KMP算法，使用前缀树在于其内容的扩展性）    
  6 Redis （redis内的数据结构：跳跃表，哈希队列；redis的应用：异步队列、排行榜） 
  7 异步框架 （为了更快地把结果返回给用户,使用了队列，还想过用优先队列处理紧急的任务，
                框架的主要构成：消息的发送、消息的处理、事件的模型定义）  
  8 邮件，排序算法 (SSL(Secure Socket Layers))   
  9 推拉timeline  
  10 爬虫  
  11 solr搜索  (思想：通过SQL语句导进来，正排索引：文档 ---> 关键词
                                       倒排索引：关键词 ---> 文档
                                       搜索的重点去重：比较相似度（敏感哈希算法、皮尔逊相关系数，余弦定理），
                                       敏感哈希算法与哈希算法的对比)  
  12 单元测试/部署（nginx的正向代理和反向代理，负载均衡，流量切换）
  
  
  产品功能扩展：  
  1.用户注册，邮箱激活流程    
  2.首页滚动到底部自动加载更多  
  3.管理员后台管理  
  4.运营推荐问题置顶  
  5.timeline推拉结合  
  
  6.个性化首页，timeline更多时间  
  
  
  技术深度扩展
  1.搜索结果排序打分
    （本项目只是按照solr的默认版本，
    改进： 标题权重比内容更大；
          关键词出现的次数多，权重更大；
          点赞人数越多，则搜索的结果越靠前；
          同义词，搜索意图的查询）  
  2.爬虫覆盖用户，评论，内容去html标签  
  3.个性化推荐  