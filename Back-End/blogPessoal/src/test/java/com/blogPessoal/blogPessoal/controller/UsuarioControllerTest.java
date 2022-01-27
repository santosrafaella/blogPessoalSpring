package com.blogPessoal.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogPessoal.blogPessoal.model.Usuario;
import com.blogPessoal.blogPessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)// se a porta 8080 estiver em uso, ele vai pegar uma porta aleatoria para testar
@TestInstance(TestInstance.Lifecycle.PER_CLASS)// faz o teste por classe, 
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//organizado uma classe de cada vez
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, //L de Long, que é não aceita nullo
			"Paulo Antunes", "paulo_antunes@email.com.br", "13465278"));

		ResponseEntity<Usuario> resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);//caminho da URI, que é a verificação se foi enviado corretamente

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());//status Http, no caso do tipo 200
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());//comparando se body é igual ao nome
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {//forçar para ver se não aceita duplicação de dados

		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278"));

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
			"Maria da Silva", "maria_silva@email.com.br", "13465278"));

		ResponseEntity<Usuario> resposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());//se tiver duplicação, ele retorna um bad request
	}

	@Test
	@Order(3)
	@DisplayName("Alterar um Usuário")
	public void deveAtualizarUmUsuario() {

		Optional<Usuario> usuarioCreate = usuarioService.CadastrarUsuario(new Usuario(0L,
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123"));

		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
			"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

		ResponseEntity<Usuario> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {

		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123"));
		
		usuarioService.CadastrarUsuario(new Usuario(0L, 
			"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123"));

		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);//exchange cria a requisição http

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

}