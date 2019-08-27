# AndroidBasicReview
《Android第一行代码》案例实现与方法总结，以及在着整个基础部分回顾过程中发现的一些细节的标记与探究，近期着重于发现，按照这本书的大概内容整理完后会具体分析发现的细节问题。除了下面的提纲，具体的内容整理在每个案例工程的具体代码中的注释里面。



1. 数据持久化
   + SharedPreferences存储
     + 以“键值对”的方式存储数据，以XML格式对数据进行管理。
     + 向SharedPreferences.Editor对象添加数据，从SharedPreferences读取数据。
     + 适用于保存一些简单的数据和键值对。
   + SQLite数据库存储
     + SQLite数据库是Android系统内置的一款轻量级的关系型数据库，支持标准SQL语法。
     + Android提供了SQLiteOpenHelper这个抽象类作为帮助类来进行数据库的创建和升级。
     + 关于增删改查，可以使用Android提供的API，也可以使用SQL语句。
     + 操作数据库的相关开源框架：
       + LitePal
   + 文件存储
2. 内容提供器