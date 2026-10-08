package com.crediya.modelo;

public abstract class Persona implements Exportable {
    private int id;
    private String nombre;
    private String documento;
    private String correo;

    public Persona(int id, String nombre, String documento, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getCorreo() { return correo; }

    public abstract String descripcion();

    @Override
    public String toString() {
        return descripcion();
    }
}
