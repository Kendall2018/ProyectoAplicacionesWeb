/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Venta;
import com.mycompany.ProyectoAplicacionesWeb.Repository.VentaRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Transactional(readOnly = true)
    public Venta getVentaConDetalle(Integer idVenta) {
        return ventaRepository.findByIdVentaConDetalle(idVenta)
                .orElseThrow(() -> new NoSuchElementException("Venta con ID " + idVenta + " no encontrada."));
    }
}
