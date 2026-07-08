package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Categoria;
import com.mycompany.ProyectoAplicacionesWeb.Repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(Boolean activo) {

        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }

        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria getCategoria(Long idCategoria) {

        return categoriaRepository.findById(idCategoria).orElse(null);

    }

    @Transactional
    public void save(Categoria categoria) {

        categoriaRepository.save(categoria);

    }

    @Transactional
    public void delete(Long idCategoria) {

        categoriaRepository.deleteById(idCategoria);

    }

}
