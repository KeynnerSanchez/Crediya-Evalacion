package com.crediya.app;

import com.crediya.persistencia.*;
import com.crediya.servicio.*;

public class Main {

    public static void main(String[] args) {
        try {
            EmpleadoServicio empleados = new EmpleadoServicio(new EmpleadoDAO());
            ClienteServicio clientes = new ClienteServicio(new ClienteDAO());
            PrestamoServicio prestamos = new PrestamoServicio(new PrestamoDAO(), clientes, empleados);
            PagoServicio pagos = new PagoServicio(new PagoDAO(), prestamos);
            ReporteServicio reportes = new ReporteServicio(prestamos, clientes, pagos);

            new Menu(empleados, clientes, prestamos, pagos, reportes).iniciar();
        } catch (Exception e) {
            System.out.println("No se pudo iniciar el sistema: " + e.getMessage());
            System.out.println("Revisa que MySQL este encendido y que el usuario/clave en ConexionBD.java sean correctos.");
        } finally {
            ConexionBD.getInstancia().cerrar();
        }
    }
}
