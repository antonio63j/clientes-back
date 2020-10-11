package com.afl.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author antonio
 *
 */
@Entity
@Table(name="clientes")

public class Cliente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	@NotEmpty(message="Ojo, el campo 'nombre' es requerido")
	@Size(min = 4, max=10,message="El numero de posiciones para 'nombre' es de 4 a 10")
	private String nombre;
	
	@NotEmpty
	@Size(min = 5, max=11)
	private String apellido;
	
	@Column(nullable=false, unique=false) 
	@NotEmpty
	@Email
	private String email;
	
	@NotNull(message="no puede estar vacio")
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;

	@Column(unique=true) 
	private String foto;
	
	@NotNull (message="no puede estar vacio")
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn (name="region_id")
	@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
	private Region region;
	
    // Para evitar problemas de recursion la directiva JsonIgnoreProperties debe ir así. tener en cuenta tambien en modelo cliente
	// Tambien es posible evitar la recursion desde el front, para ello asignar null en atributo factura del modelo cliente a la hora
	// de actualizar el cliente
	@JsonIgnoreProperties(value={"cliente", "hibernateLazyInitializer", "handler"}, allowSetters=true)
	@OneToMany(fetch=FetchType.LAZY, mappedBy="cliente", cascade=CascadeType.ALL)
	private List<Factura> facturas;
	
	public Cliente() {
	   this.facturas = new ArrayList<>();
	}
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	/* Se pasa al front
	 * @PrePersist public void prePersist () { createAt = new Date(); }
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public List<Factura> getFacturas() {
		return facturas;
	}
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	
}
