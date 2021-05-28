<?php

require 'connection.php';

$feed = null;

$Category = $_GET['categoria'];

$query = "SELECT * FROM (Posts AS pst) INNER JOIN Imagenes ON pst.id = Imagenes.id_post WHERE pst.categoria = $Category";

$result = $conn->query($query);

while ($hiadica = mysqli_fetch_array($result)) {
    
getLikes($hiadica["id_post"]);

$feed[]=$hiadica;

}
$jsonFeed['feed']=$feed;

function getLikes($id){
    $conn = $GLOBALS["conn"];
    $queryLikes = "SELECT COUNT(*) AS counterLikes FROM Likes WHERE Likes.id_post = $id";
    $resultLikes = $conn->query($queryLikes);
    while($row = mysqli_fetch_array($resultLikes)) {
        
    $jsonFeed = $row["counterLikes"];
       
    $GLOBALS['hiadica']['likes'] = $jsonFeed;
    
    }
}


echo json_encode($jsonFeed);

?>