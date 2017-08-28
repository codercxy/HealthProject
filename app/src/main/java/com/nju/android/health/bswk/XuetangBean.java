package com.nju.android.health.bswk;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class XuetangBean {


    /**
     * code : 200
     * message : 调用成功
     * data : {"dynamic_records":[{"type":"9","dynamic_date":"2016-03-12","dynamic_time":"21:31:00","time_interval":"餐前","numerical_value":"111","remark":"无"},{"type":"9","dynamic_date":"2016-03-14","dynamic_time":"15:58:00","time_interval":"早餐后2小时","numerical_value":"45678","remark":"无"},{"type":"9","dynamic_date":"2016-03-14","dynamic_time":"16:04:00","time_interval":"餐前","numerical_value":"123","remark":"无"},{"type":"9","dynamic_date":"2016-03-14","dynamic_time":"19:21:00","time_interval":"餐前","numerical_value":"1111","remark":"添加血糖记录备注测试"},{"type":"9","dynamic_date":"2016-03-16","dynamic_time":"11:49:00","time_interval":"餐前","numerical_value":"50","remark":"无"},{"type":"9","dynamic_date":"2016-03-16","dynamic_time":"11:50:00","time_interval":"餐前","numerical_value":"7.9","remark":"无"}]}
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
         * type : 9
         * dynamic_date : 2016-03-12
         * dynamic_time : 21:31:00
         * time_interval : 餐前
         * numerical_value : 111
         * remark : 无
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
            private String time_interval;
            private String numerical_value;
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

            public void setTime_interval(String time_interval) {
                this.time_interval = time_interval;
            }

            public void setNumerical_value(String numerical_value) {
                this.numerical_value = numerical_value;
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

            public String getTime_interval() {
                return time_interval;
            }

            public String getNumerical_value() {
                return numerical_value;
            }

            public String getRemark() {
                return remark;
            }
        }
    }
}
