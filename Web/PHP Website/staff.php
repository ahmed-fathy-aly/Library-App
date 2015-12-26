<?php require_once("includes/session.php"); ?>
<?php require_once("includes/connection.php"); ?>
<?php require_once("includes/functions.php"); ?>
<?php 
		if ($_SESSION['is_admin']!=1) 
		{
		if(logged_in())	redirect_to("index.php?admin=0");
		else 	redirect_to("index.php");
		}
?>
<?php
		$user_name = $_SESSION['name'];
		$user_id = $_SESSION['id'];
		$user_img = $_SESSION['image_url'];
		$reader_flag = $_SESSION['profstud_flag'];
		$admin_flag =$_SESSION['is_admin'];
		$user_id = $_SESSION['id'];
		if($admin_flag==1) $type="Administrator";
		else 
		{
		if($reader_flag==1) {$type="Professor";}
		else if($reader_flag==0) {$type="Student";}
		}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Staff Area</title>

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
                        <h1 class="page-head-line">Faculty of Engineering, AinShams University</h1>
                        <h1 class="page-subhead-line">Welcome to The Online Library Management System </h1>

                    </div>
                </div>
				<div class="row">
                              <ul  id="staff-menu">
                    


                    
                    <li>
                        <a href="lend_book.php"><i class="fa fa-toggle-on"></i>Lend a Book</a>
                        
                    </li>
                     
                      <li>
                        <a href="return_book.php"><i class="fa fa-bell "></i>Return a Book</a>
                    </li>
                     <li>
                        <a href="add_book.php"><i class="fa fa-circle-o "></i>Add Book</a>
                    </li>
					<li>
                        <a href="remove_book.php"><i class="fa fa-yelp "></i>Remove Book</a>
                    </li>
                    <li>
                        <a href="add_user.php"><i class="fa fa-sitemap "></i>Add user</a>
                    </li>
					
                   
                    
                </ul>
</div>
            </div>
            <!-- /. PAGE INNER  -->
        </div>
        <!-- /. PAGE WRAPPER  -->
    </div>
    <!-- /. WRAPPER  -->

        <div id="footer-sec">
Faculty of Engineering , Ain Shams University    </div>
    <script src="assets/js/jquery-1.10.2.js"></script>
    <!-- BOOTSTRAP SCRIPTS -->
    <script src="assets/js/bootstrap.js"></script>
     <!-- PAGE LEVEL SCRIPTS -->
    <script src="assets/js/jquery.prettyPhoto.js"></script>
    <script src="assets/js/jquery.mixitup.min.js"></script>
    <!-- METISMENU SCRIPTS -->
    <script src="assets/js/jquery.metisMenu.js"></script>
    <!-- CUSTOM SCRIPTS -->
    <script src="assets/js/custom.js"></script>
     <!-- CUSTOM Gallery Call SCRIPTS -->
    <script src="assets/js/galleryCustom.js"></script>

</body>
</html>
