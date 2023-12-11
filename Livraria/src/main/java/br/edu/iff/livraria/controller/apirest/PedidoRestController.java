package br.edu.iff.livraria.controller.apirest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1/pedido")

public class PedidoRestController {

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um pedido em expecifíco")
	public String adicionarPedido(Long id) {
		return "Pedido adicionado.";
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um pedido em expecifíco")
	public String atualizarPedido(@PathVariable("id") Long id) {
		return "Pedido atualizado.";
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um pedido em expecifíco")
	public String deletarPedido(@PathVariable("id") Long id) {
		return "Pedido deletado.";
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um pedido em expecifíco")
	public String buscarPedido(@PathVariable("id") Long id) {
		return "Pedido retornado.";
	}

	@PatchMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Finalizar um pedido em expecifíco")
	public String finalizarPedido(@PathVariable("id") Long id) {
		return "Pedido finalizado.";
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os pedidos")
	public String listarPedidos() {
		return "Pedidos listados.";
	}

	@PostMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Adicionar um item ao pedido em um cliente em expecifíco")
	public String adicionarItem(@PathVariable("id") Long id, Long iId) {
		return "Item adicionado ao pedido.";
	}

	@DeleteMapping("/{id}/item")
	@ResponseBody
	@Operation(summary = "Deletar um livro do pedido em um cliente em expecifíco")
	public String deletarItem(@PathVariable("id") Long id, Long iId) {
		return "Item deletado do pedido.";
	}

	@GetMapping("/{id}/itens")
	@ResponseBody
	@Operation(summary = "Listar os itens do pedido de um cliente em expecifíco")
	public String listarItens(@PathVariable("id") Long id) {
		return "Lista de itens do pedido.";
	}

	@PostMapping("/{id}/fpagamento")
	@ResponseBody
	@Operation(summary = "Adicionar a forma de pagamento em um pedido em expecifíco")
	public String adicionarFormaDePagamento(@PathVariable("id") Long id, Long pId, Long fpId, String tipo) {
		return "Forma de pagamento adicionada ao pedido do cliente.";
	}

	@PutMapping("/{id}/fpagamento")
	@ResponseBody
	@Operation(summary = "Atualizar a forma de pagamento em um pedido em expecifíco")
	public String atualizarFormaDePagamento(@PathVariable("id") Long id, Long pId, Long fpId, String tipo) {
		return "Forma de pagamento atualizada.";
	}

}
