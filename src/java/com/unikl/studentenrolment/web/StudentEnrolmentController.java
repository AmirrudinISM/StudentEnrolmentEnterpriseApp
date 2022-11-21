/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unikl.studentenrolment.web;


import com.unikl.studentenrolment.entities.Student;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import my.enrollment.myLibrary.Verify;

/**
 *
 * @author Amirrudin
 */
public class StudentEnrolmentController extends HttpServlet {
    DBController db;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //determin which button is clicked through out the web application.
            String submitType = request.getParameter("submit");
            //create instance of DB to get & save data.
            DBController db = new DBController();
            //initiates session when student logs in.
            HttpSession session = request.getSession();
            //removes error messages that occur when log in is unsuccessful.
            session.removeAttribute("errors");
            //using shared library, allow for easier user authentication.
            Verify ver = new Verify();
            
            //register student
            if(submitType.equals("register")){
                
                out.println("<p>You selected register</p>");
                //get all info from forms
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String password1 = request.getParameter("password1");
                String password2 = request.getParameter("password2");
                
                out.println("<p>"+ name +"</p>");
                out.println("<p>"+ email +"</p>");
                out.println("<p>"+ password1 +"</p>");
                out.println("<p>"+ password2 +"</p>");
                
                ArrayList<String> errorMessages = new ArrayList<String> ();
                
                try {
                    if (db.userExist(email)) {
                        errorMessages.add("Email already taken");
                    }
                    else{
                        errorMessages.add(ver.verifyEmail(email));
                    }
                    
                
                    
                    errorMessages.add(ver.verifyPassword(password1, password2));
                    
                    boolean hasError = false; 
                    for(int i = 0; i < errorMessages.size(); i++){
                        if(errorMessages.get(i) != null){
                            hasError = true;
                        }
                    }
                    
                    if(!hasError){
                        
                        Student inStudent = new Student(email, password1, name);
                        db.insertStudent(inStudent);
                        session.removeAttribute("errors");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("studentLogin.jsp");
                        dispatcher.forward(request, response);
                        
                    }
                    else{
                        session.setAttribute("errors", errorMessages);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("studentRegister.jsp");
                        dispatcher.forward(request, response);
                        
                    }
                    
                } catch (Exception e) {
                    if(errorMessages.size() > 0){
                        for(int i = 0; i < errorMessages.size(); i++){
                            if(errorMessages.get(i) != null){
                                out.println("<p> Error message "+ i + ": "+ errorMessages.get(i) +"</p>");
                            }
                            
                        }
                    }
                    out.println("<p> Exception message: "+ e.getMessage() +"</p>");
                }
            }
            //student login
            else if(submitType.equals("login")){
                
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                
                
                out.println("<p>"+ email +"</p>");
                out.println("<p>"+ password +"</p>");
                //user does not exist
                if(db.verified(email, password)){
                    Student loggedIn = db.getStudent(email);
                    session.setAttribute("ID", loggedIn.getStudentID());
                    session.setAttribute("name", loggedIn.getStudentName());
                    session.setAttribute("email", loggedIn.getEmail());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("studentDashboard.jsp");
                    dispatcher.forward(request, response);
                    
                }
                else{
                    out.println("<p>Either email does not exist or password is incorrect</p>");
                }
                
            }
            //student add course
            else if(submitType.equals("addSubject")){
                out.println("<p> SELECTED COURSE: " + request.getParameter("selectedCourse")+"</p>");
                out.println("<p> STUDENT ID: " + session.getAttribute("ID") + "</p>");
                
                String studentID = (String)session.getAttribute("ID");
                String selectedCourseID = request.getParameter("selectedCourse");
                db.addSubject(studentID, selectedCourseID);
                RequestDispatcher dispatcher = request.getRequestDispatcher("addSubject.jsp");
                dispatcher.forward(request, response);
                
            }
            //student logs out
            else if(submitType.equals("Logout")){
                session.invalidate();
                response.sendRedirect("studentLogin.jsp");
                return;
            }
            //admin logs in
            else if(submitType.equals("adminLogin")){
                String adminName = request.getParameter("email");
                String adminPassword = request.getParameter("password");
                
                
                if(adminName.equals("admin@unikl.com") && adminPassword.equals("123")){
                    RequestDispatcher dispatcher = request.getRequestDispatcher("adminDashboard.jsp");
                    dispatcher.forward(request, response);
                }
                else{
                    RequestDispatcher dispatcher = request.getRequestDispatcher("admin_login.jsp");
                    dispatcher.forward(request, response);
                }
            }
            //student drops course
            else if(submitType.equals("dropCourse")){
                try {
                    db.updateEnrolment("PENDING DROP",request.getParameter("courseToDrop"));
                    RequestDispatcher dispatcher = request.getRequestDispatcher("dropSubject.jsp");
                    dispatcher.forward(request, response);
                } catch (Exception e) {
                    out.println("<p>"+ e.getMessage() +"</p>");
                }
            }
            //none is chosen
            else{
                out.println("<p>You DID NOT select register</p>");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
