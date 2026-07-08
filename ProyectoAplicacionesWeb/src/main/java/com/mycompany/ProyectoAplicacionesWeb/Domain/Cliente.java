package com.mycompany.ProyectoAplicacionesWeb.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name="cliente")
public class Cliente implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    @Column(nullable = false,length = 100)
    private String nombre;

    @Column(nullable = false,length = 150)
    private String apellidos;

    @Column(nullable = false,length = 100)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 250)
    private String direccion;

    @Column(nullable = false)
    private Boolean activo=true;

}