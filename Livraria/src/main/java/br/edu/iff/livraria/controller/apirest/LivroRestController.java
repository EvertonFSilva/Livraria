package br.edu.iff.livraria.controller.apirest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1/livro")
public class LivroRestController {

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um livro em expecifíco")
	public String adicionarLivro(Long id, String titulo, String autor, String genero, int qtdPags, float precoVenda, float precoAluguel) {
		return "livro adicionado.";
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um livro em expecifíco")
	public String atualizarLivro(@PathVariable("id") Long id, String titulo, String autor, String genero, int qtdPags, float precoVenda, float precoAluguel) {
		return "Livro atualizado.";
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um livro em expecifíco")
	public String deletarLivro(@PathVariable("id") Long id) {
		return "Livro deletado.";
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um livro em expecifíco")
	public String buscarLivro(@PathVariable("id") Long id) {
		return "Livro retornado.";
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os livros")
	public String listarLivros() {
		return "Livros listados.";
	}
	
}