package com.afl.apirest.models.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afl.apirest.models.dao.IClienteDao;
import com.afl.apirest.models.dao.IFacturaDao;
import com.afl.apirest.models.dao.IProductoDao;
import com.afl.apirest.models.entity.Cliente;
import com.afl.apirest.models.entity.Factura;
import com.afl.apirest.models.entity.Producto;
import com.afl.apirest.models.entity.Region;

@Service
public class ClienteServiceImpl implements IClienteService{

	//aunque se trata de una interfaz spring crea una instancia	
	@Autowired
	private IClienteDao clienteDao;

	@Autowired
	private IFacturaDao facturaDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	// Los métodos del CRUDRepository ya vienen con transaccionabilidad, pero a modo de documentacion o para sobreescribir podemos añadir la notación
	@Override 	
	@Transactional(readOnly=true)
	public List<Cliente> findAll (){
		// Como clienteDao.findAll() retorna Iterable<T> hacemos cast
		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}

	@Override
	@Transactional (readOnly = true)
	public List<Region> findAllRegiones() {
		return clienteDao.findAllRegiones();
	}

	@Override
	@Transactional (readOnly = true)
	public Factura findByFacturaId(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional 
	public Factura saveFactura(Factura factura) {
		return facturaDao.save(factura);
	}

	@Override
	@Transactional 
	public void deleteFacturaById(Long id) {
		facturaDao.deleteById(id);
	}

	@Override
	@Transactional (readOnly = true)
	public List<Producto> findProductoByName(String term) {
		return productoDao.findByNombre(term);
	}

}
