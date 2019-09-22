package com.example.weather_forecast.Beans;


/**
 * "suggestion": {
 *         "air": {
 *           "brf": "中",
 *           "txt": "气象条件对空气污染物稀释、扩散和清除无明显影响。"
 *         },
 *         "comf": {
 *           "brf": "较不舒适",
 *           "txt": "白天天气多云，并且空气湿度偏大，在这种天气条件下，您会感到有些闷热，不很舒适。"
 *         },
 *         "cw": {
 *           "brf": "适宜",
 *           "txt": "适宜洗车，未来持续两天无雨天气较好，适合擦洗汽车，蓝天白云、风和日丽将伴您的车子连日洁净。"
 *         },
 *         "drsg": {
 *           "brf": "炎热",
 *           "txt": "天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"
 *         },
 *         "flu": {
 *           "brf": "少发",
 *           "txt": "各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"
 *         },
 *         "sport": {
 *           "brf": "较不宜",
 *           "txt": "天气较好，无雨水困扰，但考虑气温很高，请注意适当减少运动时间并降低运动强度，运动后及时补充水分。"
 *         },
 *         "trav": {
 *           "brf": "一般",
 *           "txt": "天气较好，同时有微风相伴，但温度较高，天气热，请尽量避免高温时段外出，若外出请注意防暑降温和防晒。"
 *         }
 *       }
 */
public class Suggestion {
    private Air air;
    private Comfortable comf;
    private CarWash cw;
    private DressSuggestion drsg;
    private Flu flu;
    private Sport sport;
    private Travel trav;

    public Air getAir() {
        return air;
    }

    public void setAir(Air air) {
        this.air = air;
    }

    public Comfortable getComf() {
        return comf;
    }

    public void setComf(Comfortable comf) {
        this.comf = comf;
    }

    public CarWash getCw() {
        return cw;
    }

    public void setCw(CarWash cw) {
        this.cw = cw;
    }

    public DressSuggestion getDrsg() {
        return drsg;
    }

    public void setDrsg(DressSuggestion drsg) {
        this.drsg = drsg;
    }

    public Flu getFlu() {
        return flu;
    }

    public void setFlu(Flu flu) {
        this.flu = flu;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Travel getTrav() {
        return trav;
    }

    public void setTrav(Travel trav) {
        this.trav = trav;
    }

    public class Air{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        //生活指数简介
        private String brf;
        //生活指数详细情况
        private String txt;
    }
    public class Comfortable{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
    public class Sport{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
    public class Travel{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
    public class Flu{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
    public class CarWash{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
    public class DressSuggestion{
        public String getBrf() {
            return brf;
        }

        public void setBrf(String brf) {
            this.brf = brf;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        private String brf;
        private String txt;
    }
}
