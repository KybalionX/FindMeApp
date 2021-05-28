<?php

require 'connection.php';

$municipio = $_GET["municipio"];

$result = null;

$queryMunicipio = "SELECT * FROM (Municipios, Posts AS pst) INNER JOIN Imagenes ON pst.id = Imagenes.id_post WHERE Municipios.nombre LIKE '%$municipio%' OR pst.descripcion LIKE '%$municipio%' GROUP BY pst.id";

$resultQuery = $conn->query($queryMunicipio);

while($array = mysqli_fetch_array($resultQuery)) {
    
getLikes($array["9"]);
$result[]=$array;
//echo $row["9"];



}

function getLikes($id){
    $conn = $GLOBALS["conn"];
    $queryLikes = "SELECT COUNT(*) AS counterLikes FROM Likes WHERE Likes.id_post = $id";
    $resultLikes = $conn->query($queryLikes);
    while($row = mysqli_fetch_array($resultLikes)) {
        
    $jsonFeed = $row["counterLikes"];
       
    $GLOBALS['array']['likes'] = $jsonFeed;
    
    }
}

$final['feed'] = $result;

echo json_encode($final);

?>