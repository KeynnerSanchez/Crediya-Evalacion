package com.crediya.servicio;

import com.crediya.modelo.Cliente;
import com.crediya.modelo.EstadoPrestamo;
import com.crediya.modelo.Pago;
import com.crediya.modelo.Prestamo;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ReporteServicio {

    private final PrestamoServicio prestamoServicio;
    private final ClienteServicio clienteServicio;
    private final PagoServicio pagoServicio;

    public ReporteServicio(PrestamoServicio prestamoServicio, ClienteServicio clienteServicio,
                           PagoServicio pagoServicio) {
        this.prestamoServicio = prestamoServicio;
        this.clienteServicio = clienteServicio;
        this.pagoServicio = pagoServicio;
    }

    public List<Prestamo> prestamosActivos() {
        return prestamoServicio.listar().stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.PENDIENTE)
                .collect(Collectors.toList());
    }

    public List<Prestamo> prestamosVencidos() {
        return prestamoServicio.listar().stream()
                .filter(Prestamo::estaVencido)
                .collect(Collectors.toList());
    }

    public List<Cliente> clientesMorosos() {
        Set<Integer> idsMorosos = prestamosVencidos().stream()
                .map(Prestamo::getClienteId)
                .collect(Collectors.toSet());
        return clienteServicio.listar().stream()
                .filter(c -> idsMorosos.contains(c.getId()))
                .collect(Collectors.toList());
    }

    public double carteraPendiente() {
        return prestamosActivos().stream()
                .mapToDouble(Prestamo::getSaldoPendiente)
                .sum();
    }

    public double totalRecaudado() {
        return pagoServicio.listar().stream()
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    public Map<Integer, Long> prestamosPorEmpleado() {
        return prestamoServicio.listar().stream()
                .collect(Collectors.groupingBy(Prestamo::getEmpleadoId, Collectors.counting()));
    }

    public Optional<Prestamo> prestamoMayorMonto() {
        return prestamoServicio.listar().stream()
                .max(Comparator.comparingDouble(Prestamo::getMonto));
    }
//
    public List<Prestamo> prestamosOrdenadosPorSaldo() {
        return prestamosActivos().stream()
                .sorted(Comparator.comparingDouble(Prestamo::getSaldoPendiente).reversed())
                .collect(Collectors.toList());
    }
}
