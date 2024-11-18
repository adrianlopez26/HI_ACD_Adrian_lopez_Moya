package com.techsolutions;

import com.techsolutions.service.ReportService;
import com.techsolutions.model.Cliente;
import com.techsolutions.model.Producto;
import com.techsolutions.util.DBConnection;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection conn = null;
        ReportService reportService = new ReportService();

        try {
            conn = DBConnection.getConnection();

            if (conn != null) {
                System.out.println("Conexión establecida a la base de datos.");

                boolean exit = false;

                while (!exit) {
                    System.out.println("\n--- Menú de operaciones ---");
                    System.out.println("1. Realizar venta");
                    System.out.println("2. Insertar cliente");
                    System.out.println("3. Insertar producto");
                    System.out.println("4. Actualizar cliente");
                    System.out.println("5. Actualizar producto");
                    System.out.println("6. Eliminar cliente");
                    System.out.println("7. Eliminar producto");
                    System.out.println("8. Mostrar informe de clientes");
                    System.out.println("9. Mostrar informe de productos");
                    System.out.println("10. Mostrar detalles de ventas");
                    System.out.println("11. Salir");

                    System.out.print("Elige una opción: ");
                    int option = scanner.nextInt();
                    scanner.nextLine();

                    switch (option) {
                        case 1:
                            realizarVenta(scanner, reportService);
                            break;
                        case 2:
                            insertCliente(scanner, reportService);
                            break;
                        case 3:
                            insertProducto(scanner, reportService);
                            break;
                        case 4:
                            updateCliente(scanner, reportService);
                            break;
                        case 5:
                            updateProducto(scanner, reportService);
                            break;
                        case 6:
                            deleteCliente(scanner, reportService);
                            break;
                        case 7:
                            deleteProducto(scanner, reportService);
                            break;
                        case 8:
                            reportService.mostrarInformeClientes();
                            break;
                        case 9:
                            reportService.mostrarInformeProductos();
                            break;
                        case 10:
                            reportService.mostrarDetallesVentas();
                            break;
                        case 11:
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

    private static void realizarVenta(Scanner scanner, ReportService reportService) {
        reportService.mostrarProductosDisponibles();

        System.out.print("Introduce el ID del cliente: ");
        int idCliente = scanner.nextInt();
        System.out.print("Introduce el ID del producto: ");
        int idProducto = scanner.nextInt();
        System.out.print("Introduce la cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        reportService.realizarVenta(idCliente, idProducto, cantidad);
    }

    private static void insertCliente(Scanner scanner, ReportService reportService) {
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

        Cliente cliente = new Cliente(0, nombre, apellido, correo, telefono, direccion);
        reportService.agregarCliente(cliente);
    }

    private static void insertProducto(Scanner scanner, ReportService reportService) {
        System.out.print("Introduce el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce la descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Introduce el precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Introduce la cantidad en stock: ");
        int cantidadStock = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        Producto producto = new Producto(0, nombre, descripcion, precio, cantidadStock);
        reportService.agregarProducto(producto);
    }

    private static void updateCliente(Scanner scanner, ReportService reportService) {
        System.out.print("Introduce el ID del cliente a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        System.out.print("Introduce el nuevo nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce el nuevo apellido del cliente: ");
        String apellido = scanner.nextLine();
        System.out.print("Introduce el nuevo correo del cliente: ");
        String correo = scanner.nextLine();
        System.out.print("Introduce el nuevo teléfono del cliente: ");
        String telefono = scanner.nextLine();
        System.out.print("Introduce la nueva dirección del cliente: ");
        String direccion = scanner.nextLine();

        Cliente cliente = new Cliente(id, nombre, apellido, correo, telefono, direccion);
        reportService.actualizarCliente(cliente);
    }

    private static void updateProducto(Scanner scanner, ReportService reportService) {
        System.out.print("Introduce el ID del producto a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
        System.out.print("Introduce el nuevo nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce la nueva descripción del producto: ");
        String descripcion = scanner.nextLine();
        System.out.print("Introduce el nuevo precio del producto: ");
        double precio = scanner.nextDouble();
        System.out.print("Introduce la nueva cantidad en stock: ");
        int cantidadStock = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        Producto producto = new Producto(id, nombre, descripcion, precio, cantidadStock);
        reportService.actualizarProducto(producto);
    }

    private static void deleteCliente(Scanner scanner, ReportService reportService) {
        System.out.print("Introduce el ID del cliente a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        reportService.eliminarCliente(id);
    }

    private static void deleteProducto(Scanner scanner, ReportService reportService) {
        System.out.print("Introduce el ID del producto a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        reportService.eliminarProducto(id);
    }

}