package com.dvlpr.findme.classes;

public class Comentario {

    String imagenUsuario;
    String NombreUsuario;
    String Comentario;

    public Comentario(String imagenUsuario, String nombreUsuario, String comentario) {
        this.imagenUsuario = imagenUsuario;
        NombreUsuario = nombreUsuario;
        Comentario = comentario;
    }

    public String getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(String imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }
}
