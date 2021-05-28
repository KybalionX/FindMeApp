<?php

require 'connection.php';

$id_usuario = $_GET['id_usuario'];

$query = "SELECT * FROM `Likes` WHERE id_usuario = $id_usuario";

$result = $conn->query($query);

while ($row = mysqli_fetch_array($result)) {

$feed[]=$row;

}
$jsonFeed['likes']=$feed;


echo json_encode($jsonFeed);

$conn->close();

?>