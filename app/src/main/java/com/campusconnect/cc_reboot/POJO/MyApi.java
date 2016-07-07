package com.campusconnect.cc_reboot.POJO;

import android.util.Log;

import com.campusconnect.cc_reboot.fragment.Home.FragmentCourses;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @GET("branchList")
    Call<ModelBranchList> getBranches(@Query("collegeId") String collegeId);

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
    @POST("notebookList")
    Call<ModelNoteBookList> getBookmarked(@Body getBookmarkedRequest body);
    class getBookmarkedRequest{
        private String bpid;
        public getBookmarkedRequest(String profileId)
        {
            this.bpid = profileId;
        }
    }
    @POST("notebookList")
    Call<ModelNoteBookList> getUploaded(@Body getUploadedRequest body);
    class getUploadedRequest{
        private String upid;
        public getUploadedRequest(String profileId)
        {

            this.upid = profileId;
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
    @POST("examList")
    Call<ModelTestList> getTestList(@Body getTestListRequest body);
        class getTestListRequest{
            private String courseId;
            public getTestListRequest(String courseId){
                this.courseId = courseId;
            }
        }

    @POST("getExam")
    Call<ModelTest> getTest(@Body getTestRequest body);
    class getTestRequest{
        private String examId;
        private String profileId;
        public getTestRequest(String testId, String profileId){
            this.examId = testId;
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
        private String gcmId;
        public getProfileIdRequest(String profileName,String collegeId,String batchName, String branchName, String sectionName,String photoUrl,String email,String gcmId)
        {
            this.profileName = profileName;
            this.collegeId = collegeId;
            this.gcmId = gcmId;
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

            private String collegeId;
            private String courseName;
            private List<String> batchNames;
            private List<String> sectionNames;
            private String profileId;
            private String semester;
            private List<String> date;
            private List<String> startTime;
            private List<String> endTime;
            private String professorName;
            private String colour;
            private String courseCode;
            private List<String> branchNames;
        private String elective;

            public addCourseRequest(String profileId,
                                    String collegeId,
                                    String courseName,
                                    String courseCode,
                                    String professorName,
                                    String semester,
                                    List<String> batchNames,
                                    List<String> sectionNames,
                                    List<String> branchNames,
                                    List<String> date,
                                    List<String> startTime,
                                    List<String> endTime,
                                    String colour,
                                    String elective
                                    ){
                this.profileId = profileId;
                this.collegeId = collegeId;
                this.semester = semester;
                this.batchNames = batchNames;
                this.branchNames = branchNames;
                this.sectionNames = sectionNames;
                this.courseCode = courseCode;
                this.startTime = startTime;
                this.endTime = endTime;
                this.courseName = courseName;
                this.professorName = professorName;
                this.date = date;
                this.colour = colour;
                this.elective = elective;
        }


    }


//upload issues  with retrofit, switching to asynctask
    //switched to okhttp


    @POST("searchNotes")
    Call<ModelNotesSearch> searchNotes(@Body notesSearch notesSearch);
    class notesSearch{
        private String searchString;
        public notesSearch(String searchString)
        {
            this.searchString = searchString;
        }
    }
    @POST("searchCourse")
    Call<ModelCourseSearch> searchCourse(@Body courseSearch courseSearch);
    class courseSearch{
        private String searchString;
        public courseSearch(String searchString)
        {
            this.searchString = searchString;
        }
    }


}
