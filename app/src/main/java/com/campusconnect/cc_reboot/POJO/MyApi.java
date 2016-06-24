package com.campusconnect.cc_reboot.POJO;

import android.util.Log;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by sarthak on 6/14/16.
 */
public interface MyApi {

    String BASE_URL = "https://uploadnotes-2016.appspot.com/_ah/api/notesapi/v1/";

    @GET("feed/ahJzfnVwbG9hZG5vdGVzLTIwMTZyFAsSB1Byb2ZpbGUYgICAgLyhggoM/")
    Call<Example> getFeed(@Query("profileId") String profileId);

    @POST("coursePage")
    Call<ModelCoursePage> getCourse(@Body getCourseRequest body);
    class getCourseRequest{
        private String profileId;
        private String courseId;

        public getCourseRequest(String profileId,String courseId)
        {
            this.profileId = profileId;
            this.courseId = courseId;
        }

    }

    @POST("notebookList")
    Call<ModelNoteBookList> getNoteBookList(@Body getNoteBookListRequest body);
    class getNoteBookListRequest{
        private String courseId;
        public getNoteBookListRequest(String courseId)
        {

            this.courseId = courseId;
        }
    }

    @POST("getNoteBook")
    Call<ModelNoteBook> getNoteBook(@Body getNoteBookRequest body);
        class getNoteBookRequest{
            private String noteBookId;
            private String profileId;
            public getNoteBookRequest(String noteBookId,String profileId)
            {
                this.profileId = profileId;
                this.noteBookId = noteBookId;
            }
        }

    @POST("assignmentList")
    Call<ModelAssignmentList> getAssignmentList(@Body getAssignmentListRequest body);
        class getAssignmentListRequest{
            private String courseId;
            public getAssignmentListRequest(String courseId){
                this.courseId = courseId;
            }
        }
    @POST("getAssignment")
    Call<ModelAssignment> getAssignment(@Body getAssignmentRequest body);
        class getAssignmentRequest{
            private String assignmentId;
            private String profileId;
            public getAssignmentRequest(String assignmentId, String profileId){
                this.assignmentId = assignmentId;
                this.profileId = profileId;
            }
        }
    @POST("testList")
    Call<ModelTestList> getTestList(@Body getTestListRequest body);
        class getTestListRequest{
            private String courseId;
            public getTestListRequest(String courseId){
                this.courseId = courseId;
            }
        }

    @POST("getTest")
    Call<ModelTest> getTest(@Body getTestRequest body);
    class getTestRequest{
        private String testId;
        private String profileId;
        public getTestRequest(String testId, String profileId){
            this.testId = testId;
            this.profileId = profileId;
        }
    }

    @GET("collegeList")
    Call<ModelCollegeList> getCollegeList();

    @POST("createProfile")
    Call<ModelSignUp> getProfileId(@Body getProfileIdRequest body);
    class getProfileIdRequest{
        private String profileName;
        private String collegeId;
        private String batchName;
        private String branchName;
        private String sectionName;
        private String photoUrl;
        private String email;
        public getProfileIdRequest(String profileName,String collegeId,String batchName, String branchName, String sectionName,String photoUrl,String email)
        {
            this.profileName = profileName;
            this.collegeId = collegeId;
            if(batchName==null) this.batchName = "";
            else this.batchName = batchName;
            if(branchName==null) this.branchName ="";
            else this.branchName = branchName;
            if(sectionName==null) this.sectionName="";
            else this.sectionName = sectionName;
            this.photoUrl = photoUrl;
            this.email = email;
        }
    }

    @POST("courseList/1")
    Call<ModelCourseSubscribe> getCourses(@Body getCoursesRequest body);
    class getCoursesRequest{
        private String pageNumber="1";
        private String profileId;
        public getCoursesRequest(String profileId)
        {
            this.profileId = profileId;
        }
    }

    @POST("subscribeCourse")
    Call<ModelSubscribe> subscribeCourse(@Body subscribeCourseRequest body);
    class subscribeCourseRequest{
        private String profileId;
        private String[] courseIds;
        public subscribeCourseRequest(String profileId, String[] courseIds){
            this.profileId = profileId;
            this.courseIds = courseIds;
        }
    }

    @POST("addCourse")
    Call<ModelAddCourse> addCourse(@Body addCourseRequest body);
    class addCourseRequest{
        private String profileId;
        private String collegeId;
        private String[] batchNames;
        private String[] branchNames;
        private String semester;
        private String[] date;
        private String startTime;
        private String endTime;
        private String professorName;
        private String colour;
        private String courseCode;
        public addCourseRequest(String semester, String[] date, String startTime, String endTime, String professorName, String colour, String courseCode){
            this.profileId = FragmentCourses.profileId;
            this.collegeId = FragmentCourses.collegeId;
           // this.batchNames = FragmentCourses.batchName;
           // this.branchNames = FragmentCourses.branchname;
            this.semester = "odd";
            //this.


        }


    }

    /*
    courseName
collegeId
batchNames
sectionNames
branchNames
semester
date
startTime
endTime
professorName
colour
courseCode
profileId
     */



//upload issues  with retrofit, switching to asynctask
    //switched to okhttp



}
