package com.crediya.servicio;

import com.crediya.excepciones.*;
import com.crediya.modelo.EstadoPrestamo;
import com.crediya.modelo.Prestamo;
import com.crediya.persistencia.ArchivoTexto;
import com.crediya.persistencia.PrestamoRepositorio;
import com.crediya.util.Validador;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PrestamoServicio {

    private static final String ARCHIVO = "prestamos.txt";

    private final PrestamoRepositorio repositorio;
    private final ClienteServicio clienteServicio;
    private final EmpleadoServicio empleadoServicio;
    private final List<Prestamo> prestamos;

    public PrestamoServicio(PrestamoRepositorio repositorio, ClienteServicio clienteServicio,
                            EmpleadoServicio empleadoServicio) throws SQLException, CrediYaException {
        this.repositorio = repositorio;
        this.clienteServicio = clienteServicio;
        this.empleadoServicio = empleadoServicio;
        this.prestamos = new ArrayList<>(repositorio.listar());
        ArchivoTexto.guardar(ARCHIVO, prestamos);
    }

    public Prestamo crear(int clienteId, int empleadoId, double monto, double interes, int cuotas)
            throws SQLException, CrediYaException {
        clienteServicio.buscarPorId(clienteId);
        empleadoServicio.buscarPorId(empleadoId);
        Validador.positivo(monto, "monto");
        if (interes < 0) {
            throw new ValidacionException("El interes no puede ser negativo.");
        }
        if (cuotas < 1 || cuotas > 60) {
            throw new ValidacionException("Las cuotas deben estar entre 1 y 60.");
        }

        Prestamo prestamo = new Prestamo(0, clienteId, empleadoId, monto, interes, cuotas,
                LocalDate.now(), EstadoPrestamo.PENDIENTE);
        repositorio.guardar(prestamo);
        prestamos.add(prestamo);
        ArchivoTexto.guardar(ARCHIVO, prestamos);
        return prestamo;
    }

    public void cambiarEstado(int prestamoId, EstadoPrestamo nuevoEstado) throws SQLException, CrediYaException {
        Prestamo prestamo = buscarPorId(prestamoId);
        if (nuevoEstado == EstadoPrestamo.PAGADO && prestamo.getSaldoPendiente() > 0.005) {
            throw new ValidacionException(String.format(
                    "No se puede marcar como PAGADO: aun hay saldo pendiente de $%,.2f.", prestamo.getSaldoPendiente()));
        }
        prestamo.setEstado(nuevoEstado);
        persistirEstado(prestamo);
    }

    public void persistirEstado(Prestamo prestamo) throws SQLException, CrediYaException {
        repositorio.actualizarEstado(prestamo);
        ArchivoTexto.guardar(ARCHIVO, prestamos);
    }

    public List<Prestamo> listar() {
        return new ArrayList<>(prestamos);
    }

    public List<Prestamo> listarPorCliente(int clienteId) throws RecursoNoEncontradoException {
        clienteServicio.buscarPorId(clienteId);
        return prestamos.stream()
                .filter(p -> p.getClienteId() == clienteId)
                .collect(Collectors.toList());
    }

    public Prestamo buscarPorId(int id) throws RecursoNoEncontradoException {
        return prestamos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un prestamo con id " + id));
    }
}
