package com.mycompany.ProyectoAplicacionesWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProblemaController {

    @GetMapping("/problemas")
    public String problemas() {

        return "SubMenus/problemas";

    }

}