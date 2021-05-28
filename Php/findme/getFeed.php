<?php

require 'connection.php';

$id_usuario = $_GET['id_usuario'];


$feed = null;

$queryFeed = "SELECT * FROM (Posts, Municipios) INNER JOIN Usuarios ON Posts.usuario = Usuarios.id INNER JOIN Categorias ON Posts.categoria = Categorias.id INNER JOIN `Ubicaciones` ON Municipios.id = Ubicaciones.id INNER JOIN Departamentos ON Municipios.id = Departamentos.id INNER JOIN Coordenadas ON Ubicaciones.id = Coordenadas.id GROUP BY (Posts.id)";

$resultFeed = $conn->query($queryFeed);

$row=0;

while ($row = mysqli_fetch_array($resultFeed)) {

//echo $row[0];
//echo ", ";
getComentarios($row[0]);
getLikes($row[0]);
getImages($row[0]);
$feed[]=$row;

}



$jsonFeed['feed']=$feed;

function getComentarios($id){
    $conn = $GLOBALS["conn"];
    $queryLikes = "SELECT COUNT(*) AS counter FROM `Comentarios` WHERE id_post = $id";
    
    $resultLikes = $conn->query($queryLikes);
    
    $jsonLikes=null;
    
    while($rowLikes = mysqli_fetch_array($resultLikes)){
        $jsonLikes[] = $rowLikes["counter"];
        $GLOBALS["row"]['counterComentarios'] = $jsonLikes[0];
    }
    
    
}


function getLikes($id){
    $conn = $GLOBALS["conn"];
    $queryLikes = "SELECT COUNT(*) AS counter FROM `Likes` WHERE id_post = $id";
    
    $resultLikes = $conn->query($queryLikes);
    
    $jsonLikes=null;
    
    while($rowLikes = mysqli_fetch_array($resultLikes)){
        $jsonLikes[] = $rowLikes["counter"];
        $GLOBALS["row"]['countLikes'] = $jsonLikes[0];
    }
    
    
}

function getImages($id){
    
    $conn = $GLOBALS["conn"];
    $queryImagenes = "SELECT * FROM `Imagenes` WHERE id_post = $id";
    

    $resultImagenes = $conn->query($queryImagenes);
    while ($rowImage = mysqli_fetch_array($resultImagenes)) {

        $jsonFeed[] = $rowImage[2];
       
        $GLOBALS["row"]['images'] = $jsonFeed;
        
        
    }
    
}

$id_usuario = $_GET['id_usuario'];

$query = "SELECT * FROM `Likes` WHERE id_usuario = $id_usuario";

$result = $conn->query($query);

$feedLikes = null;

while ($row = mysqli_fetch_array($result)) {

$feedLikes[]=$row;

}
$jsonFeed['likes']=$feedLikes;


$conn->close();


echo json_encode($jsonFeed);

?>