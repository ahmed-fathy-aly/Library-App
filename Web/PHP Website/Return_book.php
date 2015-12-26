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
		$message="";
		
		
		if (isset($_POST['submit']))
		{
			$errors = array();

			$required_fields = array('isbn', 'reader_code','isn');
			foreach($required_fields as $fieldname) {
				if (!isset($_POST[$fieldname]) || (empty($_POST[$fieldname]) )) { 
					$errors[] = $fieldname; 
				}
			}
			if (empty($errors)) {
				
			$reader_code=$_POST['reader_code'];
			$isn = $_POST['isn'];
			$isbn = $_POST['isbn'];
			$query_id ="select id , profstud_flag from reader where code='{$reader_code}'";	
$result_id= $connection->query($query_id);

$id_data = mysqli_fetch_array($result_id, MYSQLI_ASSOC);
$reader_id=$id_data['id'];	
$reader_flag2=$id_data['profstud_flag'];	

	if(isset($_POST['reservation_code'])&&(!empty($_POST['reservation_code'])))
	{
   $query ="UPDATE reservation SET `return_date` = now(), `return_admin_id` = '{$user_id}' WHERE reader_id = '{$reader_id}' AND copy_isn = '{$isn}' AND reservation_code = '{$_POST['reservation_code']}'";	
		
	}
	else
	{
$query ="update reservation r1 inner join reservation r2 on
(r1.`reader_id`= r2.`reader_id`  and r1.`copy_isn`=r2.`copy_isn` and r1.`reader_id`='{$reader_id}' and r1.`copy_isn`='{$isn}' and r1.`return_date` IS NULL)
set r1.`return_admin_id`='{$user_id}' , r1.`return_date` = now()";

		
	}
	
	$query11="UPDATE `book_copy` SET `is_available` = '1' WHERE `book_copy`.`book_isbn` = '{$isbn}' AND `book_copy`.`isn` = '{$isn}'";
	
$result= $connection->query($query);	

	$result11 = $connection->query($query11);

if($result&&$result11) {$message="Book Return recorded Successfully";}
else {$message="Error recording Book return".$connection->error;}	
	}
else {
				// Errors occurred
				$error_message = "There were " . count($errors) . " errors in the form.";
			}
		}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Return Book</title>

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
                        <h1 class="page-head-line">Return a Book</h1>
						<h1 class="page-subhead-line">Enter Data of the book copy </h1>
				       <p class="text-danger"><?php echo $message?></p>
					   <?php if (!empty($error_message)) {
				echo "<p class=\"text-danger\">" . $error_message . "</p>";
			} ?>
			<?php
			// output a list of the fields that had errors
			if (!empty($errors)) {
				echo "<p class=\"text-danger\">";
				echo "Please review the following fields:<br />";
				foreach($errors as $error) {
					echo " - " . $error . "<br />";
				}
				echo "</p>";
			}
			?>
                </div>
                <!-- /. ROW  -->
                
            <div class="col-md-6 col-sm-6 col-xs-12">
               <div class="panel panel-info">
                        
                        <div class="panel-body">
                            <form role="form" action ="Return_book.php" method="post">
                                        <div class="form-group">
                                            <label>Enter Reader's Code</label>
                                            <input class="form-control" name="reader_code" type="text">
                                       
                                        </div>
										<div class="form-group">
                                            <label>Enter Book  ISBN</label>
                                            <input class="form-control" name="isbn" type="text">
                                            
                                        </div>
										<div class="form-group">
                                            <label>Enter Book Copy ISN</label>
                                            <input class="form-control" name="isn" type="text">
                                            
                                        </div>
										<div class="form-group">
                                            <label>Enter Reservation Code (Optional)</label>
                                            <input class="form-control" name="reservation_code" type="text">
                                          
                                        </div>
										

                                  
                                 
                                        <button type="submit" name="submit" class="btn btn-info">Submit </button>

                                    </form>
                            </div>
                        </div>
                            </div>
      
             <!--/.ROW-->
            

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
