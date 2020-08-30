package com.example.nadim.easyapplicationsystem.Model;

public class FacultyApplicationParameter {

    private String application_type;
    private String body_reason;
    private String leave_start;
    private String leave_end;
    private String emg_address;
    private String emg_contact_no;
    private String requestBy;

    public FacultyApplicationParameter(String application_type, String body_reason, String leave_start, String leave_end, String emg_address, String emg_contact_no, String requestBy) {
        this.application_type = application_type;
        this.body_reason = body_reason;
        this.leave_start = leave_start;
        this.leave_end = leave_end;
        this.emg_address = emg_address;
        this.emg_contact_no = emg_contact_no;
        this.requestBy = requestBy;
    }
}
