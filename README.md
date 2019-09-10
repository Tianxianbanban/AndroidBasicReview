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
3. 通知
   + NotificationManager负责将Notification通知；
   + 一个延迟性质的意图可以给通知添加点击效果。
4. 多媒体
   + 内置系统应用相机的启动Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
   + 调用相册Intent intent=new Intent("android.intent.action.GET_CONTENT");
   + 播放多媒体文件，主要用到MediaPlayer类对象；
   + 播放视频，主要用到VideoView。
5. 网络编程
   + HTTP协议
   + OkHttp框架
6. Android多线程编程
   + 异步消息处理机制
     + Message、Handler、MessageQueue、Looper；runOnUIThread()方法就是一个异步消息处理机制的封装；
     + 继承AsyncTask，实现onPreExecute()、DoInBackground()、onProgressUpdate()、onPostExecute()。
7. 服务
   + 定义、创建、启动、停止服务；
   + 活动与服务进行通信，需要借助Binder对象；
   + 服务的声明周期：

