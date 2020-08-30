package com.example.nadim.easyapplicationsystem.Model;

public class FacultyInfoModel {

    private String fullname,
            faculty_id,
            contact_no,
            department,
            password,
            email,
            position;

    public FacultyInfoModel(String fullname, String faculty_id, String contact_no, String department, String password, String email, String position) {
        this.fullname = fullname;
        this.faculty_id = faculty_id;
        this.contact_no = contact_no;
        this.department = department;
        this.password = password;
        this.email = email;
        this.position = position;
    }
}
