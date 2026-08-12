/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Item;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Venta;
import com.mycompany.ProyectoAplicacionesWeb.Repository.ClienteRepository;
import com.mycompany.ProyectoAplicacionesWeb.Repository.UsuarioRepository;
import com.mycompany.ProyectoAplicacionesWeb.Service.CarritoService;
import com.mycompany.ProyectoAplicacionesWeb.Service.ClienteService;
import com.mycompany.ProyectoAplicacionesWeb.Service.VentaService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CarritoController {
 
    private final CarritoService carritoService;
    private final VentaService ventaService;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
 
    public CarritoController(CarritoService carritoService, UsuarioRepository usuarioRepository,
                              VentaService ventaService, ClienteRepository clienteRepository) {
        this.carritoService = carritoService;
        this.usuarioRepository = usuarioRepository;
        this.ventaService = ventaService;
        this.clienteRepository = clienteRepository;
    }
 
    // --- 1. MOSTRAR EL CARRITO ---
    @GetMapping("/carrito/listado")
    public String listado(HttpSession session, Model model) {
        List<Item> carrito = carritoService.obtenerCarrito(session);
 
        model.addAttribute("carritoItems", carrito);
        model.addAttribute("totalCarrito", carritoService.calcularTotal(carrito));
 
        return "/carrito/listado";
    }
 
    // --- 2. AGREGAR PRODUCTO AL CARRITO ---
    @PostMapping("/carrito/agregar")
    public ModelAndView agregar(
            @RequestParam("idProducto") Integer idProducto,
            HttpSession session,
            Model model) {
        try {
            List<Item> carrito = carritoService.obtenerCarrito(session);
            carritoService.agregarProducto(carrito, idProducto);
            carritoService.guardarCarrito(session, carrito);
 
            model.addAttribute("carritoTotal", carritoService.calcularTotal(carrito));
            model.addAttribute("listaItems", carrito);
 
            return new ModelAndView("/carrito/fragmentos :: verCarrito", model.asMap());
 
        } catch (RuntimeException e) {
            model.addAttribute("errorMensaje", e.getMessage());
            return new ModelAndView("/errores/fragmentos :: errorMensaje", model.asMap());
        }
    }
 
    // --- 3. ELIMINAR ITEM DEL CARRITO ---
    @PostMapping("/carrito/eliminar/{idProducto}")
    public String eliminarItem(
            @PathVariable("idProducto") Integer idProducto,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
 
        List<Item> carrito = carritoService.obtenerCarrito(session);
        carritoService.eliminarItem(carrito, idProducto);
        carritoService.guardarCarrito(session, carrito);
 
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado del carrito.");
        return "redirect:/carrito/listado";
    }
 
    @GetMapping("/carrito/modificar/{idProducto}")
    public String modificar(
            @PathVariable("idProducto") Integer idProducto,
            HttpSession session,
            Model model) {
 
        List<Item> carrito = carritoService.obtenerCarrito(session);
        Item item = carritoService.buscarItem(carrito, idProducto);
 
        if (item == null) {
            return "redirect:/carrito/listado";
        }
 
        model.addAttribute("item", item);
        return "/carrito/modifica";
    }
 
    // --- 4. ACTUALIZAR CANTIDAD DESDE LA VISTA ---
    @PostMapping("/carrito/actualizar")
    public String actualizarCantidad(
            @RequestParam("producto.idProducto") Integer idProducto,
            @RequestParam("cantidad") int nuevaCantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
 
        try {
            List<Item> carrito = carritoService.obtenerCarrito(session);
            carritoService.actualizarCantidad(carrito, idProducto, nuevaCantidad);
            carritoService.guardarCarrito(session, carrito);
 
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
 
        return "redirect:/carrito/listado";
    }
 
    // --- 5. PROCESAR COMPRA (CHECKOUT) ---
    @GetMapping("/facturar/carrito")
    public String facturarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            List<Item> carrito = carritoService.obtenerCarrito(session);
 
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            Usuario usuario = usuarioRepository.getUsuarioPorUsername(username).get();
 
            // El Cliente se vincula al Usuario autenticado por correo (no hay FK directa entre las tablas)
            Cliente cliente = clienteRepository.findByCorreo(usuario.getCorreo())
                .orElseThrow(() -> new RuntimeException("No se encontró un cliente asociado a este usuario."));
 
            // 1. La lógica transaccional ocurre en el servicio
            Venta venta = carritoService.procesarCompra(carrito, cliente);
 
            // 2. Limpiar el carrito de la sesión después de una compra exitosa
            carritoService.limpiarCarrito(session);
 
            // 3. Pasar el ID de la venta como Flash Attribute
            redirectAttributes.addFlashAttribute("idVenta", venta.getIdVenta());
            redirectAttributes.addFlashAttribute("mensaje", "Compra procesada con éxito. Venta Nro: " + venta.getIdVenta());
 
            // 4. Redirigir a la vista de la venta generada
            return "redirect:/carrito/verVenta";
 
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la compra: " + e.getMessage());
            return "redirect:/carrito/listado";
        }
    }
 
    // --- 6. MOSTRAR LA VENTA GENERADA ---
    @GetMapping("/carrito/verVenta")
    public String verVenta(@ModelAttribute("idVenta") Integer idVenta, Model model) {
        if (idVenta == null) {
            return "redirect:/index";
        }
 
        // Venta COMPLETA (con sus detalles y productos, gracias al FETCH JOIN)
        Venta venta = ventaService.getVentaConDetalle(idVenta);
 
        model.addAttribute("venta", venta);
        return "/carrito/verVenta"; // Nombre del archivo Thymeleaf
    }
}