package com.crediya.persistencia;

import com.crediya.modelo.EstadoPrestamo;
import com.crediya.modelo.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO implements PrestamoRepositorio {

    @Override
    public void guardar(Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamos (cliente_id, empleado_id, monto, interes, cuotas, fecha_inicio, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getClienteId());
            ps.setInt(2, p.getEmpleadoId());
            ps.setDouble(3, p.getMonto());
            ps.setDouble(4, p.getInteres());
            ps.setInt(5, p.getCuotas());
            ps.setDate(6, Date.valueOf(p.getFechaInicio()));
            ps.setString(7, p.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Prestamo> listar() throws SQLException {
        List<Prestamo> lista = new ArrayList<>();
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM prestamos ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Prestamo(rs.getInt("id"), rs.getInt("cliente_id"), rs.getInt("empleado_id"),
                        rs.getDouble("monto"), rs.getDouble("interes"), rs.getInt("cuotas"),
                        rs.getDate("fecha_inicio").toLocalDate(),
                        EstadoPrestamo.valueOf(rs.getString("estado").toUpperCase())));
            }
        }
        return lista;
    }

    @Override
    public void actualizarEstado(Prestamo p) throws SQLException {
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement("UPDATE prestamos SET estado = ? WHERE id = ?")) {
            ps.setString(1, p.getEstado().name());
            ps.setInt(2, p.getId());
            ps.executeUpdate();
        }
    }
}
