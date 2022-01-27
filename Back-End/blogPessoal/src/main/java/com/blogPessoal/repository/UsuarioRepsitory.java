package com.blogPessoal.blogPessoal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogPessoal.blogPessoal.model.Usuario;

public interface UsuarioRepsitory extends JpaRepository<Usuario, Long>{
	public Optional<Usuario> findByUsuario(String usuario);
}
