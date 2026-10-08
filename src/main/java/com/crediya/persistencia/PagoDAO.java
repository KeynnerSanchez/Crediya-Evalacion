package com.crediya.persistencia;

import com.crediya.modelo.Pago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO implements Repositorio<Pago> {

    @Override
    public void guardar(Pago p) throws SQLException {
        String sql = "INSERT INTO pagos (prestamo_id, fecha_pago, monto) VALUES (?, ?, ?)";
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getPrestamoId());
            ps.setDate(2, Date.valueOf(p.getFechaPago()));
            ps.setDouble(3, p.getMonto());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Pago> listar() throws SQLException {
        List<Pago> lista = new ArrayList<>();
        Connection con = ConexionBD.getInstancia().getConexion();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM pagos ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pago(rs.getInt("id"), rs.getInt("prestamo_id"),
                        rs.getDate("fecha_pago").toLocalDate(), rs.getDouble("monto")));
            }
        }
        return lista;
    }
}
