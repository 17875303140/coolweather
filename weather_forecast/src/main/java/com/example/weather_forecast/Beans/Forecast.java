package com.example.weather_forecast.Beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  "daily_forecast": [
 *         {
 *           "cond": {
 *             "code_d": "101",
 *             "txt_d": "多云"
 *           },
 *           "date": "2019-08-22",
 *           "pop": "56",
 *           "tmp": {
 *             "max": "35",
 *             "min": "25"
 *           }
 *         },
 *         ...
 *   ]
 */
public class Forecast {
        @SerializedName("cond")
        private Weather weather;
        @SerializedName("pop")
        private int rain_possibility;
        private String date;
        @SerializedName("tmp")
        private Temperature temperature;

        public class Weather{
            @SerializedName("code_d")
            private String weatherId;
            @SerializedName("txt_d")
            private String weatherMsg;

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
        }
        public class Temperature{
            private String max;
            private String min;

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }
        }

        public int getRain_possibility() {
             return rain_possibility;
         }
        public void setRain_possibility(int rain_possibility) {
              this.rain_possibility = rain_possibility;
          }

        public Weather getWeather() {
            return weather;
        }

        public void setWeather(Weather weather) {
            this.weather = weather;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Temperature getTemperature() {
            return temperature;
        }

        public void setTemperature(Temperature temperature) {
            this.temperature = temperature;
        }
}
