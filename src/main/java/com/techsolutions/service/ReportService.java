package com.techsolutions.service;

import com.techsolutions.dao.ClienteDAO;
import com.techsolutions.dao.ProductoDAO;
import com.techsolutions.model.Cliente;
import com.techsolutions.model.Producto;
import com.techsolutions.util.DBConnection;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class ReportService {
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;

    public ReportService() {
        this.clienteDAO = new ClienteDAO();
        this.productoDAO = new ProductoDAO();
    }

    public void mostrarInformeClientes() {
        try {
            List<Cliente> clientes = clienteDAO.obtenerClientes();
            System.out.println("Listado de Clientes:");
            System.out.printf("%-10s %-20s %-20s %-30s %-15s %-30s\n", "ID", "Nombre", "Apellido", "Correo", "Teléfono", "Dirección");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            for (Cliente cliente : clientes) {
                System.out.printf("%-10d %-20s %-20s %-30s %-15s %-30s\n",
                        cliente.getId(), cliente.getNombre(), cliente.getApellido(), cliente.getCorreo(), cliente.getTelefono(), cliente.getDireccion());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarInformeProductos() {
        try {
            List<Producto> productos = productoDAO.obtenerProductos();
            System.out.println("Listado de Productos:");
            System.out.printf("%-10s %-30s %-50s %-10s %-10s\n", "ID", "Nombre", "Descripción", "Precio", "Stock");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            for (Producto producto : productos) {
                System.out.printf("%-10d %-30s %-50s %-10.2f %-10d\n",
                        producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio(), producto.getCantidadStock());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarCliente(Cliente cliente) {
        try {
            clienteDAO.agregarCliente(cliente);
            System.out.println("Cliente agregado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void agregarProducto(Producto producto) {
        try {
            productoDAO.agregarProducto(producto);
            System.out.println("Producto agregado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarCliente(Cliente cliente) {
        try {
            clienteDAO.actualizarCliente(cliente);
            System.out.println("Cliente actualizado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarProducto(Producto producto) {
        try {
            productoDAO.actualizarProducto(producto);
            System.out.println("Producto actualizado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarCliente(int id) {
        try {
            clienteDAO.eliminarCliente(id);
            System.out.println("Cliente eliminado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarProducto(int id) {
        try {
            productoDAO.eliminarProducto(id);
            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarDetallesVentas() {
        String sql = "SELECT v.ID_Venta, c.Nombre AS Cliente, p.Nombre AS Producto, v.Fecha_Venta, v.Cantidad, v.Total " +
                "FROM Ventas v " +
                "JOIN Cliente c ON v.ID_Cliente = c.ID_Cliente " +
                "JOIN Producto p ON v.ID_Producto = p.ID_Producto";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Detalles de Ventas:");
            System.out.printf("%-10s %-20s %-30s %-15s %-10s %-10s\n", "ID Venta", "Cliente", "Producto", "Fecha", "Cantidad", "Total");
            System.out.println("---------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int idVenta = rs.getInt("ID_Venta");
                String cliente = rs.getString("Cliente");
                String producto = rs.getString("Producto");
                Date fechaVenta = rs.getDate("Fecha_Venta");
                int cantidad = rs.getInt("Cantidad");
                double total = rs.getDouble("Total");
                System.out.printf("%-10d %-20s %-30s %-15s %-10d %-10.2f\n",
                        idVenta, cliente, producto, fechaVenta, cantidad, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void realizarVenta(int idCliente, int idProducto, int cantidad) {
        String sqlVenta = "INSERT INTO Ventas (ID_Cliente, ID_Producto, Fecha_Venta, Cantidad, Total) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO Detalle_Venta (ID_Venta, ID_Producto, Cantidad, Precio_Total) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE Producto SET Cantidad_Stock = Cantidad_Stock - ? WHERE ID_Producto = ?";
        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;
        PreparedStatement pstmtUpdateStock = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Obtener el precio del producto
            Producto producto = productoDAO.obtenerProductoPorId(idProducto);
            double total = producto.getPrecio() * cantidad;

            // Insertar en la tabla Ventas
            pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setInt(1, idCliente);
            pstmtVenta.setInt(2, idProducto);
            pstmtVenta.setDate(3, new java.sql.Date(new Date().getTime()));
            pstmtVenta.setInt(4, cantidad);
            pstmtVenta.setDouble(5, total);
            pstmtVenta.executeUpdate();

            // Obtener el ID de la venta generada
            rs = pstmtVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rs.next()) {
                idVenta = rs.getInt(1);
            }

            // Insertar en la tabla Detalle_Venta
            pstmtDetalle = conn.prepareStatement(sqlDetalle);
            pstmtDetalle.setInt(1, idVenta);
            pstmtDetalle.setInt(2, idProducto);
            pstmtDetalle.setInt(3, cantidad);
            pstmtDetalle.setDouble(4, total);
            pstmtDetalle.executeUpdate();

            // Actualizar el stock del producto
            pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock);
            pstmtUpdateStock.setInt(1, cantidad);
            pstmtUpdateStock.setInt(2, idProducto);
            pstmtUpdateStock.executeUpdate();

            conn.commit();
            System.out.println("Venta realizada correctamente.");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtVenta != null) pstmtVenta.close();
                if (pstmtDetalle != null) pstmtDetalle.close();
                if (pstmtUpdateStock != null) pstmtUpdateStock.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void mostrarProductosDisponibles() {
        try {
            List<Producto> productos = productoDAO.obtenerProductos();
            System.out.println("Productos disponibles:");
            for (Producto producto : productos) {
                System.out.printf("ID: %d | Nombre: %s | Descripción: %s | Precio: %.2f\n",
                        producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
