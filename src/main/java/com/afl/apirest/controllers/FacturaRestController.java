package com.afl.apirest.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.afl.apirest.models.entity.Factura;
import com.afl.apirest.models.entity.Producto;
import com.afl.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200", "http://localhost:8070", "*"})
@RestController
@RequestMapping("/api")

public class FacturaRestController {

	@Autowired IClienteService clienteService;
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(code=HttpStatus.OK)
	public Factura show (@PathVariable Long id) {
		return clienteService.findByFacturaId(id);
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/facturas/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteFactura(@PathVariable Long id) {
		clienteService.deleteFacturaById(id);
	}
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/facturas/filtrar-productos/{term}")
	@ResponseStatus(code=HttpStatus.OK)
	public List<Producto> filtrarProductos (@PathVariable String term) {
		return clienteService.findProductoByName(term);
	}
	
	@Secured({"ROLE_ADMIN"})
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crearFactura(@RequestBody Factura factura) {
		return clienteService.saveFactura(factura);
	}
	
}
