package com.dvlpr.findme.classes;

public class PostUser {

    String id;
    String imagenPrincipal;
    int Imagenes;

    public PostUser(String id, String imagenPrincipal, int imagenes) {
        this.id = id;
        this.imagenPrincipal = imagenPrincipal;
        Imagenes = imagenes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagenPrincipal() {
        return imagenPrincipal;
    }

    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    public int getImagenes() {
        return Imagenes;
    }

    public void setImagenes(int imagenes) {
        Imagenes = imagenes;
    }
}
