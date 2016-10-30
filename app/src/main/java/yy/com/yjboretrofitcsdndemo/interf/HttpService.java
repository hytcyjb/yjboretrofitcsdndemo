package yy.com.yjboretrofitcsdndemo.interf;

import retrofit2.Call;
import retrofit2.http.GET;
import yy.com.yjboretrofitcsdndemo.entity.NetWorkClass;

/**
 * @description: <描述当前版本功能>
 * <p>
 *     1.直接get就好 http://ip.taobao.com/service/getIpInfo.php?ip=202.202.33.33
 * </p>
 * @author: yjbo
 * @date: 2016-07-12 16:13
 */
public interface HttpService {


    public final static String baseHttp = "http://lbs.sougu.net.cn/";

    //这是我之前公司的接口，如果服务器不运行就没用了哦。其实只要是get的访问都可以请求，可以自己找个get请求就可以测试了
    @GET("app.php?m=souguapp&c=appusers&a=network")
    //这里的{id} 表示是一个变量
    Call<NetWorkClass> getFirstBlog(/** 这里的id表示的是上面的{id} */);

}
