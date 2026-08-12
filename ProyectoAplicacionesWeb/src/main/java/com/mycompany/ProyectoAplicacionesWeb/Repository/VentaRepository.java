package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Venta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository
        extends JpaRepository<Venta, Integer> {

    @Query("""
           SELECT DISTINCT v
           FROM Venta v
           JOIN FETCH v.cliente c
           LEFT JOIN FETCH v.detalles d
           LEFT JOIN FETCH d.producto p
           WHERE v.idVenta = :idVenta
           """)
    Optional<Venta> findByIdVentaConDetalle(
            @Param("idVenta") Integer idVenta
    );
}