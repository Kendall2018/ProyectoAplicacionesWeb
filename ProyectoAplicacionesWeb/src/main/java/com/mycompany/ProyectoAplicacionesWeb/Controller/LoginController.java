package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import com.mycompany.ProyectoAplicacionesWeb.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
    public String guardarUsuario(
            @RequestParam String nombre,
            @RequestParam String username,
            @RequestParam String correo,
            @RequestParam String password) {

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
