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
		if($admin_flag==1) $type="Administrator";
		else 
		{
		if($reader_flag==1) {$type="Professor";}
		else if($reader_flag==0) {$type="Student";}
		}
		$search_value="";
		$message="";
		$copy_isn="";
		$reservation_code="";
		// error 1 : limit reached
		// error 3 : No available copies
		// error 2 : error reserving
		
		if (isset($_GET['error_reserve'])&&$_GET['error_reserve'])
		{
			$error_code = $_GET['error_reserve'];
		if($error_code==1)	$message= "Error : You've reached the maximum limit for reservations";
		if($error_code==3)	$message= "Error : No Available Copies for this Book";
		if($error_code==2)	$message= "Error Reserving Book";

		}
		else if(isset($_GET['reservation_code'])&&isset($_GET['copy_isn']))
		{
			if(!empty($_GET['reservation_code'])&&!empty($_GET['copy_isn']))
			{
		$message="Book Reserved Successfully.";	
		$copy_isn=$_GET['copy_isn'];
		$reservation_code=$_GET['reservation_code'];
			}
			else
			{
			$message= "Error Reserving Book";	
			}
		}
		if (isset($_POST['search'])) 
		{
			$search_value=$_POST['search_val'];
		}
  $query1 ="select title , author , isbn, img_url from book where title like '%{$search_value}%' or author like '%{$search_value}%'";
$book_array_set = $connection->query($query1);
			
	$count=0;		
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Books Page</title>

    <!-- BOOTSTRAP STYLES-->
    <link href="assets/css/bootstrap.css" rel="stylesheet" />
    <!-- FONTAWESOME STYLES-->
    <link href="assets/css/font-awesome.css" rel="stylesheet" />
	<!-- PAGE LEVEL STYLES -->
    <link href="assets/css/prettyPhoto.css" rel="stylesheet" />
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
                        <h1 class="page-head-line">Search for a book</h1>
               

                    </div>
                </div>
				       <div class="row">
					   <form action="view_books.php" method="post">
				            <div class="panel-footer">
                                <div class="input-group">
                                    <input type="text" class="form-control" name="search_val" placeholder="Search using book name or author name" />
                                    <span class="input-group-btn">
                                        <button class="btn btn-success" name="search" type="submit">SEARCH</button>
                                    </span>
                                </div>
								</div>
								</form>
								<p class="text-danger"><?php echo $message?></p>
					   <p class="text-danger"><?php if(!empty($reservation_code)) echo "Reservation Code : ".$reservation_code; ?></p>
					   <p class="text-danger"><?php if(!empty($copy_isn)){ echo "Reserved Copy ISN : ".$copy_isn; }?></p>
               
							</div>
							
                <!-- /. ROW  -->
                <div id="port-folio">
                      <div class="row " >
					  
					  
					  <?php		
										while($book_row = $book_array_set ->fetch_array(MYSQLI_ASSOC) )
										{
                                       echo "<div class=\"col-md-4 \">";

                    echo "<div class=\"portfolio-item awesome mix_all\" data-cat=\"awesome\" >";


                      echo  "<img src=\"".$book_row['img_url']."\" height =\"750px\" width=\"500px\" class=\"img-responsive\" alt=\"\" />";
                      echo "<div class=\"overlay\">  <p> ";
						echo "<span>".$book_row['title']."</span> By ".$book_row['author'];
						$query2 = "select count(*) from votes where book_isbn=".$book_row['isbn'];
						$result = $connection->query($query2);
			
							$found = mysqli_fetch_array($result, MYSQLI_ASSOC);
							$num_upvotes= $found['count(*)'];

						echo "<br/> Number of Upvotes : ".$num_upvotes."</p>  ";
	if($reader_flag==1){					
echo "<a class=\"btn btn-block btn-social btn-yahoo\" title=\"I Like it\" href=\"upvote.php?isbn=".$book_row['isbn']."\"><i class=\"fa fa-yahoo\"></i></a>";
	}
if($admin_flag==0){
echo "  <a class=\"btn btn-block btn-social btn-dropbox\" title=\"Reserve Book\" href=\"reserve_book.php?isbn=".$book_row['isbn']."\"><i class=\"fa fa-dropbox\"></i></a>";
}
echo "</div> </div> </div>";
										} 
						?>
                                        

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
<!-- JQUERY SCRIPTS -->
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
