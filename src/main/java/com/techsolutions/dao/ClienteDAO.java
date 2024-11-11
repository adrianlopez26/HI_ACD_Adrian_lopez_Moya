package com.techsolutions.dao;

import com.techsolutions.model.Cliente;
import com.techsolutions.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente (Nombre, Apellido, Correo, Telefono, Direccion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getCorreo());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.executeUpdate();
        }
    }

    public List<Cliente> obtenerClientes() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("ID_Cliente"));
                cliente.setNombre(rs.getString("Nombre"));
                cliente.setApellido(rs.getString("Apellido"));
                cliente.setCorreo(rs.getString("Correo"));
                cliente.setTelefono(rs.getString("Telefono"));
                cliente.setDireccion(rs.getString("Direccion"));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    // Métodos para actualizar y eliminar clientes

    public void actualizarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET Nombre = ?, Apellido = ?, Correo = ?, Telefono = ?, Direccion = ? WHERE ID_Cliente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getCorreo());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.setInt(6, cliente.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE ID_Cliente = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
