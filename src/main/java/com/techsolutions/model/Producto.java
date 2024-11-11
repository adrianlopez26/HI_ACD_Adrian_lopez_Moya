package com.techsolutions.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int cantidadStock;
}
