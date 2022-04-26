package com.example.proyecto.Services;

import java.util.Date;
import java.util.List;
import java.util.Optional;


import com.example.proyecto.Repository.ClienteRepository;
import com.example.proyecto.Repository.CompraRepository;
import com.example.proyecto.model.Cliente;
import com.example.proyecto.model.Compra;
import com.example.proyecto.model.Dulce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompraService implements ICompraService {

    @Autowired
    private CompraRepository repoC;

    @Autowired
    private ClienteRepository repoCli;

    @Override
    public boolean agregarCompra(Compra compra, Long id) {
        Optional<Cliente> cliente = repoCli.findById(id);
        if(cliente.isPresent()) {
            List<Dulce> dulces = compra.getPedido();
            Compra compra1 = new Compra(cliente.get(), dulces);
            //compra1.setPropietario(cliente.get());
            repoC.save(compra1);
            cliente.get().getFacturas().add(compra1);
            repoCli.save(cliente.get());
            return  true;
        }
        return false;
    } 

    @Override
    public List<Compra> buscarEntreFechas(Date inicio, Date fin) {
        return  repoC.findByFechaCompraBetween(inicio, fin);
    }

    @Override
    public List<Compra> buscarPorDuenno(Long clienteID) {
        return  repoC.getByIdPropietario(clienteID);
    }

    @Override
    public List<Compra> buscarPorFecha(Date date) {
        
        return repoC.findByFechaCompra(date);
    }

    @Override
    public Compra buscarPorId(Long id) {
        Optional<Compra> c =  repoC.findById(id);
        if(c.isPresent()){
            return c.get();
        }
        return null;
    }

    @Override
    public boolean editarCompra(Compra compra, Long id) {
        Optional<Compra> c =  repoC.findById(id);
        if(c.isPresent()){
            Compra nuevaCompra = c.get();
            nuevaCompra.setPedido(compra.getPedido());
            nuevaCompra.setPropietario(compra.getPropietario());
            nuevaCompra.setFechaCompra(compra.getFechaCompra());
            nuevaCompra.setTotal(compra.getTotal());
            repoC.save(nuevaCompra);
            return true;
        }
        return false;
    }

    @Override
    public List<Compra> getCompras() {
        return repoC.findAll();
    }

    @Override
    public boolean editarCompra(List<Dulce> carrito, Long id) {
        Optional<Compra> c =  repoC.findById(id);
        if(c.isPresent()){
            Compra compra = c.get();
            compra.setPedido(carrito);
            repoC.save(compra);
            return true;
        }
        return false;
    }

}
