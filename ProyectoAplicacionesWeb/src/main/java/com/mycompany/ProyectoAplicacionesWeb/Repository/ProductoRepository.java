
package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    public List<Producto>findByActivoTrue();
}
