package com.mycompany.ProyectoAplicacionesWeb.Controller;


import com.mycompany.ProyectoAplicacionesWeb.Domain.Tecnico;

import com.mycompany.ProyectoAplicacionesWeb.Service.CorreoService;

import com.mycompany.ProyectoAplicacionesWeb.Service.TecnicoService;


import jakarta.mail.MessagingException;


import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/tecnico")
public class TecnicoController {


    private final TecnicoService tecnicoService;

    private final CorreoService correoService;


    public TecnicoController(

            TecnicoService tecnicoService,

            CorreoService correoService) {


        this.tecnicoService =
                tecnicoService;


        this.correoService =
                correoService;

    }


    // ==========================================
    // LISTADO DE TECNICOS
    // ==========================================

    @GetMapping("/listado")
    public String listado(
            Model model) {


        var tecnicos =
                tecnicoService
                        .getTecnicos(true);


        model.addAttribute(
                "tecnicos",
                tecnicos
        );


        model.addAttribute(
                "totalTecnicos",
                tecnicos.size()
        );


        return "tecnico/listado";

    }


    // ==========================================
    // MOSTRAR FORMULARIO DE CONTACTO
    // ==========================================

    @GetMapping("/contactar/{id}")
    public String contactar(

            @PathVariable Long id,

            Model model) {


        Tecnico tecnico =
                tecnicoService
                        .getTecnico(id);


        if (tecnico == null) {

            return "redirect:/tecnico/listado";

        }


        model.addAttribute(
                "tecnico",
                tecnico
        );


        return "tecnico/contactar";

    }


    // ==========================================
    // ENVIAR CORREO
    // ==========================================

    @PostMapping("/enviarCorreo")
    public String enviarCorreo(

            @RequestParam Long idTecnico,

            @RequestParam String asunto,

            @RequestParam String mensaje,

            Model model) {


        Tecnico tecnico =
                tecnicoService
                        .getTecnico(idTecnico);


        // ======================================
        // VALIDAR TECNICO
        // ======================================

        if (tecnico == null) {

            return "redirect:/tecnico/listado";

        }


        try {


            // ==================================
            // CREAR CORREO EN HTML
            // ==================================

            String contenido = """

                    <!DOCTYPE html>

                    <html>

                    <body style="
                        margin:0;
                        padding:30px;
                        background:#f4f6f8;
                        font-family:Arial,Helvetica,sans-serif;
                    ">


                    <div style="
                        max-width:650px;
                        margin:auto;
                        background:white;
                        border-radius:18px;
                        overflow:hidden;
                        box-shadow:0 8px 30px rgba(0,0,0,.12);
                    ">


                        <div style="
                            background:#003b73;
                            padding:25px 30px;
                            color:white;
                        ">

                            <h2 style="
                                margin:0;
                            ">

                                Soporte TECH

                            </h2>

                            <p style="
                                margin:7px 0 0;
                                opacity:.9;
                            ">

                                Nueva solicitud de soporte

                            </p>

                        </div>


                        <div style="
                            padding:30px;
                        ">


                            <p>

                                Hola

                                <strong>
                                    %s %s
                                </strong>,

                            </p>


                            <p>

                                Se ha generado una nueva
                                solicitud de soporte técnico
                                desde el sistema

                                <strong>
                                    Soporte TECH
                                </strong>.

                            </p>


                            <div style="
                                margin-top:25px;
                                background:#eef7fb;
                                padding:22px;
                                border-radius:12px;
                                border-left:5px solid #0077b6;
                            ">


                                <h3 style="
                                    color:#003b73;
                                    margin-top:0;
                                ">

                                    Descripción del problema

                                </h3>


                                <p style="
                                    white-space:pre-line;
                                    color:#333;
                                    line-height:1.6;
                                ">

                                    %s

                                </p>


                            </div>


                            <p style="
                                margin-top:30px;
                                font-size:13px;
                                color:#777;
                            ">

                                Este correo fue generado
                                automáticamente desde
                                el sistema Soporte TECH.

                            </p>


                        </div>


                    </div>


                    </body>

                    </html>

                    """.formatted(

                    tecnico.getNombre(),

                    tecnico.getApellidos(),

                    mensaje

            );


            // ==================================
            // ENVIAR
            // ==================================

            correoService.enviarCorreoHtml(

                    tecnico.getCorreo(),

                    asunto,

                    contenido

            );


            // ==================================
            // MENSAJE DE EXITO
            // ==================================

            model.addAttribute(

                    "exito",

                    "El correo fue enviado correctamente a "
                    + tecnico.getNombre()
                    + " "
                    + tecnico.getApellidos()

            );


        } catch (MessagingException e) {


            // ==================================
            // ERROR
            // ==================================

            model.addAttribute(

                    "error",

                    "No se pudo enviar el correo. "
                    + "Verifique la configuración de Gmail."

            );


            System.err.println(
                    "ERROR AL ENVIAR CORREO: "
                    + e.getMessage()
            );

        }


        model.addAttribute(
                "tecnico",
                tecnico
        );


        return "tecnico/contactar";

    }


    // ==========================================
    // NUEVO TECNICO
    // SOLO ADMIN
    // ==========================================

    @GetMapping("/nuevo")
    public String nuevo(
            Model model) {


        Tecnico tecnico =
                new Tecnico();


        tecnico.setActivo(true);


        model.addAttribute(
                "tecnico",
                tecnico
        );


        return "tecnico/formulario";

    }


    // ==========================================
    // GUARDAR TECNICO
    // ==========================================

    @PostMapping("/guardar")
    public String guardar(

            @ModelAttribute Tecnico tecnico) {


        tecnicoService.save(
                tecnico
        );


        return "redirect:/tecnico/listado";

    }


    // ==========================================
    // MODIFICAR TECNICO
    // ==========================================

    @GetMapping("/modificar/{id}")
    public String modificar(

            @PathVariable Long id,

            Model model) {


        Tecnico tecnico =
                tecnicoService
                        .getTecnico(id);


        if (tecnico == null) {

            return "redirect:/tecnico/listado";

        }


        model.addAttribute(
                "tecnico",
                tecnico
        );


        return "tecnico/formulario";

    }




    @GetMapping("/eliminar/{id}")
    public String eliminar(

            @PathVariable Long id) {


        tecnicoService.delete(
                id
        );


        return "redirect:/tecnico/listado";

    }

}