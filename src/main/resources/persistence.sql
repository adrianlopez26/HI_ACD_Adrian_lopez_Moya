#Creacion BD

create database bookworld;
use bookworld;

CREATE TABLE Cliente (
    ID_Cliente INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Correo VARCHAR(100),
    Telefono VARCHAR(15),
    Direccion VARCHAR(150)
);

CREATE TABLE Producto (
    ID_Producto INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100),
    Descripcion VARCHAR(255),
    Precio DECIMAL(10, 2),
    Cantidad_Stock INT
);

CREATE TABLE Ventas (
    ID_Venta INT AUTO_INCREMENT PRIMARY KEY,
    ID_Cliente INT,
    ID_Producto INT,
    Fecha_Venta DATE,
    Cantidad INT,
    Total DECIMAL(10, 2),
    FOREIGN KEY (ID_Cliente) REFERENCES Cliente(ID_Cliente),
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

CREATE TABLE Detalle_Venta (
    ID_Detalle INT AUTO_INCREMENT PRIMARY KEY,
    ID_Venta INT,
    ID_Producto INT,
    Cantidad INT,
    Precio_Total DECIMAL(10, 2),
    FOREIGN KEY (ID_Venta) REFERENCES Ventas(ID_Venta),
    FOREIGN KEY (ID_Producto) REFERENCES Producto(ID_Producto)
);

CREATE TABLE Logs (
    ID_Log INT AUTO_INCREMENT PRIMARY KEY,
    Operacion VARCHAR(50),
    Tabla_Afectada VARCHAR(50),
    Fecha_Hora DATETIME,
    Detalles TEXT
);


#Inserts a la BD

INSERT INTO Cliente (Nombre, Apellido, Correo, Telefono, Direccion) VALUES
('Juan', 'Pérez', 'juan.perez@example.com', '612345678', 'Calle Mayor, 10'),
('María', 'López', 'maria.lopez@example.com', '622345678', 'Calle Serrano, 20'),
('Pedro', 'Gómez', 'pedro.gomez@example.com', '632345678', 'Avenida Gran Vía, 5'),
('Laura', 'Martínez', 'laura.martinez@example.com', '642345678', 'Paseo de la Castellana, 15'),
('Luis', 'Fernández', 'luis.fernandez@example.com', '652345678', 'Calle Alcalá, 30');


INSERT INTO Producto (Nombre, Descripcion, Precio, Cantidad_Stock) VALUES
('Libro de Java', 'Un libro completo sobre desarrollo en Java', 29.99, 50),
('Curso de Python', 'Acceso a un curso en línea de Python', 49.99, 20),
('Cuaderno de notas', 'Cuaderno para apuntes con tapa dura', 5.99, 100),
('Bolígrafo azul', 'Bolígrafo de tinta azul, punta fina', 0.99, 200),
('Agenda 2025', 'Agenda con planificación diaria', 15.99, 30);


INSERT INTO Ventas (ID_Cliente, ID_Producto, Fecha_Venta, Cantidad, Total) VALUES
(1, 1, '2024-11-10', 2, 59.98),
(2, 3, '2024-11-08', 5, 29.95),
(3, 2, '2024-11-05', 1, 49.99),
(4, 5, '2024-11-07', 2, 31.98),
(5, 4, '2024-11-09', 10, 9.90);


INSERT INTO Logs (Operacion, Tabla_Afectada, Fecha_Hora, Detalles) VALUES
('INSERT', 'Cliente', '2024-11-10 10:15:30', 'Añadido nuevo cliente: Juan Pérez'),
('UPDATE', 'Producto', '2024-11-09 15:42:10', 'Actualizado stock de producto: Bolígrafo azul'),
('DELETE', 'Cliente', '2024-11-08 13:22:45', 'Eliminado cliente con ID: 3'),
('INSERT', 'Ventas', '2024-11-07 18:05:00', 'Venta registrada para cliente ID: 4'),
('UPDATE', 'Producto', '2024-11-06 17:30:20', 'Modificado precio de producto: Curso de Python');

