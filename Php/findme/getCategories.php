<?php

require 'connection.php';

$query = "SELECT * FROM `Categorias`";

$result = $conn->query($query);

while ($row = mysqli_fetch_array($result)) {

$feed[]=$row;

}
$jsonFeed['categorias']=$feed;


echo json_encode($jsonFeed);

$conn->close();

?>