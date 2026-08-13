package com.mycompany.ProyectoAplicacionesWeb.Repository;


import com.mycompany.ProyectoAplicacionesWeb.Domain.Tecnico;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TecnicoRepository
        extends JpaRepository<Tecnico, Long> {


    List<Tecnico> findByActivoTrue();


}