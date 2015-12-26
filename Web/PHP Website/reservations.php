<?php require_once("includes/session.php"); ?>
<?php require_once("includes/connection.php"); ?>
<?php require_once("includes/functions.php"); ?>
<?php confirm_logged_in(); ?>
<?php
		$user_name = $_SESSION['name'];
		$user_id = $_SESSION['id'];
		$user_img = $_SESSION['image_url'];
		$reader_flag = $_SESSION['profstud_flag'];
		$admin_flag =$_SESSION['is_admin'];
		$user_id = $_SESSION['id'];
		if($admin_flag==1) 
		{	
		$type="Administrator";
		$reservation_query= "SELECT * FROM `reservation` order by reservation_code";
		}
		else 
		{
			$reservation_query="SELECT * FROM `reservation` WHERE reader_id= '{$user_id}' order by reservation_code";
		if($reader_flag==1) {$type="Professor";}
			else if($reader_flag==0) {$type="Student";}
		}
		$result_set = $connection->query($reservation_query);
			
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Reservations</title>

    <!-- BOOTSTRAP STYLES-->
    <link href="assets/css/bootstrap.css" rel="stylesheet" />
    <!-- FONTAWESOME STYLES-->
    <link href="assets/css/font-awesome.css" rel="stylesheet" />
       <!--CUSTOM BASIC STYLES-->
    <link href="assets/css/basic.css" rel="stylesheet" />
    <!--CUSTOM MAIN STYLES-->
    <link href="assets/css/custom.css" rel="stylesheet" />
    <!-- GOOGLE FONTS-->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
</head>
<body>
    <div id="wrapper">
        <nav class="navbar navbar-default navbar-cls-top " role="navigation" style="margin-bottom: 0">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
				<a class="navbar-brand" href="home.php">Welcome</a>
            </div>

            <div class="header-right">

                <a href="logout.php" class="btn btn-danger" title="Logout"><i class="fa fa-exclamation-circle fa-2x"></i></a>

            </div>
        </nav>
        <!-- /. NAV TOP  -->
        <nav class="navbar-default navbar-side" role="navigation">
            <div class="sidebar-collapse">
                <ul class="nav" id="main-menu">
                    <li>
                        <div class="user-img-div">
                            <img src="<?php echo htmlentities($user_img); ?>" class="img-thumbnail" />

                            <div class="inner-text">
                               <?php echo htmlentities($user_name); ?>
                            <br />
                                <small>USER ID : <?php echo htmlentities($user_id); ?></small>
                            </div>
                        </div>

                    </li>


                    <li>
                        <a class="active-menu" href="index.php"><i class="fa fa-dashboard "></i><?php echo htmlentities($type); ?></a>
                    </li>
                    
                    <li>
                        <a href="view_books.php"><i class="fa fa-flash "></i>View Books </a>
                        
                    </li>
                     
                      <li>
                        <a href="reservations.php"><i class="fa fa-anchor "></i>View Reservations</a>
                    </li>
                     <li>
                        <a href="exceeded_reservations.php"><i class="fa fa-bug "></i>View Reservations that exceeded deadline</a>
                    </li>
                    <li>
                        <a href="staff.php"><i class="fa fa-sign-in "></i>Staff Area</a>
                    </li>
             
                   
                    
                </ul>

            </div>

        </nav> 
		
        <!-- /. NAV SIDE  -->
        <div id="page-wrapper">
            <div id="page-inner">
                <div class="row">
                    <div class="col-md-12">
                        <h1 class="page-head-line">List of Reservation</h1>
                    </div>
                </div>
                <!-- /. ROW  -->
              
            <div class="row">
                <div class="col-md-12">
                  
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Reservations Data
                        </div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered table-hover">
                                    <thead>
                                        <tr>
                                            <th>Reader ID</th>
                                            <th>Book Copy ISN</th>
                                            <th>Reservation Code</th>
                                            <th>Lending Date</th>
											<th>Return Date</th>
											<th>Return Deadline</th>
											<th>Lending Admin_ID</th>
											<th>Return Admin_ID</th>
                                        </tr>
                                    </thead>
                                    <tbody>
										<?php		
										while($table_row = $result_set ->fetch_array(MYSQLI_ASSOC) )
										{
                                       echo '<tr>';
                                           echo '<td>'.$table_row['reader_id'].'</td>';
                                           echo '<td>'. $table_row['copy_isn'].'</td>';
											echo '<td>'.$table_row['reservation_code'].'</td>';
											echo '<td>'.$table_row['lending_date'].'</td>';
											echo '<td>'.$table_row['return_date'].'</td>';
											echo '<td>'.$table_row['return_deadline'].'</td>';
											echo '<td>'.$table_row['lending_admin_id'].'</td>';
											echo '<td>'.$table_row['return_admin_id'].'</td>';
                                        echo '</tr>';
										} 
										?>
                                        
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div> 
            </div>
               

            </div>
            <!-- /. PAGE INNER  -->
        </div>
        <!-- /. PAGE WRAPPER  -->
    </div>
    <!-- /. WRAPPER  -->
     <div id="footer-sec">
Faculty of Engineering , Ain Shams University    </div>
   

</body>
</html>
