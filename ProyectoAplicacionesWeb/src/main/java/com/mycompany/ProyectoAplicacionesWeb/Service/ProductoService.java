
package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Producto;
import com.mycompany.ProyectoAplicacionesWeb.Repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProductoService {
    
    private final ProductoRepository productoRepository;
    
    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }
    
    @Transactional(readOnly= true)
    public List<Producto> getProductos (Boolean activo){
        if (activo){
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
        
    }
    public void save(Producto producto){
        productoRepository.save(producto);
    }
    public void delete(Long id){
        productoRepository.deleteById(id);
    }
    
    public void update(Producto producto){
        productoRepository.save(producto);
    }
    
    public Producto getProducto(Long id) {
        return productoRepository.findById(id).orElse(null);
 }
    
}
