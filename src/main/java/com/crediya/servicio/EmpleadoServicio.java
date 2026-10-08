package com.crediya.servicio;

import com.crediya.excepciones.*;
import com.crediya.modelo.Empleado;
import com.crediya.persistencia.ArchivoTexto;
import com.crediya.persistencia.Repositorio;
import com.crediya.util.Validador;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoServicio {

    private static final String ARCHIVO = "empleados.txt";

    private final Repositorio<Empleado> repositorio;
    private final List<Empleado> empleados;

    public EmpleadoServicio(Repositorio<Empleado> repositorio) throws SQLException, CrediYaException {
        this.repositorio = repositorio;
        this.empleados = new ArrayList<>(repositorio.listar());
        ArchivoTexto.guardar(ARCHIVO, empleados);
    }

    public Empleado registrar(String nombre, String documento, String rol, String correo, double salario)
            throws SQLException, CrediYaException {
        Validador.textoObligatorio(nombre, "nombre");
        Validador.documento(documento);
        Validador.textoObligatorio(rol, "rol");
        Validador.correo(correo);
        Validador.positivo(salario, "salario");

        boolean repetido = empleados.stream().anyMatch(e -> e.getDocumento().equals(documento));
        if (repetido) {
            throw new ValidacionException("Ya existe un empleado con ese documento.");
        }

        Empleado empleado = new Empleado(0, nombre.trim(), documento, rol.trim(), correo.trim(), salario);
        repositorio.guardar(empleado);
        empleados.add(empleado);
        ArchivoTexto.guardar(ARCHIVO, empleados);
        return empleado;
    }

    public List<Empleado> listar() {
        return new ArrayList<>(empleados);
    }

    public Empleado buscarPorId(int id) throws RecursoNoEncontradoException {
        return empleados.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un empleado con id " + id));
    }
}
