package com.crediya.persistencia;

import java.sql.SQLException;
import java.util.List;

public interface Repositorio<T> {
    void guardar(T objeto) throws SQLException;
    List<T> listar() throws SQLException;
}
