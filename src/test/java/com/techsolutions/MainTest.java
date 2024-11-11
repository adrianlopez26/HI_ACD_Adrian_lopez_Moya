package com.techsolutions;

import java.sql.*;
import java.util.Scanner;

public class MainTest {
    private static final String URL = "jdbc:mysql://localhost:3306/bookworld";
    private static final String USER = "root"; // Cambia el usuario si es diferente
    private static final String PASSWORD = "curso"; // Cambia la contraseña si es diferente

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            if (conn != null) {
                System.out.println("Conexión establecida a la base de datos.");
                boolean exit = false;

                while (!exit) {
                    showMenu();
                    System.out.print("Elige una opción: ");
                    int option = scanner.nextInt();
                    scanner.nextLine(); // Limpiar el buffer del Scanner

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
        }
    }

    // Mostrar el menú
    private static void showMenu() {
        System.out.println("\n--- Menú de operaciones ---");
        System.out.println("1. Insertar cliente");
        System.out.println("2. Insertar producto");
        System.out.println("3. Registrar venta");
        System.out.println("4. Ver logs");
        System.out.println("5. Salir");
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
        System.out.print("Introduce el total de la venta: ");
        double total = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        String query = "INSERT INTO Ventas (ID_Cliente, ID_Producto, Fecha_Venta, Cantidad, Total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idCliente);
            stmt.setInt(2, idProducto);
            stmt.setString(3, fechaVenta);
            stmt.setInt(4, cantidad);
            stmt.setDouble(5, total);
            stmt.executeUpdate();
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
