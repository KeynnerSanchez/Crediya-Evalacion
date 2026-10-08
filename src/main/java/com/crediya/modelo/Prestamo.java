package com.crediya.modelo;

import com.crediya.excepciones.ValidacionException;
import java.time.LocalDate;
import java.util.Locale;

public class Prestamo implements Exportable {
    private int id;
    private final int clienteId;
    private final int empleadoId;
    private final double monto;
    private final double interes;
    private final int cuotas;
    private final LocalDate fechaInicio;
    private EstadoPrestamo estado;
    private double totalPagado;

    public Prestamo(int id, int clienteId, int empleadoId, double monto, double interes,
                    int cuotas, LocalDate fechaInicio, EstadoPrestamo estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.empleadoId = empleadoId;
        this.monto = monto;
        this.interes = interes;
        this.cuotas = cuotas;
        this.fechaInicio = fechaInicio;
        this.estado = estado;
    }

    public double calcularTotal() { return monto + (monto * interes / 100); }
    public double calcularCuota() { return calcularTotal() / cuotas; }
    public double getSaldoPendiente() { return Math.max(0, calcularTotal() - totalPagado); }
    public LocalDate getFechaVencimiento() { return fechaInicio.plusMonths(cuotas); }

    public boolean estaVencido() {
        return estado == EstadoPrestamo.PENDIENTE && LocalDate.now().isAfter(getFechaVencimiento());
    }

    public void validarAbono(double abono) throws ValidacionException {
        if (estado == EstadoPrestamo.PAGADO) {
            throw new ValidacionException("El prestamo ya esta pagado.");
        }
        if (abono <= 0) {
            throw new ValidacionException("El abono debe ser mayor a 0.");
        }
        if (abono > getSaldoPendiente() + 0.005) {
            throw new ValidacionException(String.format("El abono supera el saldo pendiente ($%,.2f).", getSaldoPendiente()));
        }
    }

    public void registrarAbono(double abono) throws ValidacionException {
        validarAbono(abono);
        totalPagado += abono;
        if (getSaldoPendiente() <= 0.005) {
            estado = EstadoPrestamo.PAGADO;
        }
    }

    public void cargarPagado(double abono) { totalPagado += abono; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public int getEmpleadoId() { return empleadoId; }
    public double getMonto() { return monto; }
    public double getInteres() { return interes; }
    public int getCuotas() { return cuotas; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("[Prestamo %d] Cliente:%d Empleado:%d | Monto: $%,.2f | Interes: %.1f%% | Total: $%,.2f | %d cuotas de $%,.2f | Saldo: $%,.2f | Inicio: %s | Estado: %s",
                id, clienteId, empleadoId, monto, interes, calcularTotal(), cuotas, calcularCuota(),
                getSaldoPendiente(), fechaInicio, estado);
    }

    @Override
    public String toLinea() {
        return id + ";" + clienteId + ";" + empleadoId + ";" + String.format(Locale.US, "%.2f", monto) + ";"
                + String.format(Locale.US, "%.2f", interes) + ";" + cuotas + ";" + fechaInicio + ";" + estado;
    }
}
