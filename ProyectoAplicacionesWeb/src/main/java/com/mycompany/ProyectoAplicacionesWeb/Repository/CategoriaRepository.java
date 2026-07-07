
package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    public List<Categoria>findByActivoTrue(boolean activo);
}

