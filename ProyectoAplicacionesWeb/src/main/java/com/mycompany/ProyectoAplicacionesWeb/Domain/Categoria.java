
package com.mycompany.ProyectoAplicacionesWeb.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {
   
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 250)
    private String descripcion;

    @Column(name = "ruta_imagen", nullable = false, columnDefinition = "TEXT")
    private String rutaImagen;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean activo = true;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Producto> productos;
    
    
}
