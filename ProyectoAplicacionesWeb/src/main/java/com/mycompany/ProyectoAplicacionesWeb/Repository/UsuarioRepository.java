package com.mycompany.ProyectoAplicacionesWeb.Repository;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsernameAndPasswordAndActivoTrue(String username, String password);

    Optional<Usuario> findByUsernameAndActivoTrue(String username);
    
    Optional<Usuario> findByUsername(String username);
    
    @Query("SELECT u FROM Usuario u WHERE u.username = :username")
    Optional<Usuario> getUsuarioPorUsername(@Param("username") String username);
}