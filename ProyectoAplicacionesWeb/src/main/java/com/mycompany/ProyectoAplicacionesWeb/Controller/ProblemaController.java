package com.mycompany.ProyectoAplicacionesWeb.Controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProblemaController {

    @GetMapping("/problemas")
    public String problemas(HttpSession session, Model model) {

        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/SubMenus";
        }

        return "SubMenus/problemas";
    }
}