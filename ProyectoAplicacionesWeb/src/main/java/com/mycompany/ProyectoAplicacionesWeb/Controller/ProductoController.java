package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Service.ProductoService;
import com.mycompany.ProyectoAplicacionesWeb.Service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/producto")
public class ProductoController {
    
    private final ProductoService productoService;
    private final CarritoService carritoService;
    
    public ProductoController(
            ProductoService productoService,
            CarritoService carritoService) {
        
        this.productoService = productoService;
        this.carritoService = carritoService;
    }
    
    @GetMapping("listado")
    public String inicio(Model model, HttpSession session) {
        
        var productos = productoService.getProductos(Boolean.TRUE);
        
        // Obtiene el carrito guardado en la sesión del usuario
        var carrito = carritoService.obtenerCarrito(session);
        
        // Lista de productos
        model.addAttribute("productos", productos);
        
        // Cantidad de productos disponibles
        model.addAttribute("totalCategorias", productos.size());
        
        // Información necesaria para mostrar el carrito
        model.addAttribute("listaItems", carrito);
        model.addAttribute(
                "carritoTotal",
                carritoService.calcularTotal(carrito)
        );
        
        return "productos/listado";
    }
}