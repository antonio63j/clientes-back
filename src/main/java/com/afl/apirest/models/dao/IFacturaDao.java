package com.afl.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.afl.apirest.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long>{

}
