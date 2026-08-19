package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.SolicitudSoporte;
import com.mycompany.ProyectoAplicacionesWeb.Repository.SolicitudSoporteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudSoporteService {

    private final SolicitudSoporteRepository solicitudRepository;

    public SolicitudSoporteService(
            SolicitudSoporteRepository solicitudRepository) {

        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public List<SolicitudSoporte> getSolicitudes() {

        return solicitudRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SolicitudSoporte> getSolicitudesUsuario(
            Long idUsuario) {

        return solicitudRepository
                .findByUsuarioIdUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public SolicitudSoporte getSolicitud(
            Long idSolicitud) {

        return solicitudRepository
                .findById(idSolicitud)
                .orElse(null);
    }

    @Transactional
    public void save(
            SolicitudSoporte solicitud) {

        solicitudRepository.save(solicitud);
    }

    @Transactional
    public void delete(
            Long idSolicitud) {

        solicitudRepository.deleteById(idSolicitud);
    }
}