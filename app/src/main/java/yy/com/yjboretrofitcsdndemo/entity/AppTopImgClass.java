package yy.com.yjboretrofitcsdndemo.entity;

/**
 * @description: <描述当前版本功能>
 * <p>
 * </p>
 * @author: yjbo
 * @date: 2016-07-12 15:52
 */
public class AppTopImgClass {

    public String IMG_URL;

    public String IMG_NAME;

    public String IMG_HTTP;

    public void setIMG_URL(String IMG_URL){
        this.IMG_URL = IMG_URL;
    }
    public String getIMG_URL(){
        return this.IMG_URL;
    }
    public void setIMG_NAME(String IMG_NAME){
        this.IMG_NAME = IMG_NAME;
    }
    public String getIMG_NAME(){
        return this.IMG_NAME;
    }
    public void setIMG_HTTP(String IMG_HTTP){
        this.IMG_HTTP = IMG_HTTP;
    }
    public String getIMG_HTTP(){
        return this.IMG_HTTP;
    }

    @Override
    public String toString() {
        return "AppTopImgClass{" +
                "IMG_URL='" + IMG_URL + '\'' +
                ", IMG_NAME='" + IMG_NAME + '\'' +
                ", IMG_HTTP='" + IMG_HTTP + '\'' +
                '}';
    }
}
