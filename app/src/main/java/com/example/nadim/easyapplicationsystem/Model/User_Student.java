package com.example.nadim.easyapplicationsystem.Model;

import com.google.gson.annotations.SerializedName;

public class User_Student {

    private String _id;
    private String fullname;
    private String student_id;
    private String contact_no;
    private String email;
    private String department;
    private String batch;
    private String section;

    public String get_id() {
        return _id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getStudent_id() {
        return student_id;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getBatch() {
        return batch;
    }

    public String getSection() {
        return section;
    }
}
