<?php
require("constants.php");

// 1. Create a database connection
$connection = mysqli_connect(DB_SERVER,DB_USER,DB_PASS);
if ($connection->connect_errno) {
	die("Database connection failed: " . $connection->connect_errno);
}

// 2. Select a database to use 
$db_select = mysqli_select_db($connection,DB_NAME);
if (!$connection) {
	die("Database selection failed: " . $connection->error);
}
?>
