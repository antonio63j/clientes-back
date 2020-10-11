package com.afl.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afl.apirest.models.entity.Cliente;
import com.afl.apirest.models.entity.Region;
import com.afl.apirest.models.services.IClienteService;
import com.afl.apirest.models.services.IUploadFileService;

@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8070", "*" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;

	private Logger log = LoggerFactory.getLogger(ClienteRestController.class);
	
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		return clienteService.findAll(PageRequest.of(page, 4));
	}

	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "error en el acceso a la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cliente == null) {
			response.put("mensaje", "el cliente con id=".concat(id.toString().concat(" no está en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
    
	// Para validar el objeto especificado en @RequesBody utilizamos el interceptor @Valic
	@Secured("ROLE_ADMIN")
	@PostMapping("/clientes")
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		/* sin stream
		 * if (result.hasErrors()) { 
		 * List<String> errors = new ArrayList<>(); 
		 * for (FieldError err: result.getFieldErrors()) { 
		 * errors.add("El campo '"+ err.getField() + "' " + err.getDefaultMessage()); } }
		 */
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
				.map(fielderror -> "El campo '"+ fielderror.getField() + "' " + fielderror.getDefaultMessage())
				.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
						
		try {
			clienteNew = clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "error en el acceso a la base de datos, no ha sido posible crear el cliente");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "cliente creado en la base de datos");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Secured({"ROLE_ADMIN"})
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {

		Cliente clientUpdated = null;
		Cliente clientActual = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
				.map(fielderror -> "El campo '"+ fielderror.getField() + "' " + fielderror.getDefaultMessage())
				.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		clientActual = clienteService.findById(id);
		if (clientActual == null) {
			response.put("mensaje",	"el cliente con id=".concat(id.toString().concat(" no está en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clientActual.setNombre(cliente.getNombre());
			clientActual.setApellido(cliente.getApellido());
			clientActual.setEmail(cliente.getEmail());
			clientActual.setCreateAt(cliente.getCreateAt());
			clientActual.setRegion(cliente.getRegion());
			clientUpdated = clienteService.save(clientActual);
			 
		} catch (DataAccessException e) {
			response.put("mensaje", "error al actualizar el cliente con id=".concat(id.toString()));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "sin error al actualizar el cliente con id=".concat(id.toString()));
		response.put("cliente", clientUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@Secured({"ROLE_ADMIN"})
	@DeleteMapping("/clientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			Cliente cliente = clienteService.findById(id);
			String nombreFotoAnterior = cliente.getFoto();
            uploadFileService.eliminar(nombreFotoAnterior);	
			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "cliente id=".concat(id.toString().concat(" error al eliminar en la base de datos")));
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "cliente id=".concat(id.toString().concat(" eliminado de la base de datos")));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ADMIN","ROLE_USER"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam ("archivo") MultipartFile archivo, @RequestParam ("id") Long id) {
		
		Cliente cliente;
		Map<String, Object> response = new HashMap<>();
		
		cliente = clienteService.findById(id);
		if (cliente == null) {
			response.put("mensaje",	"el cliente con id=".concat(id.toString().concat(" no está en la base de datos")));
			response.put("error", "el cliente con id=".concat(id.toString().concat(" no está en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		if (!archivo.isEmpty()) {
 	    	String nombreArchivo = null;
 	    	try {
 	    		nombreArchivo = uploadFileService.copia(archivo);
			} catch (IOException e) {
				e.printStackTrace();
 				response.put("mensaje",	"cliente con id=".concat(id.toString().concat(" error al subir imagen")));
				response.put("error", "IOException");
	 			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
 	    	
 	    	String nombreFotoAnterior = cliente.getFoto();
 	    	uploadFileService.eliminar(nombreFotoAnterior);
 	    	
 			cliente.setFoto(nombreArchivo);
 			clienteService.save(cliente);
 			response.put("cliente", cliente);
 			response.put("mensaje", "cliente id=".concat(id.toString().concat(" upload OK")));
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

		Resource resource = null;
		try {
			resource = uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		HttpHeaders cabecera = new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
		return new ResponseEntity<Resource>(resource, cabecera, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/clientes/regiones")
	public List<Region> findAllRegiones() {
		return clienteService.findAllRegiones();
	}
}
