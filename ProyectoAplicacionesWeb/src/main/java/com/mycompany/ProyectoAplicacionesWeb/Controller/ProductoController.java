
package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    
    private final ProductoService productoService;
    
    public ProductoController(ProductoService productoService){
        this.productoService = productoService;
    }
    
    @GetMapping("listado")
    public String inicio (Model model){
        var productos = productoService.getProductos(Boolean.TRUE);
        model.addAttribute("productos", productos);
        model.addAttribute("totalCategorias", productos.size());
        return "productos/listado";
    }
    
}
