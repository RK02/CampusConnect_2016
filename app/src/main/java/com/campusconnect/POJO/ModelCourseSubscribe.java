package com.campusconnect.POJO;

/**
 * Created by sarthak on 6/21/16.
 */
import java.util.ArrayList;
import java.util.List;

public class ModelCourseSubscribe {

    private String completed;
    private List<CourseList> courseList = new ArrayList<CourseList>();
    private String description;
    private String response;

    /**
     *
     * @return
     * The completed
     */
    public String getCompleted() {
        return completed;
    }

    /**
     *
     * @param completed
     * The completed
     */
    public void setCompleted(String completed) {
        this.completed = completed;
    }

    /**
     *
     * @return
     * The courseList
     */
    public List<CourseList> getCourseList() {
        return courseList;
    }

    /**
     *
     * @param courseList
     * The courseList
     */
    public void setCourseList(List<CourseList> courseList) {
        this.courseList = courseList;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The response
     */
    public String getResponse() {
        return response;
    }

    /**
     *
     * @param response
     * The response
     */
    public void setResponse(String response) {
        this.response = response;
    }

}
