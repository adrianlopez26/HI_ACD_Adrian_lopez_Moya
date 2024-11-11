package com.techsolutions.model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;

}
