package com.mycompany.ProyectoAplicacionesWeb.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "solicitud_soporte")
public class SolicitudSoporte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Long idSolicitud;

    @ManyToOne
    @JoinColumn(
            name = "id_usuario",
            nullable = false
    )
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(
            name = "id_tecnico"
    )
    private Tecnico tecnico;

    @Column(
            name = "asunto",
            nullable = false,
            length = 150
    )
    private String asunto;

    @Column(
            name = "descripcion",
            nullable = false,
            length = 500
    )
    private String descripcion;

    @Column(
            name = "prioridad",
            nullable = false,
            length = 30
    )
    private String prioridad;

    @Column(
            name = "estado",
            nullable = false,
            length = 30
    )
    private String estado = "Pendiente";

    @Column(
            name = "fecha_creacion",
            nullable = false
    )
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {

        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }

        if (estado == null || estado.isBlank()) {
            estado = "Pendiente";
        }
    }
}