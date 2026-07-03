package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Usuario;
import com.mycompany.ProyectoAplicacionesWeb.Repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping({"/", "/login"})
    public String mostrarLogin() {
        return "login/login";
    }

    @PostMapping("/login")
    public String validarLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByUsernameAndPasswordAndActivoTrue(username, password);

        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/inicio";
        }

        model.addAttribute("error", true);
        return "login/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "login/registro";
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
        usuario.setPassword(password);
        usuario.setRol("USER");
        usuario.setActivo(true);

        usuarioRepository.save(usuario);

        return "redirect:/login";
    }

    @GetMapping("/inicio")
    public String inicio(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);

        return "login/inicio";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}