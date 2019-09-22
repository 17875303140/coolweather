package com.example.weather_forecast.Beans;

import com.google.gson.annotations.SerializedName;

/**
 *      "basic": {
 *          "city":"梅州"
 *          "update": {
 *            "loc": "2019-08-22 23:15",
 *          }
 *      }
 */

public class Basic {
    private Update update;
    private String city;

    public class Update {
        @SerializedName("loc")
        private String updateTime;

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
