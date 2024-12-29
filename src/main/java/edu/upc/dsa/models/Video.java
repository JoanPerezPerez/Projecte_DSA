package edu.upc.dsa.models;

public class Video {
    String descripcion;
    String url;

    public Video() {

    }
    public Video(String descripcion, String url) {
        this.descripcion = descripcion;
        this.url = url;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Video{ descripcion= " + descripcion + ", url= " + url + " }";
    }
}
