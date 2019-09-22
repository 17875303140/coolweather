package com.example.weather_forecast.Beans;

import com.google.gson.annotations.SerializedName;

/**
 *      "now": {
 *          "cond": {
 *               "code": "101",
 *               "txt": "多云"
 *           }
 *          "hum": "94",
 *          "fl": "39",
 *          "vis": "16",
 *          "tmp": "25"
 *      }
 */
public class Now {
    @SerializedName("cond")
    private Weather weather;
    //相对湿度
    private String hum;
    //温度
    private String tmp;
    //能见度
    @SerializedName("vis")
    private String visiable;
    //体感温度
    @SerializedName("fl")
    private String temperature;

    public class Weather {
        public String getWeatherId() {
            return weatherId;
        }

        public void setWeatherId(String weatherId) {
            this.weatherId = weatherId;
        }

        public String getWeatherMsg() {
            return weatherMsg;
        }

        public void setWeatherMsg(String weatherMsg) {
            this.weatherMsg = weatherMsg;
        }

        @SerializedName("code")
        private String weatherId;
        @SerializedName("txt")
        private String weatherMsg;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getVisiable() {
        return visiable;
    }

    public void setVisiable(String visiable) {
        this.visiable = visiable;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}
