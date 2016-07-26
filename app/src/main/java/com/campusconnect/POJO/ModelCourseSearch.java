package com.campusconnect.POJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarthak on 7/4/16.
 */
public class ModelCourseSearch {

    private List<CourseList> courseList = new ArrayList<CourseList>();

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

}
