package com.example.nadim.easyapplicationsystem;

import com.example.nadim.easyapplicationsystem.Model.ApplicationSubmitionCallBack;
import com.example.nadim.easyapplicationsystem.Model.FacultyApplicationParameter;
import com.example.nadim.easyapplicationsystem.Model.FacultyInfoModel;
import com.example.nadim.easyapplicationsystem.Model.FacultyLoginCallBack;
import com.example.nadim.easyapplicationsystem.Model.FacultyUpdateCallBack;
import com.example.nadim.easyapplicationsystem.Model.HistoryCallBack;
import com.example.nadim.easyapplicationsystem.Model.FullApplication;
import com.example.nadim.easyapplicationsystem.Model.NotificationCallBack;
import com.example.nadim.easyapplicationsystem.Model.RegistrationCallBack;
import com.example.nadim.easyapplicationsystem.Model.StudentApplicationParameter;
import com.example.nadim.easyapplicationsystem.Model.StudentInfoModel;
import com.example.nadim.easyapplicationsystem.Model.StudentLoginCallBack;
import com.example.nadim.easyapplicationsystem.Model.StudentUpdateCallBack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface API {

    @POST("auth/student/login")
    Call<StudentLoginCallBack> login(@Body UserLogin login);

    @POST("auth/faculty/login")
    Call<FacultyLoginCallBack> loginFaculty(@Body UserLogin login);

    @POST("auth/student/register")
    Call<RegistrationCallBack> studentRegister(@Body StudentInfoModel studentInfoModel);

    @POST("auth/faculty/register")
    Call<RegistrationCallBack> facultyRegister(@Body FacultyInfoModel facultyInfoModel);

    @FormUrlEncoded
    @POST("auth/student/update")
    Call<StudentUpdateCallBack> studentUpdate(@Field("student_id") String id, @Field("fullname") String name,
                                              @Field("section") String section, @Field("contact_no") int contact,
                                              @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/faculty/update")
    Call<FacultyUpdateCallBack> facultyUpdate(@Field("faculty_id") String id, @Field("fullname") String name,
                                              @Field("position") String position, @Field("contact_no") int contact,
                                              @Field("password") String password);

   /* @GET("api/v1/types")
    Call<TypesData> getTypes(@Header("Authorization") String token);*/

    @FormUrlEncoded
    @POST("api/v1/types")
    Call<TypesData> getTypes(@Field("user_type") String user_type);


    @POST("api/v1/application/student/create")
    Call<ApplicationSubmitionCallBack> submitApplication(@Header("Authorization") String token,
                                                         @Body StudentApplicationParameter studentApplicationParameter);

    @POST("api/v1/application/faculty/create")
    Call<ApplicationSubmitionCallBack> submitApplicationFaculty(@Header("Authorization") String token,
                                                                @Body FacultyApplicationParameter facultyApplicationParameter);


    @FormUrlEncoded
    @POST("api/v1/application/student/single")
    Call<NotificationCallBack> getStudentNotification(@Field("appl_id") String application_id);

    @FormUrlEncoded
    @POST("api/v1/application/faculty/single")
    Call<NotificationCallBack> getFacultyNotification(@Field("appl_id") String application_id);

    @FormUrlEncoded
    @POST("api/v1/application/student/history")
    Call<HistoryCallBack> getStudentHistory(@Field("student_id") String student_id);

    @FormUrlEncoded
    @POST("api/v1/application/faculty/history")
    Call<HistoryCallBack> getFacultyHistory(@Field("faculty_id") String faculty_id);

}
