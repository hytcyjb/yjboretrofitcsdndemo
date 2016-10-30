package yy.com.yjboretrofitcsdndemo.entity;

import java.util.List;

/**
 * @description: <描述当前版本功能>
 * <p>
 * </p>
 * @author: yjbo
 * @date: 2016-07-12 15:43
 */
public class NetWorkClass {

    public String error;

    public String message;

    public String charge_time;

    public String charge_moeny;

    public String charge_else_moeny;

    public String contract_moeny;

    public String ad_time;

    public String ad_on;

    public String ad_url;

    public String theme_url;

    public List<AppTopImgClass> app_top_img;

    public String session;

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCharge_time(String charge_time) {
        this.charge_time = charge_time;
    }

    public String getCharge_time() {
        return this.charge_time;
    }

    public void setCharge_moeny(String charge_moeny) {
        this.charge_moeny = charge_moeny;
    }

    public String getCharge_moeny() {
        return this.charge_moeny;
    }

    public void setCharge_else_moeny(String charge_else_moeny) {
        this.charge_else_moeny = charge_else_moeny;
    }

    public String getCharge_else_moeny() {
        return this.charge_else_moeny;
    }

    public void setContract_moeny(String contract_moeny) {
        this.contract_moeny = contract_moeny;
    }

    public String getContract_moeny() {
        return this.contract_moeny;
    }

    public void setAd_time(String ad_time) {
        this.ad_time = ad_time;
    }

    public String getAd_time() {
        return this.ad_time;
    }

    public void setAd_on(String ad_on) {
        this.ad_on = ad_on;
    }

    public String getAd_on() {
        return this.ad_on;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getAd_url() {
        return this.ad_url;
    }

    public void setTheme_url(String theme_url) {
        this.theme_url = theme_url;
    }

    public String getTheme_url() {
        return this.theme_url;
    }

    public void setApp_top_img(List<AppTopImgClass> app_top_img) {
        this.app_top_img = app_top_img;
    }

    public List<AppTopImgClass> getApp_top_img() {
        return this.app_top_img;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSession() {
        return this.session;
    }

    @Override
    public String toString() {
        String imgUrlAllStr = "";
        for (int i = 0; i < getApp_top_img().size(); i++) {
            AppTopImgClass appTopImgClass = (AppTopImgClass) getApp_top_img().get(i);
            imgUrlAllStr += appTopImgClass.toString();
        }
        return "NetWorkClass{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", charge_time='" + charge_time + '\'' +
                ", charge_moeny='" + charge_moeny + '\'' +
                ", charge_else_moeny='" + charge_else_moeny + '\'' +
                ", contract_moeny='" + contract_moeny + '\'' +
                ", ad_time='" + ad_time + '\'' +
                ", ad_on='" + ad_on + '\'' +
                ", ad_url='" + ad_url + '\'' +
                ", theme_url='" + theme_url + '\'' +
                ", app_top_img=" + imgUrlAllStr +
                ", session='" + session + '\'' +
                '}';
    }
}
