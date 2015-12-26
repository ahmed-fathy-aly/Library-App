<?php require_once("includes/functions.php"); ?>
<?php
		session_start();
		session_unset ();
		
		$_SESSION = array();
		$_SESSION['temp_email'] = $_POST['email'];
		$_SESSION['temp_password'] = $_POST['password'];
		redirect_to("index.php");
?>