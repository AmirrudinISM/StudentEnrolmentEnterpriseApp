<!DOCTYPE html>
<html lang="en">
<%@ page import="java.util.ArrayList;" %>
<%@ page import="com.unikl.studentenrolment.entities.Enrolment;" %>
<jsp:useBean id="db" scope="page" class="com.unikl.studentenrolment.web.DBController" />
<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1.0" name="viewport">

  <title>Drop Subject</title>
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

  <header id="header" class="header fixed-top d-flex align-items-center">

    <div class="d-flex align-items-center justify-content-between">
      <a href="studentDashboard.jsp" class="logo d-flex align-items-center">
    
        <span class="d-none d-lg-block">Student Enrollment</span>
      </a>
      <i class="bi bi-list toggle-sidebar-btn"></i>
    </div>
  </header>

  <aside id="sidebar" class="sidebar">

    <ul class="sidebar-nav" id="sidebar-nav">

      <li class="nav-item ">
        <a class="nav-link collapsed" href="studentDashboard.jsp">
          <i class="bi bi-grid"></i>
          <span>Student Dashboard</span>
        </a>
      </li>
      
      <li class="nav-item ">
        <a class="nav-link collapsed" href="addSubject.jsp">
          <i class="bi bi-grid"></i>
          <span>Register Course</span>
        </a>
      </li>
      <li class="nav-item ">
        <a class="nav-link" href="dropSubject.jsp">
          <i class="bi bi-grid"></i>
          <span>Drop Course</span>
        </a>
      </li>

      <li class="nav-item">
          <form action="StudentEnrolmentController" method="post">
               <a class="nav-link collapsed" >
          <i class="bi bi-arrow-left-square"></i>
          <span ><input class="btn btn-danger" type="submit" name="submit" value="Logout" ></span>
        </a>
          </form>
       
      </li>
    </ul>
  </aside>

  <main id="main" class="main">

    <div class="pagetitle">
      <h1>Student Dashboard</h1>
      <nav>
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="studentDashboard.jsp">Home</a></li>
          <li class="breadcrumb-item active">Drop Course</li>
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
                    <h5 class="card-title">Course <span>| Drop course</span></h5>
                    <p>Name : ${name}</p>
                    <p>ID   : ${ID}</p>
                    <p>Email: ${email}</p>
                    <div class="form-group col-md-4">
                        <form action="StudentEnrolmentController" method="post">
                            <label for="inputState">Course</label>
                            <select id="inputState" name="courseToDrop" class="form-control">
                                <option value="">-- Select Course to drop --</option>
                                <% ArrayList<ArrayList<String>> courseList = db.enrolledCourses(session.getAttribute("ID").toString()); %>
                                <% for(int i = 0; i < courseList.size(); i++){ %>
                                    <option value="<%= courseList.get(i).get(0) %>"><%= courseList.get(i).get(1) + " " +courseList.get(i).get(2)%></option>
                                <% } %>
                            </select>
                            <br>
                            <div class="form-group col-md-4">
                                <button type="submit" name="submit" value="dropCourse" class="btn btn-danger">Drop</button>
                            </div>
                        </form>
                    </div>
                    <br>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

    <section class="section dashboard">
      <div class="row">
        <div class="col-lg-12">
          <div class="row">
            <div class="col-12">
              <div class="card recent-sales">
                <div class="card-body">
                  <h5 class="card-title">Course <span>| Withdrawn Courses</span></h5>

                  <table class="table">
                    <thead>
                      <tr>
                        <th scope="col">#</th>
                        <th scope="col">Code</th>
                        <th scope="col">Course</th>
                        <th scope="col">Credit Hours</th>
                        <th scope="col">Status</th>
                      </tr>
                    </thead>
                    <% ArrayList<Enrolment> enrolled = db.getStudentCoursesWithdrawnPendingDrop(session.getAttribute("ID").toString()); %>
                        <% for(int i = 0; i < enrolled.size(); i++){ %>
                        <tr>
                            <th scope="row"><%= i %></th>
                            <td><%= enrolled.get(i).getCourseID() %></td>
                            <td><%= enrolled.get(i).getCourseTitle()%></td>
                            <td><%= enrolled.get(i).getCreditHours() %></td>
                            <td><%= enrolled.get(i).getStatus() %></td>
                        </tr>
                        <% } %>
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