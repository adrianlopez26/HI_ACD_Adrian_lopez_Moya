package com.techsolutions;

import com.techsolutions.util.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            if (conn != null) {
                System.out.println("Conexión establecida a la base de datos.");

                boolean exit = false;

                while (!exit) {
                    System.out.println("\n--- Menú de operaciones ---");
                    System.out.println("1. Insertar cliente");
                    System.out.println("2. Insertar producto");
                    System.out.println("3. Registrar venta");
                    System.out.println("4. Ver logs");
                    System.out.println("5. Ver Clientes");
                    System.out.println("6. Ver Productos");
                    System.out.println("7. Insertar detalle de venta");
                    System.out.println("8. Salir");

                    System.out.print("Elige una opción: ");
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer

                    switch (option) {
                        case 1:
                            insertCliente(conn, scanner);
                            break;
                        case 2:
                            insertProducto(conn, scanner);
                            break;
                        case 3:
                            insertVenta(conn, scanner);
                            break;
                        case 4:
                            viewLogs(conn);
                            break;
                        case 5:
                            viewClientes(conn);
                            break;
                        case 6:
                            viewProductos(conn);
                            break;
                        case 7:
                            insertDetalleVenta(conn, scanner);
                            break;
                        case 8:
                            System.out.println("Saliendo...");
                            exit = true;
                            break;
                        default:
                            System.out.println("Opción no válida. Intenta de nuevo.");
                            break;
                    }
                }
            } else {
                System.out.println("Error al conectar con la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                scanner.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Ver clientes
    private static void viewClientes(Connection conn) throws SQLException {
        String query = "SELECT * FROM Cliente";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.printf("%-10s %-20s %-20s %-30s %-15s %-30s\n", "ID", "Nombre", "Apellido", "Correo", "Teléfono", "Dirección");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int idCliente = rs.getInt("ID_Cliente");
                String nombre = rs.getString("Nombre");
                String apellido = rs.getString("Apellido");
                String correo = rs.getString("Correo");
                String telefono = rs.getString("Telefono");
                String direccion = rs.getString("Direccion");

                System.out.printf("%-10d %-20s %-20s %-30s %-15s %-30s\n",
                        idCliente, nombre, apellido, correo, telefono, direccion);
            }
        }
    }

    // Ver productos
    private static void viewProductos(Connection conn) throws SQLException {
        String query = "SELECT * FROM Producto";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.printf("%-10s %-30s %-50s %-10s %-20s\n", "ID", "Nombre", "Descripción", "Precio", "Cantidad en stock");
            System.out.println("-------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int idProducto = rs.getInt("ID_Producto");
                String nombre = rs.getString("Nombre");
                String descripcion = rs.getString("Descripcion");
                double precio = rs.getDouble("Precio");
                int cantidadStock = rs.getInt("Cantidad_Stock");

                System.out.printf("%-10d %-30s %-50s %-10.2f %-20d\n",
                        idProducto, nombre, descripcion, precio, cantidadStock);
            }
        }
    }

    // Insertar detalle de venta
    private static void insertDetalleVenta(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Introduce el ID de la venta: ");
        int idVenta = scanner.nextInt();
        System.out.print("Introduce el ID del producto: ");
        int idProducto = scanner.nextInt();
        System.out.print("Introduce la cantidad de productos vendidos: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        // Obtener el precio del producto
        String queryPrecio = "SELECT Precio FROM Producto WHERE ID_Producto = ?";
        double precio = 0.0;
        try (PreparedStatement stmtPrecio = conn.prepareStatement(queryPrecio)) {
            stmtPrecio.setInt(1, idProducto);
            try (ResultSet rs = stmtPrecio.executeQuery()) {
                if (rs.next()) {
                    precio = rs.getDouble("Precio");
                } else {
                    System.out.println("Producto no encontrado.");
                    return;
                }
            }
        }

        // Calcular el precio total
        double precioTotal = precio * cantidad;

        // Insertar el detalle de venta
        String queryDetalleVenta = "INSERT INTO Detalle_Venta (ID_Venta, ID_Producto, Cantidad, Precio_Total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmtDetalleVenta = conn.prepareStatement(queryDetalleVenta)) {
            stmtDetalleVenta.setInt(1, idVenta);
            stmtDetalleVenta.setInt(2, idProducto);
            stmtDetalleVenta.setInt(3, cantidad);
            stmtDetalleVenta.setDouble(4, precioTotal);
            stmtDetalleVenta.executeUpdate();
            System.out.println("Detalle de venta registrado correctamente.");
        }
    }

    // Insertar cliente
    private static void insertCliente(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Introduce el nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce el apellido del cliente: ");
        String apellido = scanner.nextLine();
        System.out.print("Introduce el correo del cliente: ");
        String correo = scanner.nextLine();
        System.out.print("Introduce el teléfono del cliente: ");
        String telefono = scanner.nextLine();
        System.out.print("Introduce la dirección del cliente: ");
        String direccion = scanner.nextLine();

        String query = "INSERT INTO Cliente (Nombre, Apellido, Correo, Telefono, Direccion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            stmt.setString(3, correo);
            stmt.setString(4, telefono);
            stmt.setString(5, direccion);
            stmt.executeUpdate();
            System.out.println("Cliente insertado correctamente.");
        }
    }

    // Insertar producto
    private static void insertProducto(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Introduce el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce la descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Introduce el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Introduce la cantidad en stock: ");
        int cantidadStock = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        String query = "INSERT INTO Producto (Nombre, Descripcion, Precio, Cantidad_Stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, cantidadStock);
            stmt.executeUpdate();
            System.out.println("Producto insertado correctamente.");
        }
    }

    // Registrar venta
    private static void insertVenta(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Introduce el ID del cliente: ");
        int idCliente = scanner.nextInt();
        System.out.print("Introduce el ID del producto: ");
        int idProducto = scanner.nextInt();
        System.out.print("Introduce la fecha de la venta (yyyy-mm-dd): ");
        String fechaVenta = scanner.next();
        System.out.print("Introduce la cantidad de productos vendidos: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        // Obtener el precio del producto
        String queryPrecio = "SELECT Precio FROM Producto WHERE ID_Producto = ?";
        double precio = 0.0;
        try (PreparedStatement stmtPrecio = conn.prepareStatement(queryPrecio)) {
            stmtPrecio.setInt(1, idProducto);
            try (ResultSet rs = stmtPrecio.executeQuery()) {
                if (rs.next()) {
                    precio = rs.getDouble("Precio");
                } else {
                    System.out.println("Producto no encontrado.");
                    return;
                }
            }
        }

        // Calcular el total
        double total = precio * cantidad;

        // Insertar la venta
        String queryVenta = "INSERT INTO Ventas (ID_Cliente, ID_Producto, Fecha_Venta, Cantidad, Total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmtVenta = conn.prepareStatement(queryVenta)) {
            stmtVenta.setInt(1, idCliente);
            stmtVenta.setInt(2, idProducto);
            stmtVenta.setString(3, fechaVenta);
            stmtVenta.setInt(4, cantidad);
            stmtVenta.setDouble(5, total);
            stmtVenta.executeUpdate();
            System.out.println("Venta registrada correctamente.");
        }
    }

    // Ver los logs
    private static void viewLogs(Connection conn) throws SQLException {
        String query = "SELECT * FROM Logs";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int idLog = rs.getInt("ID_Log");
                String operacion = rs.getString("Operacion");
                String tablaAfectada = rs.getString("Tabla_Afectada");
                String fechaHora = rs.getString("Fecha_Hora");
                String detalles = rs.getString("Detalles");

                System.out.printf("ID: %d | Operación: %s | Tabla: %s | Fecha y Hora: %s | Detalles: %s\n",
                        idLog, operacion, tablaAfectada, fechaHora, detalles);
            }
        }
    }
}
