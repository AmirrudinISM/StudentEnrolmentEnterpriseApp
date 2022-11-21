<%@page import="com.unikl.studentenrolment.entities.PendingCourses"%>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">

  <title>Admin Dashboard</title>
  <meta content="" name="description">
  <meta content="" name="keywords">
  
  <link href="https://fonts.gstatic.com" rel="preconnect">
  <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

  <link href="assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
  <link href="assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.snow.css" rel="stylesheet">
  <link href="assets/vendor/quill/quill.bubble.css" rel="stylesheet">
  <link href="assets/vendor/remixicon/remixicon.css" rel="stylesheet">
  <link href="assets/vendor/simple-datatables/style.css" rel="stylesheet">

  <link href="assets/css/style.css" rel="stylesheet">

</head>

<body>
    <%@ page import="java.util.ArrayList;" %>
    <%@ page import="com.unikl.studentenrolment.entities.Course;" %>
    <%@ page import="com.unikl.studentenrolment.entities.Enrolment;" %>
    <jsp:useBean id="db" scope="page" class="com.unikl.studentenrolment.web.DBController" />

  <header id="header" class="header fixed-top d-flex align-items-center">

    <div class="d-flex align-items-center justify-content-between">
      <a href="index.php" class="logo d-flex align-items-center">
    
        <span class="d-none d-lg-block">Student Enrollment</span>
      </a>
      <i class="bi bi-list toggle-sidebar-btn"></i>
    </div>
  </header>

  <aside id="sidebar" class="sidebar">

    <ul class="sidebar-nav" id="sidebar-nav">

      <li class="nav-item ">
        <a class="nav-link " href="adminDashboard.jsp">
          <i class="bi bi-grid"></i>
          <span>Admin Dashboard</span>
        </a>
      </li>
      
     

      <li class="nav-item">
        <a class="nav-link collapsed" href="studentLogin.jsp">
          <i class="bi bi-arrow-left-square"></i>
          <span>Logout</span>
        </a>
      </li>
    </ul>
  </aside>

  <main id="main" class="main">

    <div class="pagetitle">
      <h1>Admin Dashboard</h1>
      <nav>
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="">Home</a></li>
          <li class="breadcrumb-item active">Dashboard</li>
        </ol>
      </nav>
    </div>
    

    <section class="section dashboard">
      <div class="row">
        <div class="col-lg-12">
          <div class="row">
            <div class="col-12">
              <div class="card recent-sales">
                <div class="card-body">
                  <h5 class="card-title">Dashboard <span>| List course</span></h5>

                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">#</th>
                        <th scope="col">Student Name</th>
                        <th scope="col">Code</th>
                        <th scope="col">Course</th>
                        <th scope="col">Status</th>
                        <th scope="col">Action</th>
                        
                      </tr>
                    </thead>
                    <tbody>
                        <% 
                            
                            ArrayList<PendingCourses> courseList = db.getStudentCourses(); %>
                        <% for(int i = 0; i < courseList.size(); i++){ %>
                    <form action="AdminController" method="post">
                        <tr>
                            <th scope="row"><%= i %></th>
                            <td><%= courseList.get(i).getStudentName()%></td>
                            <td><%= courseList.get(i).getCourseID()%></td>
                            <td><%= courseList.get(i).getCourseTitle()%></td>
                            <td><%= courseList.get(i).getStatus()%></td>
                            <td><%
                                if(courseList.get(i).getStatus().equals("PENDING ADD")){
                                    out.append("<Button type='submit' name='approveAdd' value='"+courseList.get(i).getEnrolmentID()+"' class='btn btn-warning'>Approve Add</Button><Button type='submit' name='rejectAdd' value='"+courseList.get(i).getEnrolmentID()+"' class='btn btn-danger'>Reject Add </Button>");
                                }else{
                                    out.append(" <Button type='submit' name='approveDrop' value='"+courseList.get(i).getEnrolmentID()+"' class='btn btn-warning'>Approve Drop</Button><Button type='submit' name='rejectDrop' value='"+courseList.get(i).getEnrolmentID()+ "' class='btn btn-danger'>Reject Drop</Button>");
                                }
                                %>
                            </td>
                        </tr>
                        <% } %>
                    </form>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

  </main>


  <footer id="footer" class="footer">

  </footer>

  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>
  <script src="assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="assets/js/main.js"></script>

</body>

</html>