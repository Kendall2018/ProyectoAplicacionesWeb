package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.SolicitudSoporte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudSoporteRepository
        extends JpaRepository<SolicitudSoporte, Long> {

    List<SolicitudSoporte> findByUsuarioIdUsuario(
            Long idUsuario
    );
}