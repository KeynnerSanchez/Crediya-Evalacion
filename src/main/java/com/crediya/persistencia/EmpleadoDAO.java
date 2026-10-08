package com.crediya.persistencia;

import com.crediya.modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements Repositorio<Empleado> {

    @Override
    public void guardar(Empleado e) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, documento, rol, correo, salario) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDocumento());
            ps.setString(3, e.getRol());
            ps.setString(4, e.getCorreo());
            ps.setDouble(5, e.getSalario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    e.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Empleado> listar() throws SQLException {
        List<Empleado> lista = new ArrayList<>();
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM empleados ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Empleado(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"),
                        rs.getString("rol"), rs.getString("correo"), rs.getDouble("salario")));
            }
        }
        return lista;
    }
}
