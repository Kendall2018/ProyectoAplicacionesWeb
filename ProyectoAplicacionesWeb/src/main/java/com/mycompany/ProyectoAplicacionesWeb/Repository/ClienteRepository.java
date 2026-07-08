package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{

    List<Cliente> findByActivoTrue();

}