package com.mycompany.ProyectoAplicacionesWeb.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;


@Data
@Entity
@Table(name = "tecnico")
public class Tecnico implements Serializable {


    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "id_tecnico")
    private Long idTecnico;


    @Column(
            name = "nombre",
            nullable = false,
            length = 100
    )
    private String nombre;


    @Column(
            name = "apellidos",
            nullable = false,
            length = 150
    )
    private String apellidos;


    @Column(
            name = "especialidad",
            nullable = false,
            length = 150
    )
    private String especialidad;


    @Column(
            name = "correo",
            nullable = false,
            length = 150
    )
    private String correo;


    @Column(
            name = "telefono",
            length = 20
    )
    private String telefono;


    @Column(
            name = "descripcion",
            length = 500
    )
    private String descripcion;


    @Column(
            name = "activo",
            nullable = false
    )
    private Boolean activo = true;

}