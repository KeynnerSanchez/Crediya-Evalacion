package com.crediya.persistencia;

import com.crediya.modelo.Prestamo;
import java.sql.SQLException;

public interface PrestamoRepositorio extends Repositorio<Prestamo> {
    void actualizarEstado(Prestamo prestamo) throws SQLException;
}
