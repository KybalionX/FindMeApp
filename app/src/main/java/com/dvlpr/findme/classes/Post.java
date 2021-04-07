package com.dvlpr.findme.classes;

import java.util.ArrayList;

public class Post {

    String Id;
    String Imagen;
    String Nombre;
    String Categoria;
    ArrayList<Image> Imagenes;
    String Likes;
    String Comentarios;
    String Descripcion;
    Boolean Liked;
    String Ciudad;
    String Departamento;
    String Pais;
    String Fecha;

    public Post(String id, String imagen, String nombre, String categoria, ArrayList<Image> imagenes, String likes, String comentarios, String descripcion, boolean liked, String ciudad, String departamento, String pais, String fecha) {
        Id = id;
        Imagen = imagen;
        Nombre = nombre;
        Categoria = categoria;
        Imagenes = imagenes;
        Likes = likes;
        Comentarios = comentarios;
        Descripcion = descripcion;
        Liked = liked;
        Ciudad = ciudad;
        Departamento = departamento;
        Pais = pais;
        Fecha = fecha;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(String departamento) {
        Departamento = departamento;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public Boolean getLiked() {
        return Liked;
    }

    public void setLiked(Boolean liked) {
        Liked = liked;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public ArrayList<Image> getImagenes() {
        return Imagenes;
    }

    public void setImagenes(ArrayList<Image> imagenes) {
        Imagenes = imagenes;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}
