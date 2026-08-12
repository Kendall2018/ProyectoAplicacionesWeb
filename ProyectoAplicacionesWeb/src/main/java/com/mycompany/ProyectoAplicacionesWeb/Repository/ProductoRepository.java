
package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto>findByActivoTrue();
    
    public Optional<Producto>findById(Integer idProducto);
}
