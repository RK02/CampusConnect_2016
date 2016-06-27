package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/21/16.
 */

        import java.util.ArrayList;
        import java.util.List;

public class ModelCollegeList {

    private List<CollegeList> collegeList = new ArrayList<CollegeList>();

    /**
     *
     * @return
     * The collegeList
     */
    public List<CollegeList> getCollegeList() {
        return collegeList;
    }

    /**
     *
     * @param collegeList
     * The collegeList
     */
    public void setCollegeList(List<CollegeList> collegeList) {
        this.collegeList = collegeList;
    }

}
