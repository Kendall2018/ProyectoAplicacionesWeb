package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.SolicitudSoporte;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Tecnico;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import com.mycompany.ProyectoAplicacionesWeb.Repository.UsuarioRepository;
import com.mycompany.ProyectoAplicacionesWeb.Service.SolicitudSoporteService;
import com.mycompany.ProyectoAplicacionesWeb.Service.TecnicoService;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/solicitud")
public class SolicitudSoporteController {

    private final SolicitudSoporteService solicitudSoporteService;
    private final TecnicoService tecnicoService;
    private final UsuarioRepository usuarioRepository;


    public SolicitudSoporteController(
            SolicitudSoporteService solicitudSoporteService,
            TecnicoService tecnicoService,
            UsuarioRepository usuarioRepository) {

        this.solicitudSoporteService = solicitudSoporteService;
        this.tecnicoService = tecnicoService;
        this.usuarioRepository = usuarioRepository;
    }


    // =====================================================
    // LISTADO DE SOLICITUDES
    // =====================================================

    @GetMapping("/listado")
    public String listado(
            Model model,
            Principal principal) {

        // Si no existe sesión, regresar al login
        if (principal == null) {
            return "redirect:/login";
        }

        // El repositorio devuelve Optional<Usuario>
        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByUsernameAndActivoTrue(
                        principal.getName()
                );

        // Si no se encuentra el usuario
        if (usuarioOptional.isEmpty()) {
            return "redirect:/login";
        }

        // Obtener el Usuario real desde el Optional
        Usuario usuario = usuarioOptional.get();


        // =================================================
        // ADMIN VE TODAS LAS SOLICITUDES
        // USER SOLO VE LAS PROPIAS
        // =================================================

        if ("ADMIN".equalsIgnoreCase(usuario.getRol())) {

            model.addAttribute(
                    "solicitudes",
                    solicitudSoporteService.getSolicitudes()
            );

        } else {

            model.addAttribute(
                    "solicitudes",
                    solicitudSoporteService.getSolicitudesUsuario(
                            usuario.getIdUsuario()
                    )
            );
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        return "solicitud/listado";
    }


    // =====================================================
    // PANTALLA PARA CREAR UNA NUEVA SOLICITUD
    // =====================================================

    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute(
                "tecnicos",
                tecnicoService.getTecnicos(true)
        );

        return "solicitud/formulario";
    }


    // =====================================================
    // GUARDAR SOLICITUD
    // =====================================================

    @PostMapping("/guardar")
    public String guardar(

            @RequestParam String asunto,

            @RequestParam String descripcion,

            @RequestParam String prioridad,

            @RequestParam(required = false)
            Long idTecnico,

            Principal principal) {

        // Si no existe sesión
        if (principal == null) {
            return "redirect:/login";
        }


        // =================================================
        // OBTENER USUARIO AUTENTICADO
        // =================================================

        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByUsernameAndActivoTrue(
                        principal.getName()
                );


        if (usuarioOptional.isEmpty()) {
            return "redirect:/login";
        }


        Usuario usuario =
                usuarioOptional.get();


        // =================================================
        // CREAR SOLICITUD
        // =================================================

        SolicitudSoporte solicitud =
                new SolicitudSoporte();


        solicitud.setUsuario(
                usuario
        );


        solicitud.setAsunto(
                asunto
        );


        solicitud.setDescripcion(
                descripcion
        );


        solicitud.setPrioridad(
                prioridad
        );


        solicitud.setEstado(
                "Pendiente"
        );


        // =================================================
        // ASIGNAR TÉCNICO SI EL USUARIO LO SELECCIONÓ
        // =================================================

        if (idTecnico != null) {

            Tecnico tecnico =
                    tecnicoService.getTecnico(
                            idTecnico
                    );

            if (tecnico != null) {

                solicitud.setTecnico(
                        tecnico
                );

            }
        }



        solicitudSoporteService.save(
                solicitud
        );


        return "redirect:/solicitud/listado";
    }

}