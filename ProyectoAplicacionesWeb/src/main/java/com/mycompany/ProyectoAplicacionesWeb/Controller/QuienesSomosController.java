package com.mycompany.ProyectoAplicacionesWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuienesSomosController {

    @GetMapping("/quienesSomos")
    public String quienesSomos() {
        return "SubMenus/QuienesSomos";
    }
}