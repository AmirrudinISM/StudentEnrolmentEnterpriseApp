/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unikl.studentenrolment.web;

import com.unikl.studentenrolment.entities.Course;
import com.unikl.studentenrolment.entities.Student;
import com.unikl.studentenrolment.entities.Enrolment;
import com.unikl.studentenrolment.entities.PendingCourses;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Amirrudin
 */
public class DBController {
    // Use a prepared statement to store a student into the database
    private PreparedStatement pstmt;
    private Connection conn;

    public DBController() {
      initializeJdbc();
    }

    /** Initialize database connection */
    private void initializeJdbc() {
        try {
            // Declare driver and connection string
            String driver = "org.apache.derby.jdbc.ClientDriver";
            String connectionString = "jdbc:derby://localhost:1527/StudentEnrolment";

            // Load the Oracle JDBC Thin driver
            Class.forName(driver);
            System.out.println("Driver " + driver + " loaded");

            // Connect to the sample database
            conn = DriverManager.getConnection(connectionString, "root", "nbuser");
            System.out.println("Database " + connectionString + " connected");
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    //Registers new student
    //arg: Student object
    //no returns
    public void insertStudent(Student student) throws SQLException {
        // Create a Statement
        pstmt = conn.prepareStatement("INSERT INTO Student ("
                + "STUDENTID, EMAIL, PASSWORD, STUDENTNAME) "
                + "VALUES (?, ?, ?, ?)");
        pstmt.setString(1, IDGenerator.generateStudentID(10));
        pstmt.setString(2, student.getEmail());
        pstmt.setString(3, student.getPassword());
        pstmt.setString(4, student.getStudentName());
        pstmt.executeUpdate();
    }
    
    //Checks for existing user. 
    //Used for checking if a new user wants to register using an already registered email.
    //args: email string
    //returns boolean
    public boolean userExist(String inEmail) throws SQLException {
        // Create a Statement
        pstmt = conn.prepareStatement("SELECT COUNT(*) FROM STUDENT WHERE EMAIL = ?");
        pstmt.setString(1, inEmail);
        ResultSet rs =  pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return (count > 0);
    }
    
    //Checks if the password is correct for the given email.
    //Used for user authentication (login).
    //args: email string, password string
    //returns boolean
    public boolean verified(String inEmail, String inPassword){
        int count = 0;
        
        try {
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM STUDENT WHERE EMAIL = ? AND PASSWORD = ?");
            pstmt.setString(1, inEmail);
            pstmt.setString(2, inPassword);
            
            ResultSet rs =  pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return (count > 0);
    }

    //Sets the current session values to the attributes of Student.
    //Used when login is successful
    //args: email string
    //returns  Student object
    Student getStudent(String inEmail) {
        Student loggedInStudent = new Student();
        try {
            pstmt = conn.prepareStatement("SELECT STUDENTID,EMAIL,STUDENTNAME FROM STUDENT WHERE EMAIL = ?");
            pstmt.setString(1, inEmail);
            
            ResultSet rs =  pstmt.executeQuery();
            rs.next();
            
            loggedInStudent.setStudentID(rs.getString("STUDENTID"));
            loggedInStudent.setEmail(rs.getString("EMAIL"));
            loggedInStudent.setStudentName(rs.getString("STUDENTNAME"));
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
        return loggedInStudent;
    }
    
    //Used for listing courses for students to enroll.
    //args: N/A
    //returns ArrayList of Course entities
    public ArrayList<Course> getCourses(){
        ArrayList<Course> temp = new ArrayList<Course>();
        try {
            pstmt = conn.prepareStatement("SELECT * FROM COURSE");
            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                Course course = new Course(rs.getString("COURSEID"),rs.getString("COURSETITLE"), rs.getInt("CREDITHOURS"));
                temp.add(course);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return temp;
    }
    
    //Creates enrolment for the current logged in student.
    //args: student's ID string, course's ID string
    //returns: N/A
    public void addSubject(String inStudentID, String inCourseID){
        String query = "INSERT INTO ENROLMENT (ENROLMENTID, STATUS, STUDENTID, COURSEID) VALUES (?,?,?,?)";
        
        try{
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, IDGenerator.generateEnrolmentID(10));
            pstmt.setString(2, "PENDING ADD");
            pstmt.setString(3, inStudentID);
            pstmt.setString(4, inCourseID);
            pstmt.execute();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    //Creates list of enrolled courses of logged in students.
    //Used for viewing the current state of courses.
    //args: student's ID string
    //returns: an arraylist of Enrolment entities
    public ArrayList<Enrolment> getStudentCourses(String inStudentID){
        ArrayList<Enrolment> temp = new ArrayList<Enrolment>();
        try {
            pstmt = conn.prepareStatement("SELECT COURSE.COURSEID, COURSE.COURSETITLE, COURSE.CREDITHOURS, ENROLMENT.STATUS "
                    + "FROM Enrolment "
                    + "INNER JOIN Course ON Course.CourseID = Enrolment.CourseID "
                    + "INNER JOIN Student ON Student.StudentID = Enrolment.StudentID "
                    + "WHERE ENROLMENT.STUDENTID = ?"
                    + "ORDER BY ENROLMENT.Status ASC");
            
            pstmt.setString(1, inStudentID);
            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                Enrolment enrolment = new Enrolment(rs.getString("COURSEID"),rs.getString("COURSETITLE"), rs.getInt("CREDITHOURS"), rs.getString("STATUS"));
                temp.add(enrolment);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
        return temp;
        
    }
    
    //Creates list of enrolled & pending Add courses of logged in students.
    //Used for viewing the current state of courses in add course page.
    //args: student's ID string
    //returns: an arraylist of Enrolment entities
    public ArrayList<Enrolment> getStudentCoursesEnrolledPendingAdd(String inStudentID){
        ArrayList<Enrolment> temp = new ArrayList<Enrolment>();
        try {
            pstmt = conn.prepareStatement("SELECT COURSE.COURSEID, COURSE.COURSETITLE, COURSE.CREDITHOURS, ENROLMENT.STATUS "
                    + "FROM Enrolment "
                    + "INNER JOIN Course ON Course.CourseID = Enrolment.CourseID "
                    + "INNER JOIN Student ON Student.StudentID = Enrolment.StudentID "
                    + "WHERE ENROLMENT.STUDENTID = ? AND (Enrolment.STATUS = 'ENROLLED' OR Enrolment.STATUS = 'PENDING ADD')"
                    + "ORDER BY ENROLMENT.Status ASC");
            
            pstmt.setString(1, inStudentID);
            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                Enrolment enrolment = new Enrolment(rs.getString("COURSEID"),rs.getString("COURSETITLE"), rs.getInt("CREDITHOURS"), rs.getString("STATUS"));
                temp.add(enrolment);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
        return temp;
        
    }
    
    //Creates list of enrolled & pending Add courses of logged in students.
    //Used for viewing the current state of courses in add course page.
    //args: student's ID string
    //returns: an arraylist of Enrolment entities
    public ArrayList<Enrolment> getStudentCoursesWithdrawnPendingDrop(String inStudentID){
        ArrayList<Enrolment> temp = new ArrayList<Enrolment>();
        try {
            pstmt = conn.prepareStatement("SELECT COURSE.COURSEID, COURSE.COURSETITLE, COURSE.CREDITHOURS, ENROLMENT.STATUS "
                    + "FROM Enrolment "
                    + "INNER JOIN Course ON Course.CourseID = Enrolment.CourseID "
                    + "INNER JOIN Student ON Student.StudentID = Enrolment.StudentID "
                    + "WHERE ENROLMENT.STUDENTID = ? AND (Enrolment.STATUS = 'WITHDRAWN' OR Enrolment.STATUS = 'PENDING DROP')"
                    + "ORDER BY ENROLMENT.Status ASC");
            
            pstmt.setString(1, inStudentID);
            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                Enrolment enrolment = new Enrolment(rs.getString("COURSEID"),rs.getString("COURSETITLE"), rs.getInt("CREDITHOURS"), rs.getString("STATUS"));
                temp.add(enrolment);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
        return temp;
        
    }
    
    //Lists all enrolment and withdrawal requests for admin to approve.
    //Used in the admin dashboard
    //args: N/A
    //returns: an array list of Pending Courses entities
    public ArrayList<PendingCourses> getStudentCourses(){
        ArrayList<PendingCourses> temp = new ArrayList<PendingCourses>();
        try {
            pstmt = conn.prepareStatement("SELECT ENROLMENT.ENROLMENTID, STUDENT.STUDENTNAME, COURSE.COURSEID, COURSE.COURSETITLE, ENROLMENT.STATUS "
                    + "FROM ENROLMENT "
                    + "INNER JOIN Course ON Course.CourseID = Enrolment.CourseID "
                    + "INNER JOIN Student ON Student.StudentID = Enrolment.StudentID "
                    + "WHERE ENROLMENT.Status LIKE 'PENDING %'");

            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                PendingCourses pendingCourses = new PendingCourses(rs.getString("ENROLMENTID"),rs.getString("STUDENTNAME"), rs.getString("COURSEID"), rs.getString("COURSETITLE"), rs.getString("STATUS"));
                temp.add(pendingCourses);
            }
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
       
        return temp;   
    }
    
    //Updates the status of add/drop requests.
    //Used in the admin dashboard.
    //args: status string(either ENROLLED or WITHDRAWN, depending on the type of request)
    //returns: N/A
    public void updateEnrolment(String state, String inEnrolmentID) throws SQLException{
        String query = "UPDATE Enrolment " +
            "SET Status = ?" +
            "WHERE EnrolmentID = ?";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, state );
            pstmt.setString(2, inEnrolmentID );
            pstmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Gets the total number of credit hours from the student's enrolled courses.
    //Used in determing whether the student can register for the semester or not.
    //args: the student's ID string.
    //returns: sum of credit hours.
    public int getStudentCreditHours(String inStudentID){
        int sum = 0;
        String query = "SELECT SUM(Course.CREDITHOURS) "
                + "FROM ENROLMENT "
                + "INNER JOIN Course ON Course.COURSEID = ENROLMENT.COURSEID "
                + "INNER JOIN Student ON Student.STUDENTID = ENROLMENT.STUDENTID "
                + "WHERE Enrolment.STUDENTID = ? "
                + "AND Enrolment.STATUS = 'ENROLLED'";
  
        try{
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, inStudentID);

            ResultSet rs =  pstmt.executeQuery();
            rs.next();
            sum = rs.getInt(1);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        
        
        return sum;
    }
    
    //Get a list of enrolled courses for a student.
    //Used for creating drop requests.
    //args: student's ID string
    //returns: an array list of array list of string(each array list of string is consideres as a row)
    public ArrayList<ArrayList<String>> enrolledCourses(String inStudentID){
        //array list for containing rows
        ArrayList<ArrayList<String>> res = new ArrayList<ArrayList<String>> ();
     
        String query = "SELECT Enrolment.ENROLMENTID, Course.COURSEID, Course.COURSETITLE, Enrolment.STATUS FROM Enrolment INNER JOIN Course ON Course.COURSEID = ENROLMENT.COURSEID WHERE ENROLMENT.STATUS = 'ENROLLED' AND ENROLMENT.STUDENTID = ?";
        
        try{
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, inStudentID);
            
            ResultSet rs =  pstmt.executeQuery();
            while (rs.next()) {
                //araylist for containing columns
                ArrayList<String> row = new ArrayList<String>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                
                res.add(row);
            }
        }
        catch(Exception e){
            
        }
        
        return res;
    }
    
    public void deleteRequest(String inEnrolmentID){
        String query = "DELETE FROM Enrolment WHERE ENROLMENTID = ?";
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, inEnrolmentID );
            pstmt.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
