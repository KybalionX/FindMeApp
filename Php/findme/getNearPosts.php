<?php
include("connection.php");
$lat = "3.107685";
$lon = "101.7624521";

$sql="SELECT ((ACOS(SIN($lat * PI() / 180) * SIN(lat * PI() / 180) + COS($lat * PI() / 180) * COS(lat * PI() / 180) * COS(($lon – lon) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS 'distance' FROM loc_coordinate HAVING 'distance'<='10' ORDER BY 'distance' ASC";

$resultFeed = $conn->query($sql);

$row=0;

while ($row = mysqli_fetch_array($resultFeed))
{
echo $row['place'];
}

?>