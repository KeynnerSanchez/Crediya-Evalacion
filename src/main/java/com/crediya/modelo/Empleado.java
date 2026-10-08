package com.crediya.modelo;

import java.util.Locale;

public class Empleado extends Persona {
    private String rol;
    private double salario;

    public Empleado(int id, String nombre, String documento, String rol, String correo, double salario) {
        super(id, nombre, documento, correo);
        this.rol = rol;
        this.salario = salario;
    }

    public String getRol() { return rol; }
    public double getSalario() { return salario; }

    @Override
    public String descripcion() {
        return String.format("[Empleado %d] %s | Doc: %s | Rol: %s | Correo: %s | Salario: $%,.2f",
                getId(), getNombre(), getDocumento(), rol, getCorreo(), salario);
    }

    @Override
    public String toLinea() {
        return getId() + ";" + getNombre() + ";" + getDocumento() + ";" + rol + ";" + getCorreo() + ";"
                + String.format(Locale.US, "%.2f", salario);
    }
}
