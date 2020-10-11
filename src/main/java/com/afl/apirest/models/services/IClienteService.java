package com.afl.apirest.models.services;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.afl.apirest.models.entity.Cliente;
import com.afl.apirest.models.entity.Factura;
import com.afl.apirest.models.entity.Producto;
import com.afl.apirest.models.entity.Region;

public interface IClienteService {
	
	List<Cliente> findAll();
	
	Page<Cliente> findAll(Pageable pageable);
	
	Cliente save (Cliente cliente);

	void delete (Long id);
	
	public Cliente findById(Long id);
	
	public List<Region> findAllRegiones();
	
	public Factura findByFacturaId(Long id);
	
	public Factura saveFactura (Factura factura);
	
	public void deleteFacturaById (Long id);
	
	public List<Producto> findProductoByName(String term);
	
	
}
