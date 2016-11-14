
[csdn博客地址：基于Retrofit2，okhttp3的数据缓存（cache）技术--深入挖掘](http://write.blog.csdn.net/mdeditor#!postId=52975183)

/****
 * 基于Retrofit2，okhttp3的数据缓存（cache）技术---进一步研究
 * 2016年10月30日16:48:17
 * @author yjbo
 * @mail :1457521527@qq.com
 */

/****
 * 研究以下四个（2*2）方面：
 * <p>
 * 有网时:
 * 1.每次都请求实时数据；
 * 2.特定时间之后请求数据；（比如：特定时间为20s）
 * 无网时
 * 1.无限时请求有网请求好的数据；
 * 2.特定时间之前请求有网请求好的数据；（（比如：特定时间为20s））
 * ```
 */



###效果如下：
![sample](https://github.com/hytcyjb/yjboretrofitcsdndemo/blob/master/运行效果.gif)

###部分截图：
![效果图1](http://img.blog.csdn.net/20161030223411927)
![效果图2](http://img.blog.csdn.net/20161030223428864)
![效果图3](http://img.blog.csdn.net/20161030223438333)
