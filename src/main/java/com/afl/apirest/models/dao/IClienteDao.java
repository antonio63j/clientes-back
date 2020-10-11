package com.afl.apirest.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.afl.apirest.models.entity.Cliente;
import com.afl.apirest.models.entity.Region;

// Documentacion en https://docs.spring.io/spring-data/jpa/docs

public interface IClienteDao extends JpaRepository<Cliente, Long> {
// JpaRepository ya viene con transaccionabiliad pero para los métodos propios habría que añadir la notacion @Transactional
	
	@Query ("from Region")
	public List<Region> findAllRegiones(); 
}
