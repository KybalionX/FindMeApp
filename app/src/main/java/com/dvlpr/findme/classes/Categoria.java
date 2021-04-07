package com.dvlpr.findme.classes;

public class Categoria {

    String id, categoria, ilustracion;

    public Categoria(String id, String categoria, String ilustracion) {
        this.id = id;
        this.categoria = categoria;
        this.ilustracion = ilustracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIlustracion() {
        return ilustracion;
    }

    public void setIlustracion(String ilustracion) {
        this.ilustracion = ilustracion;
    }
}
