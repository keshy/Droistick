<?php

	$phone_number = $_POST["userid"];
	$up = $_POST["dirn"];
	$hit  = $_POST["hit"];
	$accel = $_POST["mag"];
	$change = $_POST["change"];
	$qid = $_POST["qid"];
	$player_order = $_POST["pnumber"];

	if($qid=="INSERT"){
		$sql = "insert into player(phone_number,player_order) values (\"$phone_number\",$player_order)";
	}
	/*$sql = "update player set direction=$up,hit=$hit,acceleration=$accel,modified=$change";
	*/
	else{
		$sql = "update player set phone_number=\"$phone_number\",direction=$up,hit=$hit,modified=$change,acceleration=$accel where phone_number=\"$phone_number\"";
	}

	$conn=mysql_connect('localhost','karthik','th1sisthepassword!!');
	if(!$conn){
		echo "Nope!";
	}

	if(!(mysql_select_db('pingpong',$conn))){
		echo "Fail!";
	}
	
	$result = mysql_query($sql);

	if(!$result){
		echo "Fail too!";
	}

	mysql_close($conn);
?>
