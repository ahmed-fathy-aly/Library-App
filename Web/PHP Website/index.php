<?php require_once("includes/session.php"); ?>
<?php require_once("includes/connection.php"); ?>
<?php require_once("includes/functions.php"); ?>
<?php
	
	if (logged_in()) {
		
		if (isset($_GET['admin']) && $_GET['admin']=='0')
		{
		$message2="Login as administrator or click here to go to reader's page";
		}
			else redirect_to("home.php");
	    }

	include_once("includes/form_functions.php");
	
	// START FORM PROCESSING
	if (isset($_SESSION['temp_email'] )|| isset($_SESSION['temp_password'])) { // Form has been submitted.

		
		
		$email = trim(mysqli_prep($_SESSION['temp_email']));
		$password = trim(mysqli_prep($_SESSION['temp_password']));
		$hashed_password = sha1($password);
		
		
			// Check database to see if email and the hashed password exist there.
			$query = "SELECT user.id,name,mail,is_admin,image_url,profstud_flag  ";
			$query .= "FROM user LEFT JOIN reader ON reader.id = user.id ";
			$query .= "WHERE mail = '{$email}' ";
			$query .= "AND hashed_password = '{$hashed_password}' ";
			$query .= "LIMIT 1";
			$result_set = $connection->query($query);
			
			if ($result_set->num_rows == 1) {
				// email/password authenticated
				// and only 1 match
				$found_user = mysqli_fetch_array($result_set, MYSQLI_ASSOC);
			    $_SESSION['id'] = $found_user['id'];
				$_SESSION['mail'] = $found_user['mail'];
				$_SESSION['name'] = $found_user['name'];
				$_SESSION['is_admin'] = $found_user['is_admin'];
				$_SESSION['profstud_flag'] = $found_user['profstud_flag'];
				$_SESSION['image_url'] = $found_user['image_url'];
				unset ($_SESSION['temp_email']);
				unset ($_SESSION['temp_password']);
				
				redirect_to("home.php");
			} else {
				// email/password combo was not found in the database
				$message = "email/password combination incorrect.<br />
					Please make sure your caps lock key is off and try again.";
			}
		
	} else { // Form has not been submitted.
		if (isset($_GET['logout']) && $_GET['logout'] == 1) {
			$message1 = "You are now logged out.";
		} 
		$email = "";
		$password = "";
	}
?>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
      <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login Page</title>

    <!-- BOOTSTRAP STYLES-->
    <link href="assets/css/bootstrap.css" rel="stylesheet" />
    <!-- FONTAWESOME STYLES-->
    <link href="assets/css/font-awesome.css" rel="stylesheet" />
    <!-- GOOGLE FONTS-->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />

</head>
<body style="background-color: #E2E2E2;">

    <div class="container">
        <div class="row text-center " style="padding-top:100px;">
            <div class="col-md-12">
                <img src="assets/img/logo-invoice.gif" />
            </div>
        </div>
         <div class="row ">
               
                <div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">
                           
                            <div class="panel-body">
                                <form role="form" action="logoutlogin.php" method="post">
                                    <hr />
									<?php if (!empty($message1)) {echo "<p class=\"p-err\">" . $message1 . "</p>";} ?>
									<br />
									<?php if (!empty($message2)) {echo "<a class=\"p-err\" href=\"home.php\">" . $message2 . "</a>";} ?>
									<br />
                                    <h5>Enter Your Data to Login</h5>
                                       <br />
									   <?php if (!empty($message)) {echo "<p class=\"p-err\">" . $message . "</p>";} ?>
									   <br />
			                         
                                        <div class="form-group input-group">
                                            <span class="input-group-addon"><i class="fa fa-tag"  ></i></span>
                                            <input type="email" name="email" maxlength="255" class="form-control" value="<?php echo htmlentities($email); ?>" />
                                        </div>
                                                                              <div class="form-group input-group">
                                            <span class="input-group-addon"><i class="fa fa-lock"  ></i></span>
                                            <input type="password" name="password" maxlength="30" class="form-control"  value="<?php echo htmlentities($password); ?>" />
                                        </div>
                                                                         
                                     <input type="submit" name="submit"  value="Login"></a>
                                    <hr />
                                   </form>
                            </div>
                           
                        </div>
                
                
        </div>
    </div>

<script>

</script>
</body>
</html>
