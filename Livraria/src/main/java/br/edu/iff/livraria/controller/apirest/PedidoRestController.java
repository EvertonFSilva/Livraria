package br.edu.iff.livraria.controller.apirest;

import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedido")
public class PedidoRestController {

	@Autowired
	private PedidoService pedidoService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um pedido em específico")
	public ResponseEntity<String> adicionarPedido(@RequestParam Long clienteId, @RequestParam String formaPagamento)
			throws Exception {
		try {
			Pedido resultado = pedidoService.adicionarPedido(clienteId, formaPagamento);
			return ResponseEntity.ok(resultado != null ? "Pedido adicionado. Id: " + resultado.getId()
					: "Não foi possível criar um pedido.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar pedido.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um pedido em específico")
	public ResponseEntity<String> atualizarPedido(@PathVariable("id") Long id, @RequestParam Long clienteId,
			@RequestParam float valorTotal,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataPedido,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataEntrega,
			@RequestParam String formaPagamento, boolean status) throws Exception {

		try {
			String mensagem = pedidoService.atualizarPedido(id, clienteId, valorTotal, dataPedido, dataEntrega,
					formaPagamento, status);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar pedido.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um pedido em específico")
	public ResponseEntity<String> deletarPedido(@PathVariable("id") Long id) throws Exception {
		try {
			String mensagem = pedidoService.deletarPedido(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar pedido.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um pedido em específico")
	public ResponseEntity<Pedido> buscarPedido(@PathVariable("id") Long id) throws Exception {
		try {
			Pedido pedido = pedidoService.buscarPorId(id);
			return ResponseEntity.ok(pedido);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PatchMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Finalizar um pedido em específico")
	public ResponseEntity<String> finalizarPedido(@PathVariable("id") Long id) throws Exception {
		try {
			String mensagem = pedidoService.finalizarPedido(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao finalizar pedido.");
		}
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os pedidos")
	public ResponseEntity<List<Pedido>> listarPedidos() throws Exception {
		List<Pedido> pedidos = pedidoService.listarPedidos();
		return ResponseEntity.ok(pedidos);
	}

	@PostMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Adicionar um item ao pedido em um cliente em específico")
	public ResponseEntity<String> adicionarItem(@PathVariable("id") Long id, @RequestParam Long livroId,
			@RequestParam int quantidade) throws Exception {
		try {
			String mensagem = pedidoService.adicionarItemAoPedido(id, livroId, quantidade);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar item ao pedido.");
		}
	}

	@DeleteMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Deletar um livro do pedido em um cliente em específico")
	public ResponseEntity<String> deletarItem(@PathVariable("id") Long id, @RequestParam Long itemId) throws Exception {
		try {
			String mensagem = pedidoService.deletarItemDoPedido(id, itemId);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar item do pedido.");
		}
	}

	@GetMapping("/{id}/itens")
	@ResponseBody
	@Operation(summary = "Listar os itens do pedido de um cliente em específico")
	public ResponseEntity<List<Item>> listarItens(@PathVariable("id") Long id) throws Exception {
		try {
			List<Item> itens = pedidoService.listarItensDoPedido(id);
			return ResponseEntity.ok(itens);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
