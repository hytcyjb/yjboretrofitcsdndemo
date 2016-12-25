package yy.com.yjboretrofitcsdndemo;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yy.com.yjboretrofitcsdndemo.entity.NetWorkClass;
import yy.com.yjboretrofitcsdndemo.interf.HttpService;

import static okhttp3.CacheControl.FORCE_CACHE;

/****
 * 基于Retrofit2，okhttp3的数据缓存（cache）技术---进一步研究
 * 2016年10月30日16:48:17
 *
 * @author yjbo
 * @mail :1457521527@qq.com
 */

/****
 * 研究以下四个（2*2）方面：
 * <p>
 * ```
 * 有网时:
 * 1.每次都请求实时数据；
 * 2.特定时间之后请求数据；（比如：特定时间为20s）
 * ```
 * ```
 * 无网时
 * 1.无限时请求有网请求好的数据；
 * 2.特定时间之前请求有网请求好的数据；（（比如：特定时间为20s））
 * ```
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.show_result)
    TextView showResult;
//    int maxAge = 0; // 在线缓存在1分钟内可读取
    /**
     * netGet:有网时
     * 0:每次都请求实时数据
     * 1:特定时间之后请求数据；（比如：特定时间为20s）
     */
    int netGet = 0;
    /**
     * nonetGet:无网时
     * 0:无限时请求有网请求好的数据
     * 1:特定时间之前请求有网请求好的数据；（（比如：特定时间为20s））
     */
    int nonetGet = 0;
    int cacheTime = 10;
    @Bind(R.id.show_kind_ask)
    TextView showKindAsk;
    String netGetStr = "";
    String nonetGetStr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showKindAsk.setVisibility(View.GONE);
        initGet();
    }

    /***
     * 获取服务器数据
     */
    private void initGet() {
        //设置缓存
        File httpCacheDirectory = new File(MainActivity.this.getCacheDir(), "cache_responses_yjbo");
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
                .baseUrl(HttpService.baseHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        final HttpService service = retrofit.create(HttpService.class);
        Call<NetWorkClass> call = service.getFirstBlog();
        call.enqueue(new Callback<NetWorkClass>() {
            @Override
            public void onResponse(Call<NetWorkClass> call, retrofit2.Response<NetWorkClass> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "数据请求成功", Toast.LENGTH_SHORT).show();
                    NetWorkClass netWorkClass = response.body();
                    showResult.setText(netWorkClass.toString());

                    showKindAsk.setText("当前是：" + "\n1." + netGetStr + "\n2." + nonetGetStr);
                } else {
                    showResult.setText(response.code() + "--数据请求失败--");
                }
            }

            @Override
            public void onFailure(Call<NetWorkClass> call, Throwable t) {

            }
        });
    }

    /***
     * 拦截器，保存缓存的方法
     * 2016年7月29日11:22:47
     */
    Interceptor interceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            if (checkNet(MainActivity.this)) {//有网时
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
                    //break;
                    case 1://t（s）之后获取实时信息--此处是20s
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", "public, max-age=" + cacheTime)
                                .build();
                    //break;
                }
                return null;
            } else {//无网时
                switch (nonetGet) {
                    case 0://无网时一直请求有网请求好的缓存数据，不设置过期时间
                        request = request.newBuilder()
                                .cacheControl(FORCE_CACHE)//此处不设置过期时间
                                .build();
                        break;
                    case 1://此处设置过期时间为t(s);t（s）之前获取在线时缓存的信息(此处t=20)，t（s）之后就不获取了
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
    //这是设置在多长时间范围内获取缓存里面
    public CacheControl FORCE_CACHE1 = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(cacheTime, TimeUnit.SECONDS)//CacheControl.FORCE_CACHE--是int型最大值
            .build();


    /***
     * 检查网络
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对像
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info == null || !info.isAvailable()) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 弹出框
     *
     * @param title 标题
     * @param msg   提示内容
     * @param type
     */
    public void showDialogFragment(String title, String msg, final int type) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle(title); //设置标题
        builder.setMessage(msg); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
//                    Toast.makeText(MainActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();
                initGet();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "忽略" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();

        /**
         * 这个弹窗是引用了第三方的，用了会报错，修改了
         * @author yjbo
         * @time 2016/12/25 20:43
         */
//        final SimpleDialog.Builder builder = new SimpleDialog.Builder() {
//            @Override
//            public void onPositiveActionClicked(DialogFragment fragment) {
//                super.onPositiveActionClicked(fragment);
//                switch (type) {
//                    case 0:
//                        showKindAsk.setVisibility(View.VISIBLE);
//                        initGet();
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            @Override
//            public void onNegativeActionClicked(DialogFragment fragment) {
//                super.onNegativeActionClicked(fragment);
//            }
//        };
//        builder.message(msg)
//                .title(title)
//                .positiveAction("确定")
//                .negativeAction("取消");
//        fragment = DialogFragment.newInstance(builder);
//        fragment.show(MainActivity.this.getSupportFragmentManager(), null);
    }

    private DialogFragment fragment;


    @OnClick({R.id.click_1_txt, R.id.click_2_txt, R.id.click_3_txt, R.id.click_4_txt, R.id.btn_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.click_1_txt:
                netGet = 0;
                nonetGet = 0;
                break;
            case R.id.click_2_txt:
                netGet = 0;
                nonetGet = 1;
                break;
            case R.id.click_3_txt:
                netGet = 1;
                nonetGet = 0;
                break;
            case R.id.click_4_txt:
                netGet = 1;
                nonetGet = 1;
                break;
            case R.id.btn_again:
                break;
        }
//        showKindAsk.setVisibility(View.VISIBLE);
        switch (netGet) {
            case 0:
                netGetStr = "有网实时更新";
                break;
            case 1:
                netGetStr = "有网时设置缓存时间为" + cacheTime + "s,之后再次获取数据";
                break;
        }
        switch (nonetGet) {
            case 0:
                nonetGetStr = "无网时不设置缓存时间，一直可以获取缓存";
                break;
            case 1:
                nonetGetStr = "无网时设置缓存时间为" + cacheTime + "s,之后不能获取缓存";
                break;
        }
//        showKindAsk.setText("当前是：" + "\n1." + netGetStr + "\n2." + nonetGetStr);
        showDialogFragment("是否重新请求数据", "当前是：" + "\n1." + netGetStr + "\n2." + nonetGetStr, 0);
//不要弹窗，下面就可以直接弹出来
//        Toast.makeText(this, ""+"当前是：" + "\n1." + netGetStr + "\n2." + nonetGetStr, Toast.LENGTH_LONG).show();
//        initGet();
    }
}
