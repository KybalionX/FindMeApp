<?php

require 'connection.php';

$idPost = $_GET['idPost'];

$query = "SELECT * FROM Comentarios INNER JOIN Usuarios ON Comentarios.id_usuario=Usuarios.id  WHERE id_post = '$idPost'";


$comentarios = null;

$result = $conn->query($query);

while ($row = mysqli_fetch_array($result)) {

$comentarios[]=$row;

}
$jsonFeed['comentarios']=$comentarios;


echo json_encode($jsonFeed);

$conn->close();

?>