package com.crediya.servicio;

import com.crediya.excepciones.*;
import com.crediya.modelo.Cliente;
import com.crediya.persistencia.ArchivoTexto;
import com.crediya.persistencia.Repositorio;
import com.crediya.util.Validador;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteServicio {

    private static final String ARCHIVO = "clientes.txt";

    private final Repositorio<Cliente> repositorio;
    private final List<Cliente> clientes;

    public ClienteServicio(Repositorio<Cliente> repositorio) throws SQLException, CrediYaException {
        this.repositorio = repositorio;
        this.clientes = new ArrayList<>(repositorio.listar());
        ArchivoTexto.guardar(ARCHIVO, clientes);
    }

    public Cliente registrar(String nombre, String documento, String correo, String telefono)
            throws SQLException, CrediYaException {
        Validador.textoObligatorio(nombre, "nombre");
        Validador.documento(documento);
        Validador.correo(correo);
        Validador.telefono(telefono);

        boolean repetido = clientes.stream().anyMatch(c -> c.getDocumento().equals(documento));
        if (repetido) {
            throw new ValidacionException("Ya existe un cliente con ese documento.");
        }

        Cliente cliente = new Cliente(0, nombre.trim(), documento, correo.trim(), telefono);
        repositorio.guardar(cliente);
        clientes.add(cliente);
        ArchivoTexto.guardar(ARCHIVO, clientes);
        return cliente;
    }

    public List<Cliente> listar() {
        return new ArrayList<>(clientes);
    }

    public Cliente buscarPorId(int id) throws RecursoNoEncontradoException {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un cliente con id " + id));
    }
}
