package com.mycompany.ProyectoAplicacionesWeb.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuienesSomosController {

    @GetMapping("/quienesSomos")
    public String quienesSomos(HttpSession session) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        return "SubMenus/quienesSomos";
    }
}