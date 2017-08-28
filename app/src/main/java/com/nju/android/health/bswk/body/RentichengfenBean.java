package com.nju.android.health.bswk.body;

import java.util.List;

/**
 * Created by Administrator on 2016/3/15.
 */
public class RentichengfenBean {

    /**
     * code : 200
     * message : 调用成功
     * data : {"dynamic_records":[{"type":"6","dynamic_date":"2016-03-14","dynamic_time":"17:58:00","weight":"111","fat":"111","boneMass":"111","muscle":"111","visceral_fat":"111","body_impedance":"111","quality_index":"111","moisture":"111","heat":"111","remark":"111"}]}
     */

    private int code;
    private String message;
    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * type : 6
         * dynamic_date : 2016-03-14
         * dynamic_time : 17:58:00
         * weight : 111
         * fat : 111
         * boneMass : 111
         * muscle : 111
         * visceral_fat : 111
         * body_impedance : 111
         * quality_index : 111
         * moisture : 111
         * heat : 111
         * remark : 111
         */

        private List<DynamicRecordsEntity> dynamic_records;

        public void setDynamic_records(List<DynamicRecordsEntity> dynamic_records) {
            this.dynamic_records = dynamic_records;
        }

        public List<DynamicRecordsEntity> getDynamic_records() {
            return dynamic_records;
        }

        public static class DynamicRecordsEntity {
            private String type;
            private String dynamic_date;
            private String dynamic_time;
            private String weight;
            private String fat;
            private String boneMass;
            private String muscle;
            private String visceral_fat;
            private String body_impedance;
            private String quality_index;
            private String moisture;
            private String heat;
            private String remark;

            public void setType(String type) {
                this.type = type;
            }

            public void setDynamic_date(String dynamic_date) {
                this.dynamic_date = dynamic_date;
            }

            public void setDynamic_time(String dynamic_time) {
                this.dynamic_time = dynamic_time;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public void setFat(String fat) {
                this.fat = fat;
            }

            public void setBoneMass(String boneMass) {
                this.boneMass = boneMass;
            }

            public void setMuscle(String muscle) {
                this.muscle = muscle;
            }

            public void setVisceral_fat(String visceral_fat) {
                this.visceral_fat = visceral_fat;
            }

            public void setBody_impedance(String body_impedance) {
                this.body_impedance = body_impedance;
            }

            public void setQuality_index(String quality_index) {
                this.quality_index = quality_index;
            }

            public void setMoisture(String moisture) {
                this.moisture = moisture;
            }

            public void setHeat(String heat) {
                this.heat = heat;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getType() {
                return type;
            }

            public String getDynamic_date() {
                return dynamic_date;
            }

            public String getDynamic_time() {
                return dynamic_time;
            }

            public String getWeight() {
                return weight;
            }

            public String getFat() {
                return fat;
            }

            public String getBoneMass() {
                return boneMass;
            }

            public String getMuscle() {
                return muscle;
            }

            public String getVisceral_fat() {
                return visceral_fat;
            }

            public String getBody_impedance() {
                return body_impedance;
            }

            public String getQuality_index() {
                return quality_index;
            }

            public String getMoisture() {
                return moisture;
            }

            public String getHeat() {
                return heat;
            }

            public String getRemark() {
                return remark;
            }
        }
    }
}
