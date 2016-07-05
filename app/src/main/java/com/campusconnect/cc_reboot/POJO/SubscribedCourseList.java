package com.campusconnect.cc_reboot.POJO;

/**
 * Created by sarthak on 6/14/16.
 */
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedCourseList extends SugarRecord {

    private String dueExams;
    private String courseName;
    private String recentNotes;

    @Unique
    private String courseId;
    private String colour;

    @Ignore
    private List<String> startTime = new ArrayList<String>();
    @Ignore
    private List<String> date = new ArrayList<String>();
    private String dueAssignments;
    private  String professorName;
    @Ignore
    private List<String> endTime = new ArrayList<String>();
    @Ignore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();



    /**
     *
     * @return
     * The dueTests
     */

    public SubscribedCourseList(){}
    /**
     *
     * @return
     * The courseName
     */
    public SubscribedCourseList(SubscribedCourseList copy) {
        this.dueExams = copy.dueExams;
        this.courseName = copy.courseName;
        this.recentNotes = copy.recentNotes;
        this.courseId = copy.courseId;
        this.colour = copy.colour;
        this.startTime = copy.startTime;
        this.date = copy.date;
        this.dueAssignments = copy.dueAssignments;
        this.professorName = copy.professorName;
        this.endTime = copy.endTime;
    }
    public String getCourseName() {
        return courseName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscribedCourseList)) return false;

        SubscribedCourseList that = (SubscribedCourseList) o;

        if (!dueExams.equals(that.dueExams)) return false;
        if (!courseName.equals(that.courseName)) return false;
        if (!recentNotes.equals(that.recentNotes)) return false;
        if (!courseId.equals(that.courseId)) return false;
        if (!colour.equals(that.colour)) return false;
        if (!startTime.equals(that.startTime)) return false;
        if (!date.equals(that.date)) return false;
        if (!dueAssignments.equals(that.dueAssignments)) return false;
        if (!professorName.equals(that.professorName)) return false;
        if (!endTime.equals(that.endTime)) return false;
        return additionalProperties != null ? additionalProperties.equals(that.additionalProperties) : that.additionalProperties == null;
    }

    @Override
    public int hashCode() {
        int result = dueExams.hashCode();
        result = 31 * result + courseName.hashCode();
        result = 31 * result + recentNotes.hashCode();
        result = 31 * result + courseId.hashCode();
        result = 31 * result + colour.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + dueAssignments.hashCode();
        result = 31 * result + professorName.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + (additionalProperties != null ? additionalProperties.hashCode() : 0);
        return result;
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

    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId)
    {
        this.courseId = courseId;
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
