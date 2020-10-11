package com.afl.apirest.models.services;

import com.afl.apirest.models.entity.Usuario;

public interface IUsuarioService {

	public Usuario findByUsername (String username);
}
