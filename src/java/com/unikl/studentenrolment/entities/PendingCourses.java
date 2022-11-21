/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unikl.studentenrolment.entities;

/**
 *
 * @author Amirrudin
 */
public class PendingCourses {
    private String enrolmentID;
    private String studentName;
    private String courseID;
    private String courseTitle;
    private String status;

    public PendingCourses() {
    }

    public PendingCourses(String enrolmentID, String studentName, String courseID, String courseTitle, String status) {
        this.enrolmentID = enrolmentID;
        this.studentName = studentName;
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.status = status;
    }
    
    
    
    public String getEnrolmentID() {
        return enrolmentID;
    }

    public void setEnrolmentID(String enrolmentID) {
        this.enrolmentID = enrolmentID;
    }
    
    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
