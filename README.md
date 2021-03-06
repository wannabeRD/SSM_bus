## 一.项目概述
一个基于SSM框架构建的maven子父工程。项目用于对城市公交系统的管理，共提供两种登录入口：  
**普通司机登录**：普通司机账号权限相对较小，只能够查看司机的相关信息和修改个人信息，无法对上一级的线路列表和片区模块进行查询和其他操作，更无法查看管理员账号信息。  
**管理员账号登录**：管理员账号拥有着系统的最高权限，可以实现对司机、线路、片区等所有模块的管理，甚至可以设置新的管理员账号。  
## 二.开发环境及相关技术栈简介
开发环境
  工具  | 版本信息
  ------------- | -------------
 JDK  | 11
IDE  | IntelliJ IDEA 2020.1
MySQL  | 8.0.20
tomcat  | 7.0.76
maven  | 3.6.0  

**相关技术栈简介**：SSM框架（spring、springMVC、mybatis），mybatis分页插件，EL表达式，JSTL，linux远程部署，git版本控制；前端框架采用JQuery EasyUI和基于Bootstrap框架的H-UI，使用JSON表达，发送Ajax请求；  
## 三.项目架构说明
**项目架构:**   
1. **SSM_bean** : 数据库对象实体

2. **SSM_dao** : 数据访问层代码 

   * \resources\database-conf : c3p0连接池配置文件
   * \resources\mapper : dao层接口映射xml文件
   * \resources\mybatis-conf : 用于对包配置别名
   * \resources\spring-conf : spring核心配置文件中与dao层相关部分
3. **SSM_interceptor** : 配置Controller层的过滤器
4. **SSM_service** : 业务逻辑层代码
   * \resources\spring-conf : spring核心配置文件中与Service层相关部分 
5. **SSM_util** ：下含两个工具类代码：1.CreateVerifiCodeImage 构建图片验证码  2.UploadFile 文件上传工具类
6. **SSM_web** ： 表现层代码
   * \java\com\firework\controller ：核心控制器类
   * \resources\spring-conf ： 下含两个部分：1.springMVC的核心配置文件 2.指定spring核心配置文件的位置
   * \webapp\image : 存放用户的初始头像
   * \webapp\static ： 静态资源文件夹，存放前端页面代码
   * \webapp\WEB-INF\view ： jsp动态页面（数据交互界面代码）
   
**模块依赖关系：**  
       SSM_util  
       SSM_bean         --> SSM_dao --> SSM_service --> SSM_web  
SSM_interceptor
## 四.功能细节说明
1.为防止用户在未登录的情况下直接进入系统内部，设置了**拦截器组件**，在执行Controller层方法之前都会先对用户的登录状态进行验证。   
2.登录注册界面使用验证码，通过时间戳清空缓冲区，实现**点击图片更换验证码。**  
3.用户登录成功后通过session域中设置的userInfo属性**获取用户身份**，并在登录成功界面返回用户身份信息。    
4.对某一模块进行查询操作时不仅可以通过该表的字段名称进行**模糊查询**，也可以选择该表的**上一级表中的字段名称查询**，建立了公交司机、线路与片区模块之
间的实质联系。    
5.在用户的个人信息模块中不仅包含字段信息，**更可以上传图片文件**。为**提高项目的可移植性**，我们将文件保存地址设在了target目录下。    
6.对用户修改数据时输入的参数进行了**合法性检查**和**二次复查**。例如：用户邮箱必须是有效的邮箱格式、手机号码必须是11位、修改密码时需要先输入原密码再输入新的密码，并且还需要对新的密码进行二次确认。  
7.个人认为整个项目**最大的特点**是在模糊查询的同时配合辅助以**分页查询**。但这里的分页查询也并没有对数据库进行实际意义上的分页查询，仍然是按照条件查询出了所有满足条件的数据封装到list集合中。但之后我们却引入**mybatis分页插件**中的**pageInfo类**对集合进行更近一步的封装：按照jsp页面传入的参数将指定数据存入一个更小的list集合当中，之后再将这个list集合发送到页面进行显示。即：**逻辑分页查询**。
