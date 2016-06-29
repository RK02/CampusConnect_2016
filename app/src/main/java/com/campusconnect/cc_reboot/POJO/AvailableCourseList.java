package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailableCourseList {

    private String dueExams;
    private String courseName;
    private String recentNotes;
    private String colour;
    private List<String> startTime = new ArrayList<String>();
    private List<String> date = new ArrayList<String>();
    private String dueAssignments;
    private String professorName;
    private List<String> endTime = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


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
     * The recentNotes
     */
    public String getRecentNotes() {
        return recentNotes;
    }

    /**
     *
     * @param recentNotes
     * The recentNotes
     */
    public void setRecentNotes(String recentNotes) {
        this.recentNotes = recentNotes;
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
     * The dueAssignments
     */
    public String getDueAssignments() {
        return dueAssignments;
    }

    /**
     *
     * @param dueAssignments
     * The dueAssignments
     */
    public void setDueAssignments(String dueAssignments) {
        this.dueAssignments = dueAssignments;
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

    public String getDueExams() {
        return dueExams;
    }

    public void setDueExams(String dueExams) {
        this.dueExams = dueExams;
    }


    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
