package com.example.weather_forecast.Beans;

import com.google.gson.annotations.SerializedName;

/**
 *      "aqi": {
 *         "city": {
 *           "aqi": "34",
 *           "qlty": "优",
 *           "pm25": "22",
 *           "pm10": "34",
 *           "no2": "19",
 *           "so2": "6",
 *           "co": "0.9",
 *           "o3": "55"
 *         }
 *     }
 */
public class AQI {
    private City city;
    public static class City{
        private String aqi;
        @SerializedName("qlty")
        private String quality;
        private String pm25;
        private String pm10;
        private String no2;
        private String so2;
        private String co;
        private String o3;

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getSo2() {
            return so2;
        }

        public void setSo2(String so2) {
            this.so2 = so2;
        }

        public String getCo() {
            return co;
        }

        public void setCo(String co) {
            this.co = co;
        }

        public String getO3() {
            return o3;
        }

        public void setO3(String o3) {
            this.o3 = o3;
        }
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}

