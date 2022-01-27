package com.blogPessoal.blogPessoal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blogPessoal.blogPessoal.model.Usuario;

@Repository
public interface UsuarioRepsitory extends JpaRepository<Usuario, Long>{
	public Optional<Usuario> findByUsuario(String usuario);
	
	//método para sessão testes
	public List<Usuario> findAllByNomeContainingIgnoreCase(String nome);
}
