
package com.mycompany.ProyectoAplicacionesWeb.Domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name="producto")
public class Producto implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "ruta_imagen", columnDefinition = "TEXT")
    private String rutaImagen;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(name = "existencias", nullable = false)
    private Integer existencias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;
}
