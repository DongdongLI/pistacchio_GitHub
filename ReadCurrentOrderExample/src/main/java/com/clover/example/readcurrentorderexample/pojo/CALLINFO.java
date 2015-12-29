package com.clover.example.readcurrentorderexample.pojo;

public class CALLINFO {

    private String phone_number;
    private String restid;
    private String restname;
    private Integer retry_delay;
    private Integer wait_time;
    private Integer call_delay;
    private Integer max_retries;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRestid() {
        return restid;
    }

    public void setRestid(String restid) {
        this.restid = restid;
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public Integer getRetry_delay() {
        return retry_delay;
    }

    public void setRetry_delay(Integer retry_delay) {
        this.retry_delay = retry_delay;
    }

    public Integer getWait_time() {
        return wait_time;
    }

    public void setWait_time(Integer wait_time) {
        this.wait_time = wait_time;
    }

    public Integer getCall_delay() {
        return call_delay;
    }

    public void setCall_delay(Integer call_delay) {
        this.call_delay = call_delay;
    }

    public Integer getMax_retries() {
        return max_retries;
    }

    public void setMax_retries(Integer max_retries) {
        this.max_retries = max_retries;
    }
}