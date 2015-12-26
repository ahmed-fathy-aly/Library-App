<?php require_once("includes/session.php"); ?>
<?php require_once("includes/connection.php"); ?>
<?php require_once("includes/functions.php"); ?>
<?php confirm_logged_in(); ?>
<?php
$reader_flag = $_SESSION['profstud_flag'];
$admin_flag =$_SESSION['is_admin'];
if($admin_flag==1) {$type="Administrator"; redirect_to("view_books.php");}
		else 
		{
		if($reader_flag==1) {$type="Professor";}
		else if($reader_flag==0) {$type="Student";}
		}
		$user_name = $_SESSION['name'];
		$reader_id = $_SESSION['id'];
		$user_img = $_SESSION['image_url'];
		
		$user_id = $_SESSION['id'];
		
		$message="";
		
		
		if (isset($_GET['isbn']))
		{
			$isn ="";
			$isbn = $_GET['isbn'];
			$reservation_code="";
			$flag_limit=1;
$done_flag=0;
if ($reader_flag==0)
{
$flag_limit=0 ; 
$query_limit= "select count(`reservation_code`) , books_limit from reservation , reader where reservation.`reader_id`='{$reader_id}' and reader.id='{$reader_id}'  and `return_date` IS NULL";	
$result_limit= $connection->query($query_limit);

$limit_data = mysqli_fetch_array($result_limit, MYSQLI_ASSOC);
$reserved_count=$limit_data['count(`reservation_code`)'];
$books_limit = 	$limit_data['books_limit'];
if($reserved_count<$books_limit) {$flag_limit=1 ; }

}

if($flag_limit==1)
{
//check if reserved : return reservation code
// if not reserved : reserve and return reservation code
$query_check ="select reservation_code , copy_isn from reservation , book_copy
where reservation.reader_id = '{$reader_id}' AND `return_date` IS NULL  and return_admin_id IS NULL
AND reservation.copy_isn = book_copy.isn and book_copy.book_isbn= '{$isbn}'
";		
$result_check = $connection->query($query_check);
			if (($result_check)&&($result_check->num_rows == 1))  //already reserved 
			{
$found_data = mysqli_fetch_array($result_check, MYSQLI_ASSOC);
				$reservation_code=$found_data['reservation_code'];
				$isn=$found_data['copy_isn'];
$done_flag=1;
			}
			else // not reserved
			{
	
				
$query_isn ="select isn from book_copy where book_isbn='{$isbn}' and is_available='1' limit 1
";		
$resultisn= $connection->query($query_isn);

				$found_isn = mysqli_fetch_array($resultisn, MYSQLI_ASSOC);
				$isn=$found_isn['isn']	;	
	if($resultisn&&!empty($isn))
				{			

	
	$query1="INSERT INTO `reservation` (`reader_id`, `copy_isn`, `reservation_code`, `reservation_date`, `lending_date`, `return_date`, `return_deadline`, `return_admin_id`, `lending_admin_id`) 
values( '{$reader_id}' , '{$isn}', NULL, now(), NULL, NULL,DATE_ADD(now(),INTERVAL 45 DAY), NULL, NULL)";	
	$result= $connection->query($query1);
		
if($result){
$query11="UPDATE `book_copy` SET `is_available` = '0' WHERE `book_copy`.`book_isbn` = '{$isbn}' AND `book_copy`.`isn` = '{$isn}'";
$result11 = $connection->query($query11);
}

if($result&&$result11)
{
$query_code ="select reservation_code  from reservation
where reservation.reader_id = '{$reader_id}' AND `return_date` IS NULL  and return_admin_id IS NULL and `lending_date` is NULL and `return_admin_id` is NULL
AND reservation.copy_isn = '{$isn}' ";		
$resultcode= $connection->query($query_code);

				$found_code = mysqli_fetch_array($resultcode, MYSQLI_ASSOC);
				$reservation_code=$found_code['reservation_code']	;		
$done_flag=1;	
}
else {$done_flag=0;
}	
			
			} else {redirect_to("view_books.php?error_reserve=3");}		
			}
			
if($done_flag==1){
	$url="view_books.php?reservation_code=".$reservation_code."&copy_isn=".$isn;
	redirect_to($url);}
else {redirect_to("view_books.php?error_reserve=2");}	
	

}
else {redirect_to("view_books.php?error_reserve=1");}
}
	
		
?>