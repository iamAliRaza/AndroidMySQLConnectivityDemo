<?php

require "Connection.php";

$sql_qry="Select city from donor";
$result = $con->query($sql_qry);

while($row = $result->fetch_assoc()) {
        echo $row["city"]."\n";
    }
	
$sql_qryy="Select BloodGroup from donor";
$result2 = $con->query($sql_qryy);

while($row = $result2->fetch_assoc()) {
        echo $row["BloodGroup"]."\n";
    }


?>