package com.example.nadim.easyapplicationsystem.Model;

public class StudentInfoModel {

    private String token, fullname, student_id, contact_no, email, department, batch, section, password;

    public StudentInfoModel(String fullname, String student_id, String contact_no, String email, String department, String batch, String section, String password) {
        this.fullname = fullname;
        this.student_id = student_id;
        this.contact_no = contact_no;
        this.email = email;
        this.department = department;
        this.batch = batch;
        this.section = section;
        this.password = password;
    }

    public String getToken() {
        return token;
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

    public String getPassword() {
        return password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
