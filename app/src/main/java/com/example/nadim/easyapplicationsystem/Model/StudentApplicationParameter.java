package com.example.nadim.easyapplicationsystem.Model;

public class StudentApplicationParameter {

    private String application_type;
    private String body_reason;
    private String body_request;
    private String requestBy;

    public StudentApplicationParameter(String application_type, String body_reason, String body_request, String requestBy) {
        this.application_type = application_type;
        this.body_reason = body_reason;
        this.body_request = body_request;
        this.requestBy = requestBy;
    }

    public String getApplication_type() {
        return application_type;
    }

    public String getBody_reason() {
        return body_reason;
    }

    public String getBody_request() {
        return body_request;
    }

    public String getRequestBy() {
        return requestBy;
    }
}
