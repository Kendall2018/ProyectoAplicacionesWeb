package com.mycompany.ProyectoAplicacionesWeb.Controller;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import com.mycompany.ProyectoAplicacionesWeb.Service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService){

        this.clienteService=clienteService;

    }

    @GetMapping("/listado")
    public String listado(Model model){

        var clientes=clienteService.getClientes(true);

        model.addAttribute("clientes",clientes);
        model.addAttribute("totalClientes",clientes.size());

        return "cliente/listado";

    }

    @GetMapping("/nuevo")
    public String nuevo(Cliente cliente){

        return "cliente/formulario";

    }

    @PostMapping("/guardar")
    public String guardar(Cliente cliente){

        clienteService.save(cliente);

        return "redirect:/cliente/listado";

    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id,Model model){

        model.addAttribute("cliente",
                clienteService.getCliente(id));

        return "cliente/formulario";

    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id){

        clienteService.delete(id);

        return "redirect:/cliente/listado";

    }

}