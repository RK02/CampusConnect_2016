package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/21/16.
 */
import java.util.ArrayList;
import java.util.List;

public class CourseList {

    private List<String> batchNames = new ArrayList<String>();
    private List<String> branchNames = new ArrayList<String>();
    private String courseId;
    private String courseName;
    private String notesCount;
    private String professorName;
    private List<String> sectionNames = new ArrayList<String>();
    private String semester;
    private String studentCount;
    private String colour;

    /**
     *
     * @return
     * The batchNames
     */
    public List<String> getBatchNames() {
        return batchNames;
    }

    /**
     *
     * @param batchNames
     * The batchNames
     */
    public void setBatchNames(List<String> batchNames) {
        this.batchNames = batchNames;
    }

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
     * The courseId
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     *
     * @param courseId
     * The courseId
     */
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    /**
     *
     * @return
     * The courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     *
     * @param courseName
     * The courseName
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     *
     * @return
     * The notesCount
     */
    public String getNotesCount() {
        return notesCount;
    }

    /**
     *
     * @param notesCount
     * The notesCount
     */
    public void setNotesCount(String notesCount) {
        this.notesCount = notesCount;
    }

    /**
     *
     * @return
     * The professorName
     */
    public String getProfessorName() {
        return professorName;
    }

    /**
     *
     * @param professorName
     * The professorName
     */
    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    /**
     *
     * @return
     * The sectionNames
     */
    public List<String> getSectionNames() {
        return sectionNames;
    }

    /**
     *
     * @param sectionNames
     * The sectionNames
     */
    public void setSectionNames(List<String> sectionNames) {
        this.sectionNames = sectionNames;
    }

    /**
     *
     * @return
     * The studentCount
     */
    public String getStudentCount() {
        return studentCount;
    }

    /**
     *
     * @param studentCount
     * The studentCount
     */
    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}