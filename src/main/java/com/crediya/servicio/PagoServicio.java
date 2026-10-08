package com.crediya.servicio;

import com.crediya.excepciones.*;
import com.crediya.modelo.EstadoPrestamo;
import com.crediya.modelo.Pago;
import com.crediya.modelo.Prestamo;
import com.crediya.persistencia.ArchivoTexto;
import com.crediya.persistencia.Repositorio;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PagoServicio {

    private static final String ARCHIVO = "pagos.txt";

    private final Repositorio<Pago> repositorio;
    private final PrestamoServicio prestamoServicio;
    private final List<Pago> pagos;

    public PagoServicio(Repositorio<Pago> repositorio, PrestamoServicio prestamoServicio)
            throws SQLException, CrediYaException {
        this.repositorio = repositorio;
        this.prestamoServicio = prestamoServicio;
        this.pagos = new ArrayList<>(repositorio.listar());

        for (Pago pago : pagos) {
            try {
                prestamoServicio.buscarPorId(pago.getPrestamoId()).cargarPagado(pago.getMonto());
            } catch (RecursoNoEncontradoException e) {
            }
        }
        ArchivoTexto.guardar(ARCHIVO, pagos);
    }

    public Pago registrar(int prestamoId, double monto) throws SQLException, CrediYaException {
        Prestamo prestamo = prestamoServicio.buscarPorId(prestamoId);
        prestamo.validarAbono(monto);

        Pago pago = new Pago(0, prestamoId, LocalDate.now(), monto);
        repositorio.guardar(pago);
        pagos.add(pago);

        prestamo.registrarAbono(monto);
        if (prestamo.getEstado() == EstadoPrestamo.PAGADO) {
            prestamoServicio.persistirEstado(prestamo);
        }
        ArchivoTexto.guardar(ARCHIVO, pagos);
        return pago;
    }

    public List<Pago> historial(int prestamoId) throws RecursoNoEncontradoException {
        prestamoServicio.buscarPorId(prestamoId);
        return pagos.stream()
                .filter(p -> p.getPrestamoId() == prestamoId)
                .sorted(Comparator.comparing(Pago::getFechaPago).thenComparing(Pago::getId))
                .collect(Collectors.toList());
    }

    public List<Pago> listar() {
        return new ArrayList<>(pagos);
    }
}
