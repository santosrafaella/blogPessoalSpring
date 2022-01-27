package com.blogPessoal.blogPessoal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blogPessoal.blogPessoal.model.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> { //falando pro temarepository se externder para jpa repository e usar as ferramentas do jparepostory
	public List<Tema> findAllByDescricaoContainingIgnoreCase(String descricao);
	
}
