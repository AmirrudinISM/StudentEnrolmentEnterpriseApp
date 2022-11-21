/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unikl.studentenrolment.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Amirrudin
 */
public class AdminController extends HttpServlet {

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
            DBController db = new DBController();
            
            //admins clicks either approve add or approve drop & sets their values
            String approveAdd = request.getParameter("approveAdd");
            String approveDrop = request.getParameter("approveDrop");
            String rejectAdd = request.getParameter("rejectAdd");
            String rejectDrop = request.getParameter("rejectDrop");
            
            //if approve add was clicked
            if(approveAdd != null){
                try {
                    //update the student's enrolment
                    db.updateEnrolment("ENROLLED", approveAdd);
                } catch (Exception e) {
                    out.println("<p>"+ e.getMessage() +"</p>");
                }  
            }
            //if approve drop was clicked
            if(approveDrop != null){
                try {
                    //update the student's enrolment
                    db.updateEnrolment("WITHDRAWN", approveDrop);
                } catch (Exception e) {
                    out.println("<p>"+ e.getMessage() +"</p>");
                }
            }
            //iff reject add
            if(rejectAdd != null){
                try {
                    //update the student's enrolment
                    db.deleteRequest(rejectAdd);
                } catch (Exception e) {
                    out.println("<p>"+ e.getMessage() +"</p>");
                }
            }
            
            //if reject drop
            if(rejectDrop != null){
                try {
                    //update the student's enrolment
                    db.updateEnrolment("ENROLLED",rejectDrop);
                } catch (Exception e) {
                    out.println("<p>"+ e.getMessage() +"</p>");
                }
            }
            
            //refresh the page
            RequestDispatcher dispatcher = request.getRequestDispatcher("adminDashboard.jsp");
            dispatcher.forward(request, response);
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
