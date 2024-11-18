package com.techsolutions.dao;

import com.techsolutions.model.Producto;
import com.techsolutions.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    public void agregarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO Producto (Nombre, Descripcion, Precio, Cantidad_Stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidadStock());
            pstmt.executeUpdate();
        }
    }

    public List<Producto> obtenerProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM Producto";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("ID_Producto"));
                producto.setNombre(rs.getString("Nombre"));
                producto.setDescripcion(rs.getString("Descripcion"));
                producto.setPrecio(rs.getDouble("Precio"));
                producto.setCantidadStock(rs.getInt("Cantidad_Stock"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE Producto SET Nombre = ?, Descripcion = ?, Precio = ?, Cantidad_Stock = ? WHERE ID_Producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidadStock());
            pstmt.setInt(5, producto.getId());
            pstmt.executeUpdate();
        }
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM Producto WHERE ID_Producto = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Producto obtenerProductoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Producto WHERE ID_Producto = ?";
        Producto producto = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setId(rs.getInt("ID_Producto"));
                    producto.setNombre(rs.getString("Nombre"));
                    producto.setDescripcion(rs.getString("Descripcion"));
                    producto.setPrecio(rs.getDouble("Precio"));
                    producto.setCantidadStock(rs.getInt("Cantidad_Stock"));
                }
            }
        }
        return producto;
    }
}
