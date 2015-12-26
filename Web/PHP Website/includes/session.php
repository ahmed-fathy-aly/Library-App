<?php
	session_start();
	
	function logged_in() {
		return isset($_SESSION['id']);
	}
	function admin_logged_in() {
		if (($_SESSION['is_admin']=='0')|| ($_SESSION['is_admin']==0) || !isset($_SESSION['is_admin'])) return NULL;
	}
	function confirm_logged_in() {
		if (!logged_in()) {
			redirect_to("index.php");
		}
	}
	function confirm_admin_logged_in() {
		if (!admin_logged_in()) 
		{
		if(logged_in())	redirect_to("index.php?admin=0");
		else 	redirect_to("index.php");
		}
	}
?>
