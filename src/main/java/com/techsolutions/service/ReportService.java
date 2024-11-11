package com.techsolutions.service;

import com.techsolutions.dao.ClienteDAO;
import com.techsolutions.dao.ProductoDAO;
import com.techsolutions.model.Cliente;
import com.techsolutions.model.Producto;
import java.sql.SQLException;
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
}
