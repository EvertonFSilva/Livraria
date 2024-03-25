package br.edu.iff.livraria.controller.apirest;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.service.AluguelService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/aluguel")
public class AluguelRestController {

	@Autowired
	private AluguelService aluguelService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um aluguel em específico")
	public ResponseEntity<String> adicionarAluguel(@RequestParam Long clienteId, @RequestParam String formaPagamento)
			throws Exception {
		try {
			Aluguel resultado = aluguelService.adicionarAluguel(clienteId, formaPagamento);
			return ResponseEntity.ok(resultado != null ? "Aluguel adicionado. Id: " + resultado.getId()
					: "Não foi possível criar um aluguel.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar aluguel.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um aluguel em específico")
	public ResponseEntity<String> atualizarAluguel(@PathVariable Long id, @RequestParam Long clienteId,
			@RequestParam float valorTotal,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataEntrega,
			@RequestParam String formaPagamento, boolean status) throws Exception {

		try {
			String mensagem = aluguelService.atualizarAluguel(id, clienteId, valorTotal, dataInicio, dataFim,
					dataEntrega, formaPagamento, status);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar aluguel.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um aluguel em específico")
	public ResponseEntity<String> deletarAluguel(@PathVariable Long id) throws Exception {
		try {
			String mensagem = aluguelService.deletarAluguel(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar aluguel.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um aluguel em específico")
	public ResponseEntity<Aluguel> buscarAluguel(@PathVariable Long id) throws Exception {
		try {
			Aluguel aluguel = aluguelService.buscarPorId(id);
			return ResponseEntity.ok(aluguel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PatchMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Finalizar um aluguel em específico")
	public ResponseEntity<String> finalizarAluguel(@PathVariable Long id) throws Exception {
		try {
			String mensagem = aluguelService.finalizarAluguel(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao finalizar aluguel.");
		}
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os alugueis")
	public ResponseEntity<List<Aluguel>> listarAlugueis() throws Exception {
		try {
			List<Aluguel> alugueis = aluguelService.listarAlugueis();
			return ResponseEntity.ok(alugueis);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Adicionar um item ao aluguel em um cliente em específico")
	public ResponseEntity<String> adicionarItem(@PathVariable Long id, @RequestParam Long livroId,
			@RequestParam int quantidade) throws Exception {
		try {
			String mensagem = aluguelService.adicionarItemAoAluguel(id, livroId, quantidade);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar item ao aluguel.");
		}
	}

	@DeleteMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Deletar um livro do aluguel em um cliente em específico")
	public ResponseEntity<String> deletarItem(@PathVariable Long id, @RequestParam Long itemId) throws Exception {
		try {
			String mensagem = aluguelService.deletarItemDoAluguel(id, itemId);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar item do aluguel.");
		}
	}

	@GetMapping("/{id}/itens")
	@ResponseBody
	@Operation(summary = "Listar os itens do aluguel de um cliente em específico")
	public ResponseEntity<List<Item>> listarItens(@PathVariable Long id) throws Exception {
		try {
			List<Item> itens = aluguelService.listarItensDoAluguel(id);
			return ResponseEntity.ok(itens);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}