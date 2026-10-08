package com.crediya.modelo;

import java.time.LocalDate;
import java.util.Locale;

public class Pago implements Exportable {
    private int id;
    private final int prestamoId;
    private final LocalDate fechaPago;
    private final double monto;

    public Pago(int id, int prestamoId, LocalDate fechaPago, double monto) {
        this.id = id;
        this.prestamoId = prestamoId;
        this.fechaPago = fechaPago;
        this.monto = monto;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPrestamoId() { return prestamoId; }
    public LocalDate getFechaPago() { return fechaPago; }
    public double getMonto() { return monto; }

    @Override
    public String toString() {
        return String.format("[Pago %d] Prestamo:%d | Fecha: %s | Monto: $%,.2f", id, prestamoId, fechaPago, monto);
    }

    @Override
    public String toLinea() {
        return id + ";" + prestamoId + ";" + fechaPago + ";" + String.format(Locale.US, "%.2f", monto);
    }
}
