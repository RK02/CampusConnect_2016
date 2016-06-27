package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/21/16.
 */

        import java.util.ArrayList;
        import java.util.List;

public class CollegeList {

    private List<String> branchNames = new ArrayList<String>();
    private String collegeId;
    private String collegeName;

    /**
     *
     * @return
     * The branchNames
     */
    public List<String> getBranchNames() {
        return branchNames;
    }

    /**
     *
     * @param branchNames
     * The branchNames
     */
    public void setBranchNames(List<String> branchNames) {
        this.branchNames = branchNames;
    }

    /**
     *
     * @return
     * The collegeId
     */
    public String getCollegeId() {
        return collegeId;
    }

    /**
     *
     * @param collegeId
     * The collegeId
     */
    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    /**
     *
     * @return
     * The collegeName
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     *
     * @param collegeName
     * The collegeName
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

}