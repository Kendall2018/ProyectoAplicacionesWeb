package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Categoria;
import com.mycompany.ProyectoAplicacionesWeb.Service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {

        var categorias = categoriaService.getCategorias(true);

        model.addAttribute("categorias", categorias);
        model.addAttribute("totalCategorias", categorias.size());

        return "categoria/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        model.addAttribute("categoria", new Categoria());

        return "categoria/formulario";

    }

    @PostMapping("/guardar")
    public String guardar(Categoria categoria) {

        categoriaService.save(categoria);

        return "redirect:/categoria/listado";

    }

    @GetMapping("/modificar/{idCategoria}")
    public String modificar(@PathVariable Long idCategoria,
            Model model) {

        model.addAttribute("categoria",
                categoriaService.getCategoria(idCategoria));

        return "categoria/formulario";

    }

    @GetMapping("/eliminar/{idCategoria}")
    public String eliminar(@PathVariable Long idCategoria) {

        categoriaService.delete(idCategoria);

        return "redirect:/categoria/listado";

    }

}
