package yy.com.yjboretrofitcsdndemo.entity;

/**
 * 引用:http://blog.csdn.net/u010904027/article/details/52210487
 * 泛型 T  class
 *
 * @author yjbo
 * @time 2017/2/11 15:18
 */

public class TClass<T> {

    private T data;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
