package com.campusconnect.cc_reboot.POJO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

}
