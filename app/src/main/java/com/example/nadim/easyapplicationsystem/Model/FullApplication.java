package com.example.nadim.easyapplicationsystem.Model;

import com.example.nadim.easyapplicationsystem.Apply_Page;
import com.google.gson.annotations.SerializedName;

public class FullApplication {

    @SerializedName("application_type")
    private Application application;
    private String body_reason;
    private String body_request;
    private StudentInfoModel studentInfoModel;
    private int status;

    public Application getApplication() {
        return application;
    }

    public String getBody_reason() {
        return body_reason;
    }

    public String getBody_request() {
        return body_request;
    }

    public StudentInfoModel getStudentInfoModel() {
        return studentInfoModel;
    }

    public int getStatus() {
        return status;
    }
}
