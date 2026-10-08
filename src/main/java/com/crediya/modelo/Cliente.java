package com.crediya.modelo;

public class Cliente extends Persona {
    private String telefono;

    public Cliente(int id, String nombre, String documento, String correo, String telefono) {
        super(id, nombre, documento, correo);
        this.telefono = telefono;
    }

    public String getTelefono() { return telefono; }

    @Override
    public String descripcion() {
        return String.format("[Cliente %d] %s | Doc: %s | Correo: %s | Tel: %s",
                getId(), getNombre(), getDocumento(), getCorreo(), telefono);
    }

    @Override
    public String toLinea() {
        return getId() + ";" + getNombre() + ";" + getDocumento() + ";" + getCorreo() + ";" + telefono;
    }
}
