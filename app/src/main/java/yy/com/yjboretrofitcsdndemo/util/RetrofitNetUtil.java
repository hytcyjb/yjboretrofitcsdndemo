package yy.com.yjboretrofitcsdndemo.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yy.com.yjboretrofitcsdndemo.interf.HttpService;

import static okhttp3.CacheControl.FORCE_CACHE;

/**
 * retrofit+rxjava+okhttp访问数据封装的方法
 *
 * @author yjbo
 * @time 2017/2/11 14:01
 */
public class RetrofitNetUtil {

    public RetrofitNetUtil() {
    }

    //接口回调：为了获取返回值
    private static OnrequestListener mListener;
    public void setOnrequestistener(OnrequestListener li) {
        mListener = li;
    }
    public interface OnrequestListener {
        void onService(HttpService service);
    }

    public static void requestData(final Context mcontext, String topUrl, String otherUrl, final int netGet, final int nonetGet) {

        final int cacheTime = CommonUtil.setCacheTime;
        /***
         * 拦截器，保存缓存的方法
         * 2016年7月29日11:22:47
         */
        Interceptor interceptor = new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (CommonUtil.checkNet(mcontext)) {//有网时
                    Response response = chain.proceed(request);
                    String cacheControl = request.cacheControl().toString();
                    Log.e("yjbo-cache", "在线缓存在1分钟内可读取" + cacheControl);

                    switch (netGet) {
                        case 0://总获取实时信息
                            return response.newBuilder()
                                    .removeHeader("Pragma")
                                    .removeHeader("Cache-Control")
                                    .header("Cache-Control", "public, max-age=" + 0)
                                    .build();
                        case 1://t（s）之后获取实时信息--此处是60s
                            return response.newBuilder()
                                    .removeHeader("Pragma")
                                    .removeHeader("Cache-Control")
                                    .header("Cache-Control", "public, max-age=" + cacheTime)
                                    .build();
                    }
                    return null;
                } else {//无网时
                    switch (nonetGet) {
                        case 0://无网时一直请求有网请求好的缓存数据，不设置过期时间
                            request = request.newBuilder()
                                    .cacheControl(FORCE_CACHE)//此处不设置过期时间
                                    .build();
                            break;
                        case 1://此处设置过期时间为t(s);t（s）之前获取在线时缓存的信息(此处t=60)，t（s）之后就不获取了
                            //这是设置在多长时间范围内获取缓存里面
                            CacheControl FORCE_CACHE1 = new CacheControl.Builder()
                                    .onlyIfCached()
                                    .maxStale(cacheTime, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
                                    .build();

                            request = request.newBuilder()
                                    .cacheControl(FORCE_CACHE1)//此处设置了t秒---修改了系统方法
                                    .build();
                            break;
                    }

                    Response response = chain.proceed(request);
                    //下面注释的部分设置也没有效果，因为在上面已经设置了
                    return response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached")
                            .removeHeader("Pragma")
                            .build();
                }

            }

            ;
        };

        //设置缓存
        File httpCacheDirectory = new File(mcontext.getCacheDir(), "cache_responses_yjbo");
        Cache cache = null;
        try {
            cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .addNetworkInterceptor(interceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(topUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final HttpService service = retrofit.create(HttpService.class);
        if (mListener == null) return;
        mListener.onService(service);
    }
}
