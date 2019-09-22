package com.example.weather_forecast.Beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 *   {
 *      "HeWeather5":[
 *           {
 *              "aqi:":{},
 *              "basic":{},
 *              "daily_forecast":{},
 *              "now":{}
 *              "status":"ok"
 *              "suggestion":{}
 *          }
 *      ]
 *   }
 */
public class HeWeather{
    private List<WeatherBean> HeWeather5;
    public class WeatherBean {
        private AQI aqi;
        private Basic basic;
        @SerializedName("daily_forecast")
        private List<Forecast> weatherList;
        private Now now;
        private String status;
        private Suggestion suggestion;

        public AQI getAqi() {
            return aqi;
        }

        public void setAqi(AQI aqi) {
            this.aqi = aqi;
        }

        public Basic getBasic() {
            return basic;
        }

        public void setBasic(Basic basic) {
            this.basic = basic;
        }

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Forecast> getWeatherList() {
            return weatherList;
        }

        public void setWeatherList(List<Forecast> weatherList) {
            this.weatherList = weatherList;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(Suggestion suggestion) {
            this.suggestion = suggestion;
        }

    }

    public List<WeatherBean> getHeWeather5() {
        return HeWeather5;
    }

    public void setHeWeather5(List<WeatherBean> heWeather5) {
        HeWeather5 = heWeather5;
    }
}
