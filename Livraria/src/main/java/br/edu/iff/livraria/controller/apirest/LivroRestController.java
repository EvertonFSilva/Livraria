package br.edu.iff.livraria.controller.apirest;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/livro")
public class LivroRestController {

	@Autowired
	private LivroService livroService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um livro em específico")
	public ResponseEntity<String> adicionarLivro(@RequestParam String titulo, @RequestParam String autor,
			@RequestParam String genero, @RequestParam int qtdPaginas, @RequestParam float preco) {
		try {
			String mensagem = livroService.adicionarLivro(titulo, autor, genero, qtdPaginas, preco);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar livro.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um livro em específico")
	public ResponseEntity<String> atualizarLivro(@PathVariable Long id, @RequestParam String titulo,
			@RequestParam String autor, @RequestParam String genero, @RequestParam int qtdPaginas,
			@RequestParam float preco) {
		try {
			String mensagem = livroService.atualizarLivro(id, titulo, autor, genero, qtdPaginas, preco);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar livro.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um livro em específico")
	public ResponseEntity<String> deletarLivro(@PathVariable Long id) {
		try {
			String mensagem = livroService.deletarLivro(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar livro.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um livro em específico")
	public ResponseEntity<Livro> buscarLivro(@PathVariable Long id) {
		try {
			Livro livro = livroService.buscarPorId(id);
			return ResponseEntity.ok(livro);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os livros")
	public ResponseEntity<List<Livro>> listarLivros() {
		List<Livro> livros = livroService.listarLivros();
		return ResponseEntity.ok(livros);
	}
}