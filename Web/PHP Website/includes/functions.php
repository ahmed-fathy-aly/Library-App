<?php
	

	function mysqli_prep( $value ) {
		global $connection;
		$magic_quotes_active = get_magic_quotes_gpc();
		$new_enough_php = function_exists( "mysqli_real_escape_string" ); // i.e. PHP >= v4.3.0
		if( $new_enough_php ) { // PHP v4.3.0 or higher
			// undo any magic quote effects so mysqli_real_escape_string can do the work
			if( $magic_quotes_active ) { $value = stripslashes( $value ); }
			$value = mysqli_real_escape_string( $connection,$value );
		} else { // before PHP v4.3.0
			// if magic quotes aren't already on then add slashes manually
			if( !$magic_quotes_active ) { $value = addslashes( $value ); }
			// if magic quotes are active, then the slashes already exist
		}
		return $value;
	}

	function redirect_to( $location = NULL ) {
		if ($location != NULL) {
			header("Location: {$location}");
			exit;
		}
	}

	function confirm_query($result_set) {
		global $connection;
		if (!$result_set) {
			die("Database query failed: " . $connection->error);
		}
	}

?>