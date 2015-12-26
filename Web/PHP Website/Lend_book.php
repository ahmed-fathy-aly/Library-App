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
			$reader_id="";
			$isn = $_POST['isn'];
			$isbn = $_POST['isbn'];
			$reservation_code="";
			$deadline_date="";
			$flag_limit=1;
			$reserved_flag=0;
$query_id ="select id , profstud_flag from reader where code='{$reader_code}'";	
$result_id= $connection->query($query_id);

$id_data = mysqli_fetch_array($result_id, MYSQLI_ASSOC);
$reader_id=$id_data['id'];	
$reader_flag2=$id_data['profstud_flag'];	
	
if ($reader_flag2==0)
{
$flag_limit=0 ; 
$query_limit= "select count(`reservation_code`) , books_limit from reservation , reader where reservation.`reader_id`='{$reader_id}' and reader.id='{$reader_id}'  and `return_date` IS NULL";	
$result_limit= $connection->query($query_limit);

$limit_data = mysqli_fetch_array($result_limit, MYSQLI_ASSOC);
$reserved_count=$limit_data['count(`reservation_code`)'];
$books_limit = 	$limit_data['books_limit'];
if(!isset($books_limit)||empty($books_limit)) {$message="Error : Reader Not Found"; $flag_limit=2 ; }
else{
if($reserved_count<$books_limit) {$flag_limit=1 ; }
else { //check if already reserved
$query_check ="select reservation_code , return_deadline from reservation where reader_id = '{$reader_id}' AND copy_isn = '{$isn}' and lending_date IS NULL AND `return_date` IS NULL and lending_admin_id IS NULL and return_admin_id IS NULL";		
$result_check = $connection->query($query_check);
			if (($result_check)&&($result_check->num_rows == 1))  //already reserved 
			{
				$flag_limit=1 ;
				$reserved_flag=1;
				$found_data = mysqli_fetch_array($result_check, MYSQLI_ASSOC);
				$reservation_code=$found_data['reservation_code'];
				$return_deadline=$found_data['return_deadline'];
				$deadline_date = date_create($return_deadline);
				$message="Lending Book recorded Successfully";
				
			}
	
	
}
}
}

if($flag_limit==1)
{
	if(isset($_POST['reservation_code'])&&(!empty($_POST['reservation_code'])))  // reservation_code given
	{
		if($reserved_flag!=1){
		$reservation_code=$_POST['reservation_code'];
		}
   $query1 ="UPDATE reservation SET `lending_date` = now() , `lending_admin_id` = '{$user_id}' WHERE reader_id = '{$reader_id}' AND copy_isn = '{$isn}' AND `reservation_code` = '{$reservation_code}'";	
	if($reserved_flag!=1){
	$query2= "select reservation_code , return_deadline from reservation where reader_id = '{$reader_id}' AND copy_isn = '{$isn}' AND `reservation_code` = '{$reservation_code}'";
	}$result= $connection->query($query1);
	if($reserved_flag!=1){
$result2= $connection->query($query2);
	}
$query11="UPDATE `book_copy` SET `is_available` = '0' WHERE `book_copy`.`book_isbn` = '{$isbn}' AND `book_copy`.`isn` = '{$isn}'";
			$result11 = $connection->query($query11);

if (($reserved_flag!=1)&&($result)&&($result2)&&$result11&&($result2->num_rows == 1)&&($found_data['reservation_code'] ==$_POST['reservation_code']))  {
$message="Lending Book recorded Successfully";
				$found_data = mysqli_fetch_array($result2, MYSQLI_ASSOC);
				$reservation_code=$found_data['reservation_code'];
				$return_deadline=$found_data['return_deadline'];
				$deadline_date = date_create($return_deadline);
	
	}
else if ($reserved_flag!=1){$message="Error recording lending event ".$connection->error;
unset($reservation_code);
unset($deadline_date);
}	
	}
	else  // reservation code not given
	{
//check if reserved : return reservation code
// if not reserved : reserve and return reservation code
if($reserved_flag!=1){
$query_check ="select reservation_code , return_deadline from reservation where reader_id = '{$reader_id}' AND copy_isn = '{$isn}' and lending_date IS NULL AND `return_date` IS NULL and lending_admin_id IS NULL and return_admin_id IS NULL";		
$result_check = $connection->query($query_check);
}
			if ((($result_check)&&($result_check->num_rows == 1))||($reserved_flag==1))  //already reserved 
			{
				if($reserved_flag!=1){
$found_data = mysqli_fetch_array($result_check, MYSQLI_ASSOC);
				$reservation_code=$found_data['reservation_code'];
				$return_deadline=$found_data['return_deadline'];
				$deadline_date = date_create($return_deadline);
				}	
$query1 ="UPDATE reservation SET `lending_date` = now() , `lending_admin_id` = '{$user_id}' WHERE reader_id = '{$reader_id}' AND copy_isn = '{$isn}' AND `reservation_code` = '{$reservation_code}'";	
$result= $connection->query($query1);

$query11="UPDATE `book_copy` SET `is_available` = '0' WHERE `book_copy`.`book_isbn` = '{$isbn}' AND `book_copy`.`isn` = '{$isn}'";
			$result11 = $connection->query($query11);

if($result&&$result11)
{
$message="Lending Book recorded Successfully";				
}	
else {$message="Error recording lending event ".$connection->error;
unset($reservation_code);
unset($deadline_date);
}
			}
			else // not reserved
			{
	
	$query1="INSERT INTO `reservation` (`reader_id`, `copy_isn`, `reservation_code`, `reservation_date`, `lending_date`, `return_date`, `return_deadline`, `return_admin_id`, `lending_admin_id`) 
values( '{$reader_id}' , '{$isn}', NULL, now(), now(), NULL,DATE_ADD(now(),INTERVAL 45 DAY), NULL, '{$user_id}')";	
	$result= $connection->query($query1);
		


$query11="UPDATE `book_copy` SET `is_available` = '0' WHERE `book_copy`.`book_isbn` = '{$isbn}' AND `book_copy`.`isn` = '{$isn}'";
			$result11 = $connection->query($query11);

if($result&&$result11)
{
$message="Lending Book recorded Successfully";				
}
else {$message="Error recording lending event ".$connection->error;
unset($reservation_code);
unset($deadline_date);
}	
			}

	}

}
			else if ($flag_limit==0){$message="Error : Reader has reached Maximum Borrowed Books Limit!!";} }
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
    <title>Lend Book</title>

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
                        <h1 class="page-head-line">Lend a Book</h1>
						<h1 class="page-subhead-line">Enter Reservation Data </h1>
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
				       <p class="text-danger"><?php echo $message?></p>
					   <p class="text-danger"><?php if(!empty($reservation_code)) echo "Reservation Code : ".$reservation_code; ?></p>
					   <p class="text-danger"><?php if(!empty($deadline_date)){ echo "Return_deadline : "; if(isset($deadline_date) && !empty($deadline_date)) echo date_format($deadline_date, 'Y-m-d H:i:s'); }?></p>
                </div>
                <!-- /. ROW  -->
                
            <div class="col-md-6 col-sm-6 col-xs-12">
               <div class="panel panel-info">
                        
                        <div class="panel-body">
                            <form role="form" action ="Lend_book.php" method="post">
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
