<?php

	$conn = mysql_connect('localhost','karthik','th1sisthepassword!!');
	
	$sql = "select player_order,phone_number,hit,acceleration,direction from player where modified=1";

	mysql_select_db('pingpong');

	$result = mysql_query($sql);
	$num = mysql_num_rows($result);
	if($num==0){
		return "NO";
	}
	$player = array();

	$i=0;
	while($row=mysql_fetch_assoc($result)){
		if($row["player_order"]==0)
			$player["player1"] = array("hit"=>$row["hit"],"acceleration"=>$row["acceleration"],"imei"=>$row["phone_number"],"direction"=>$row["direction"]);
		else
			$player["player2"] = array("hit"=>$row["hit"],"acceleration"=>$row["acceleration"],"imei"=>$row["phone_number"],"direction"=>$row["direction"]);
	
/*		echo '<?xml version="1.0"?>';
		echo "<player>";
		echo "<hit>".$row["hit"]."</hit>";
		echo "<acceleration>".$row["acceleration"]."</acceleration>";
		echo "<imei>".$row["phone_number"]."</imei>";
		echo "<direction>".$row["direction"]."</direction>";
		echo "</player>";
		echo "</xml>";*/
		mysql_query("update player set modified=0 where phone_number=\"".$row["phone_number"]."\"");
		$i++;
	}
	
	print_r(json_encode($player));
?>
