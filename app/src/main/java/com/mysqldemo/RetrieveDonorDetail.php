<?php

require"Connection.php";

$blood = $_POST["DonorBlood"];
$city = $_POST["DonorCity"];

$sql_qry = "SELECT * FROM donor where BloodGroup='$blood' and city = '$city'";
$result = $con->query($sql_qry);

while($row = $result->fetch_assoc()) {
        echo $row["DName"]."\n"
		.$row["BloodGroup"]."\n"
		.$row["MobileNo"]."\n"
		.$row["Age"]."\n"
		.$row["City"]."\n"
		.$row["Address"];
    }

?>