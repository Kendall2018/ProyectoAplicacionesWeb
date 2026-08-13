package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import com.mycompany.ProyectoAplicacionesWeb.Repository.ClienteRepository;
import com.mycompany.ProyectoAplicacionesWeb.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class LoginController {
 
    @Autowired
    private UsuarioRepository usuarioRepository;
 
    @Autowired
    private ClienteRepository clienteRepository;
 
    @GetMapping({"/", "/login"})
    public String mostrarLogin(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        if (logout != null) {
            model.addAttribute("logout", true);
        }
        return "login/login";
    }
 
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "login/registro";
    }
 
    @GetMapping("/accesoDenegado")
    public String accesoDenegado() {
        return "login/accesoDenegado";
    }
 
    @PostMapping("/guardarUsuario")
    @Transactional
    public String guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String username,
            @RequestParam String correo,
            @RequestParam String password) {
 
        // 1. Crear el Usuario (login / autenticación)
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setUsername(username);
        usuario.setCorreo(correo);
        // Temporalmente se guarda en texto plano porque
        // estamos usando NoOpPasswordEncoder.
        // Más adelante lo cambiaremos a BCrypt.
        usuario.setPassword(password);
        usuario.setRol("USER");
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
 
        // 2. Crear el Cliente asociado (datos de facturación), vinculado por correo.
        //    Solo se crea si no existe ya un Cliente con ese correo.
        if (clienteRepository.findByCorreo(correo).isEmpty()) {
            Cliente cliente = new Cliente();
 
            // El formulario solo pide un "nombre" completo; lo separamos en
            // nombre (primera palabra) y apellidos (resto). Si el registro
            // llega a tener un campo "apellidos" propio, usar ese en vez de
            // este split.
            String[] partes = nombre.trim().split("\\s+", 2);
            cliente.setNombre(partes[0]);
            cliente.setApellidos(partes.length > 1 ? partes[1] : "");
 
            cliente.setCorreo(correo);
            cliente.setActivo(true);
            clienteRepository.save(cliente);
        }
 
        return "redirect:/login";
    }
 
    @GetMapping("/inicio")
    public String inicio(Model model, Principal principal) {
        Usuario usuario = usuarioRepository
                .findByUsernameAndActivoTrue(principal.getName())
                .orElse(null);
        model.addAttribute("usuario", usuario);
        return "login/inicio";
    }
}
