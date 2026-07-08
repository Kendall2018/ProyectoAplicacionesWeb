package com.mycompany.ProyectoAplicacionesWeb.Service;

import com.mycompany.ProyectoAplicacionesWeb.Domain.Cliente;
import com.mycompany.ProyectoAplicacionesWeb.Repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository=clienteRepository;
    }

    @Transactional(readOnly=true)
    public List<Cliente> getClientes(Boolean activo){

        if(activo){
            return clienteRepository.findByActivoTrue();
        }

        return clienteRepository.findAll();

    }

    @Transactional(readOnly=true)
    public Cliente getCliente(Long idCliente){

        return clienteRepository.findById(idCliente).orElse(null);

    }

    @Transactional
    public void save(Cliente cliente){

        clienteRepository.save(cliente);

    }

    @Transactional
    public void delete(Long idCliente){

        Cliente cliente=getCliente(idCliente);

        if(cliente!=null){

            cliente.setActivo(false);

            clienteRepository.save(cliente);

        }

    }

}