package yy.com.yjboretrofitcsdndemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.DialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import yy.com.yjboretrofitcsdndemo.entity.NetWorkClass;
import yy.com.yjboretrofitcsdndemo.interf.HttpService;
import yy.com.yjboretrofitcsdndemo.util.CommonUtil;
import yy.com.yjboretrofitcsdndemo.util.RetrofitNetUtil;

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
    @Bind(R.id.show_kind_ask)
    TextView showKindAsk;


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
        RetrofitNetUtil netUtil = new RetrofitNetUtil();

        netUtil.setOnrequestistener(new RetrofitNetUtil.OnrequestListener() {

            @Override
            public void onService(HttpService service) {
                Call<NetWorkClass> call = service.getFirstBlog();
                call.enqueue(new Callback<NetWorkClass>() {
                    @Override
                    public void onResponse(Call<NetWorkClass> call, retrofit2.Response<NetWorkClass> response) {
                        if (response.isSuccessful()) {
                            CommonUtil.toast(MainActivity.this, "数据请求成功", Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                            NetWorkClass netWorkClass = response.body();
                            showResult.setText("时间：" +System.currentTimeMillis()
                                    + "\n" + CommonUtil.getTipStr(netGet,nonetGet)+"\n"
                                    +netWorkClass.toString());

                            showKindAsk.setText(CommonUtil.getTipStr(netGet,nonetGet));
                        } else {
                            showResult.setText(response.code() + "==onResponse--数据请求失败--");
                        }
                    }


                    @Override
                    public void onFailure(Call<NetWorkClass> call, Throwable t) {
                        showResult.setText( "===onFailure--数据请求失败--");
                    }
                });
            }
        });

        netUtil.requestData(MainActivity.this, HttpService.baseHttp, "", netGet, nonetGet);
    }


    /**
     * 弹出框
     *
     * @param title 标题
     * @param msg   提示内容
     * @param type
     */
    public void showDialogFragment(String title, String msg, final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle(title); //设置标题
        builder.setMessage(msg); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
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

        showDialogFragment("是否重新请求数据",CommonUtil.getTipStr(netGet,nonetGet),0);
    }

}
