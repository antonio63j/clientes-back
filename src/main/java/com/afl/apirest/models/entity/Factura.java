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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "facturas")

public class Factura implements Serializable{
	
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
    // Para evitar problemas de recursion la directiva JsonIgnoreProperties debe ir así. tener en cuenta tambien en modelo facturas
	@JsonIgnoreProperties(value={"facturas", "hibernateLazyInitializer", "handler"}, allowSetters=true)
	@ManyToOne (fetch=FetchType.LAZY)
    private Cliente cliente;
    
    private String descripcion;
    
	private String observacion;
    
    @Column(name="create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;
    
    @JsonIgnoreProperties({"facturas", "hibernateLazyInitializer", "handler"})
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "factura_id")
	private List<ItemFactura> itemsFactura;
	
	
    public Factura() {
		this.itemsFactura = new ArrayList<>();
	}

	@PrePersist
    public void prePersist() {
    	this.createAt = new Date();
    }
    
	public Double getTotal() {
		Double total = 0.0;
		for( ItemFactura item : itemsFactura) {
			total = total + item.getImporte();
		}
		return total;
	}
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String obeservacion) {
		this.observacion = obeservacion;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

    
	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	
	public List<ItemFactura> getItemsFactura() {
		return itemsFactura;
	}

	public void setItemsFactura(List<ItemFactura> itemsFactura) {
		this.itemsFactura = itemsFactura;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
