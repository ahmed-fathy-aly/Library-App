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

			$required_fields = array('user_name', 'email', 'password','img_url','user_type');
			foreach($required_fields as $fieldname) {
				if (!isset($_POST[$fieldname]) || (empty($_POST[$fieldname]) )) { 
					$errors[] = $fieldname; 
				}
			}
					if (isset($_POST['user_type']) && !empty($_POST['user_type'] ) && (($_POST['user_type']=='Student')|| ($_POST['user_type']=='Professor')) ) { 
					
					if (!isset($_POST['code']) || (empty($_POST['code']) )) { 
					 $fieldname ="Reader's Code";
					$errors[] = $fieldname; 
				} 
				}
					if (isset($_POST['user_type']) && !empty($_POST['user_type'] ) && ($_POST['user_type']=='Student') ) { 
					
					if (!isset($_POST['limit']) || (empty($_POST['limit']) )) { 
					 $fieldname ="Borrowed Books Limit";
					$errors[] = $fieldname; 
				} 
				}
			if (empty($errors)) {
	 	$adduser_name = mysqli_prep($_POST['user_name']);
		$adduser_id = "";
		$adduser_email = mysqli_prep($_POST['email']);
	if(isset($_POST['code'])&&!empty($_POST['code']))		$adduser_code = $_POST['code'];
	if(isset($_POST['limit'])&&!empty($_POST['limit']))	$adduser_limit = $_POST['limit'];
		$adduser_pswd = $_POST['password'];
     $adduser_img = $_POST['img_url'];
		$adduser_type =mysqli_prep($_POST['user_type']);
		if($adduser_type=="Administrator")	
		{
	$query="INSERT INTO `user` (`id`, `name`, `hashed_password`, `mail`, `is_admin`, `token`, `image_url`) VALUES (NULL, '{$adduser_name}', SHA1('{$adduser_pswd }'), '{$adduser_email}', '1',NULL, '{$adduser_img }')";
  
	
$result= $connection->query($query);

if($result)
{	
$query_id="SELECT LAST_INSERT_ID()";
$result_id= $connection->query($query_id);

$found_id = mysqli_fetch_array($result_id, MYSQLI_ASSOC);
$adduser_id=$found_id['LAST_INSERT_ID()'];
$query1="INSERT INTO `admin` (`id`, `joindate`) VALUES ('{$adduser_id}', now())";
$result1= $connection->query($query1);

if($result1)
{	
$message="Administrator Added Successfully with ID = ".$adduser_id;
}
else { $message="Error Adding Administrator";}
}else { $message="Error Adding Administrator";}
		}	
		else if ($adduser_type=="Professor"||$adduser_type=="Student")
		{
$query="INSERT INTO `user` (`id`, `name`, `hashed_password`, `mail`, `is_admin`, `token`, `image_url`) VALUES (NULL, '{$adduser_name}', SHA1('{$adduser_pswd }'), '{$adduser_email}', '0',NULL, '{$adduser_img }');";	
$result= $connection->query($query);

if($result)
{	
$query_id="SELECT LAST_INSERT_ID()";
$result_id= $connection->query($query_id);

$found_id = mysqli_fetch_array($result_id, MYSQLI_ASSOC);
$adduser_id=$found_id['LAST_INSERT_ID()'];
if ($adduser_type=="Professor")
{
$query1="INSERT INTO `reader` (id, `code`, `profstud_flag`, `books_limit`) VALUES ('{$adduser_id}', '{$adduser_code}', '1', NULL)";
}
else if ($adduser_type=="Student")
{
$query1="INSERT INTO `reader` (id, `code`, `profstud_flag`, `books_limit`) VALUES ('{$adduser_id}', '{$adduser_code}', '0', '{$adduser_limit}')";
}
$result1= $connection->query($query1);

if($result1)
{	
$message="User Added Successfully with ID = ".$adduser_id;
}
else { $message="Error Adding User";}
}else { $message="Error Adding User";}
					
			}}
		else {
				// Errors occurred
				$error_message = "There were " . count($errors) . " errors in the form.";
		}}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Add User</title>

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
                        <h1 class="page-head-line">Add New User </h1>
                        <h1 class="page-subhead-line">Enter data of the new user </h1>
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
                </div>
                <!-- /. ROW  -->
                <div class="row">
            <div class="col-md-6 col-sm-6 col-xs-12">
               <div class="panel panel-info">
                        
<div class="col-lg-12 col-md-12 col-sm-12
               <div class="panel panel-danger">
                           <div class="panel-body">
                            <form role="form" action="add_user.php" method="POST">
                                  <div class="form-group">
                                            <label>Enter Name</label>
                                            <input class="form-control" name="user_name" type="text">
                                            
                                        </div>      
                                 <div class="form-group">
                                            <label>Enter Email</label>
                                            <input class="form-control" name="email" type="email">
                                     
                                        </div>
                                            <div class="form-group">
                                            <label>Enter Password</label>
                                            <input class="form-control" name="password" type="password">
											</div>
                                            <div class="form-group">
                                            <label>Enter Photo URL</label>
                                            <textarea class="form-control" rows="3" name="img_url"></textarea>
                                        </div>                                  
										<div class="form-group">
                                            <label>User Type</label>
                                             <div class="radio" >
                                                <label>
                                                    <input type="radio" name="user_type" id="user_type" value="Administrator" onclick="document.getElementById('limit').disabled = true; document.getElementById('code').disabled = true;" >Administrator
                                                </label>
                                            </div>
                                            <div class="radio">
                                                <label>
                                                    <input type="radio" name="user_type" id="user_type" value="Professor" onclick="document.getElementById('limit').disabled = true; document.getElementById('code').disabled = false;">Professor
                                                </label>
                                            </div>
                                            <div class="radio">
                                                <label>
                                                    <input type="radio" name="user_type" id="user_type" value="Student" onclick="document.getElementById('limit').disabled = false; document.getElementById('code').disabled = false;">Student
                                                </label>
                                            </div>
                                        </div>
									     <div class="form-group">
											  <label>Reader's Code</label>
                                                <input class="form-control" id="code" name="code" type="text" placeholder="For Readers Only" disabled>
                                            </div>  
										     <div class="form-group">
											  <label>Borrowed Books Limit</label>
                                                <input class="form-control" id="limit" name="limit" type="text" placeholder="For Students Only" disabled>
                                            </div>  
                            <hr>
                                 
                                        <button type="submit" name="submit" class="btn btn-danger">Register Now </button>

                                    </form>
                            </div>
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
