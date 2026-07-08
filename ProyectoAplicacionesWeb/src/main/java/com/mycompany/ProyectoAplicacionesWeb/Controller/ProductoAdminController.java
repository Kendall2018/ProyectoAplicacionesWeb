
package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Producto;
import com.mycompany.ProyectoAplicacionesWeb.Service.CategoriaService;
import com.mycompany.ProyectoAplicacionesWeb.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class ProductoAdminController {

 private final ProductoService productoService;
 private final CategoriaService categoriaService;
 
    
    public ProductoAdminController(ProductoService productoService, CategoriaService categoriaService){
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

 
 @GetMapping("listado")
    public String listado(Model model) {
        model.addAttribute("productos", productoService.getProductos(Boolean.FALSE));
        return "admin/listado";
    }

    @GetMapping("/producto/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.getCategorias(Boolean.FALSE));
        return "admin/modifica";
    }

    @PostMapping("/producto/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.save(producto);
        return "redirect:/admin/listado";
    }

    @GetMapping("/producto/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.getProducto(id));
        model.addAttribute("categorias", categoriaService.getCategorias(Boolean.FALSE));
        return "admin/modifica";
    }

    @PostMapping("/producto/actualizar")
    public String actualizar(@ModelAttribute Producto producto) {
        productoService.update(producto);
        return "redirect:/admin/listado";
    }

    @GetMapping("/producto/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        productoService.delete(id);
        return "redirect:/admin/listado";
    }
}
