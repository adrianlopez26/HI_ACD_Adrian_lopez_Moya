package com.techsolutions.service;

import com.techsolutions.dao.ClienteDAO;
import com.techsolutions.dao.ProductoDAO;
import com.techsolutions.model.Cliente;
import com.techsolutions.model.Producto;
import com.techsolutions.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarInformeProductos() {
        try {
            List<Producto> productos = productoDAO.obtenerProductos();
            System.out.println("Listado de Productos:");
            for (Producto producto : productos) {
                System.out.println(producto);
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
            while (rs.next()) {
                int idVenta = rs.getInt("ID_Venta");
                String cliente = rs.getString("Cliente");
                String producto = rs.getString("Producto");
                Date fechaVenta = rs.getDate("Fecha_Venta");
                int cantidad = rs.getInt("Cantidad");
                double total = rs.getDouble("Total");
                System.out.printf("ID Venta: %d | Cliente: %s | Producto: %s | Fecha: %s | Cantidad: %d | Total: %.2f\n",
                        idVenta, cliente, producto, fechaVenta, cantidad, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
