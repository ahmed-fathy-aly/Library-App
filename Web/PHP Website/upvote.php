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
		if($type=="Professor")
		{
		$query = "INSERT INTO votes (`professor_id`, `book_isbn`) VALUES ('{$user_id}','{$_GET['isbn']}' )";
$result = $connection->query($query);
		}
       redirect_to("view_books.php");
		
?>