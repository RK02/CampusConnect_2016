package com.campusconnect.POJO;

/**
 * Created by sarthak on 6/14/16.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelCoursePage {

    private String description;
    private String isSubscribed;
    private String assignmentCount;
    private String testCount;
    private String courseName;
    private String semester;
    private List<String> startTime = new ArrayList<String>();
    private List<String> date = new ArrayList<String>();
    private List<String> endTime = new ArrayList<String>();
    private String response;
    private String notesCount;
    private String studentCount;
    private String professorName;
    private String isAdmin;
    private String courseCode;
    private String elective;
    private List<String> branchNames = new ArrayList<>();
    private List<String> sectionNames = new ArrayList<>();
    private List<String> batchNames = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The isSubscribed
     */
    public String getIsSubscribed() {
        return isSubscribed;
    }

    /**
     *
     * @param isSubscribed
     * The isSubscribed
     */
    public void setIsSubscribed(String isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    /**
     *
     * @return
     * The assignmentCount
     */
    public String getAssignmentCount() {
        return assignmentCount;
    }

    /**
     *
     * @param assignmentCount
     * The assignmentCount
     */
    public void setAssignmentCount(String assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    /**
     *
     * @return
     * The testCount
     */
    public String getTestCount() {
        return testCount;
    }

    /**
     *
     * @param testCount
     * The testCount
     */
    public void setTestCount(String testCount) {
        this.testCount = testCount;
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
     * The startTime
     */
    public List<String> getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     * The startTime
     */
    public void setStartTime(List<String> startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return
     * The date
     */
    public List<String> getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(List<String> date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The endTime
     */
    public List<String> getEndTime() {
        return endTime;
    }

    /**
     *
     * @param endTime
     * The endTime
     */
    public void setEndTime(List<String> endTime) {
        this.endTime = endTime;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<String> getBranchNames() {
        return branchNames;
    }

    public void setBranchNames(List<String> branchNames) {
        this.branchNames = branchNames;
    }

    public List<String> getSectionNames() {
        return sectionNames;
    }

    public void setSectionNames(List<String> sectionNames) {
        this.sectionNames = sectionNames;
    }

    public List<String> getBatchNames() {
        return batchNames;
    }

    public void setBatchNames(List<String> batchNames) {
        this.batchNames = batchNames;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getElective() {
        return elective;
    }

    public void setElective(String elective) {
        this.elective = elective;
    }
}