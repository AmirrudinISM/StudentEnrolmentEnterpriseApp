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
public class Student {
    private String studentID;
    private String email;
    private String password;
    private String studentName;
    private int requestedCreditHours;
    private int accumulatedCreditHours;
    private boolean isRegistered;
    

    public Student() {
    }

    public Student(String email, String password, String studentName) {
        this.email = email;
        this.password = password;
        this.studentName = studentName;
    }
    
    public String getStudentID(){
        return this.studentID;
    }
    
    public void setStudentID(String studentID){
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getRequestedCreditHours() {
        return requestedCreditHours;
    }

    public void setRequestedCreditHours(int requestedCreditHours) {
        this.requestedCreditHours = requestedCreditHours;
    }

    public int getAccumulatedCreditHours() {
        return accumulatedCreditHours;
    }

    public void setAccumulatedCreditHours(int accumulatedCreditHours) {
        this.accumulatedCreditHours = accumulatedCreditHours;
    }

    public boolean isIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
    
    
    
}
