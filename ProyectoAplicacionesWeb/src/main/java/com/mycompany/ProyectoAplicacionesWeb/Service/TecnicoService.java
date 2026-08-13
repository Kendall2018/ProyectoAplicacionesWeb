package com.mycompany.ProyectoAplicacionesWeb.Service;


import com.mycompany.ProyectoAplicacionesWeb.Domain.Tecnico;

import com.mycompany.ProyectoAplicacionesWeb.Repository.TecnicoRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;


@Service
public class TecnicoService {


    private final TecnicoRepository tecnicoRepository;


    public TecnicoService(
            TecnicoRepository tecnicoRepository) {

        this.tecnicoRepository =
                tecnicoRepository;
    }


    

    @Transactional(readOnly = true)
    public List<Tecnico> getTecnicos(
            Boolean activos) {


        if (Boolean.TRUE.equals(activos)) {

            return tecnicoRepository
                    .findByActivoTrue();

        }


        return tecnicoRepository.findAll();

    }


    

    @Transactional(readOnly = true)
    public Tecnico getTecnico(
            Long idTecnico) {


        return tecnicoRepository
                .findById(idTecnico)
                .orElse(null);

    }



    @Transactional
    public void save(
            Tecnico tecnico) {


        if (tecnico.getActivo() == null) {

            tecnico.setActivo(true);

        }


        tecnicoRepository.save(tecnico);

    }


    // ==========================================
    // DESACTIVAR TECNICO
    // ==========================================

    @Transactional
    public void delete(
            Long idTecnico) {


        Tecnico tecnico =
                getTecnico(idTecnico);


        if (tecnico != null) {


            tecnico.setActivo(false);


            tecnicoRepository.save(
                    tecnico
            );

        }

    }

}