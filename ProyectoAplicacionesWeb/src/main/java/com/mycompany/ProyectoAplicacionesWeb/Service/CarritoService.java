/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import com.mycompany.ProyectoAplicacionesWeb.Domain.DetalleVenta;
import com.mycompany.ProyectoAplicacionesWeb.Domain.EstadoVenta;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Item;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Producto;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Venta;
import com.mycompany.ProyectoAplicacionesWeb.Repository.DetalleVentaRepository;
import com.mycompany.ProyectoAplicacionesWeb.Repository.ProductoRepository;
import com.mycompany.ProyectoAplicacionesWeb.Repository.VentaRepository;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarritoService {
    private static final String ATTRIBUTE_CARRITO = "carrito";

    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public CarritoService(ProductoRepository productoRepository,
                           VentaRepository ventaRepository,
                           DetalleVentaRepository detalleVentaRepository) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    // --- 1. Gestión de Sesión ---
    public List<Item> obtenerCarrito(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Item> carrito = (List<Item>) session.getAttribute(ATTRIBUTE_CARRITO);
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    public void guardarCarrito(HttpSession session, List<Item> carrito) {
        session.setAttribute(ATTRIBUTE_CARRITO, carrito);
    }

    public void agregarProducto(List<Item> carrito, Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

        Optional<Item> itemExistente = carrito.stream()
            .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
            .findFirst();

        int cantidad = 1;

        if (itemExistente.isPresent()) {
            Item item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;

            if (nuevaCantidad > producto.getExistencias()) {
                throw new RuntimeException("Stock insuficiente para agregar " + cantidad + " unidades.");
            }
            item.setCantidad(nuevaCantidad);
        } else {
            if (cantidad > producto.getExistencias()) {
                throw new RuntimeException("Stock insuficiente para agregar " + cantidad + " unidades.");
            }

            Item nuevoItem = new Item();
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            nuevoItem.setPrecio(producto.getPrecio()); // Captura el precio actual
            carrito.add(nuevoItem);
        }
    }

    public Item buscarItem(List<Item> carrito, Integer idProducto) {
        if (carrito == null) {
            return null;
        }
        return carrito.stream()
                .filter(item -> item.getProducto().getIdProducto().equals(idProducto))
                .findFirst()
                .orElse(null);
    }

    public void eliminarItem(List<Item> carrito, Integer idProducto) {
        carrito.removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
    }

    public void actualizarCantidad(List<Item> carrito, Integer idProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarItem(carrito, idProducto);
            return;
        }

        Optional<Item> itemExistente = carrito.stream()
            .filter(i -> i.getProducto().getIdProducto().equals(idProducto))
            .findFirst();

        if (itemExistente.isPresent()) {
            Item item = itemExistente.get();
            Producto producto = item.getProducto();

            if (nuevaCantidad > producto.getExistencias()) {
                throw new RuntimeException("No hay suficiente stock disponible.");
            }
            item.setCantidad(nuevaCantidad);
        }
    }

    public int contarUnidades(List<Item> carrito) {
        if (carrito == null || carrito.isEmpty()) {
            return 0;
        }
        return carrito.stream()
                .mapToInt(Item::getCantidad)
                .sum();
    }

    public BigDecimal calcularTotal(List<Item> carrito) {
        return carrito.stream()
            .map(Item::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limpiarCarrito(HttpSession session) {
        List<Item> carrito = obtenerCarrito(session);
        if (carrito != null) {
            carrito.clear();
        }
        guardarCarrito(session, carrito);
    }

    // --- 2. Procesar Compra (Checkout) ---
    @Transactional
    public Venta procesarCompra(List<Item> carrito, Cliente cliente) {
        if (carrito == null || carrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío para procesar la compra.");
        }

        // 1. CREAR Y PERSISTIR LA VENTA (cabecera)
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setTotal(calcularTotal(carrito));
        venta.setEstado(EstadoVenta.Pagada);
        venta = ventaRepository.save(venta); // Persistir primero para obtener id_venta

        // 2. CREAR Y PERSISTIR CADA DETALLE, Y ACTUALIZAR STOCK
        for (Item item : carrito) {
            Producto producto = productoRepository.findById(item.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

            // Doble chequeo de stock antes de persistir
            if (item.getCantidad() > producto.getExistencias()) {
                throw new RuntimeException("Fallo en la compra: el producto " + producto.getDescripcion()
                    + " ya no tiene suficiente stock.");
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecio(item.getPrecio());
            detalle.setSubtotal(item.getSubTotal());
            detalleVentaRepository.save(detalle);

            producto.setExistencias(producto.getExistencias() - item.getCantidad());
            productoRepository.save(producto);
        }

        return venta;
    }
}
