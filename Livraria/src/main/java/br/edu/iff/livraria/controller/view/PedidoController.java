package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@GetMapping
	public String listarPedidos(Model model) {
		List<Pedido> pedidos = pedidoService.listarPedidos();
		model.addAttribute("pedidos", pedidos);
		return "listarPedidos";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioAdicionar() {
		return "adicionarPedido";
	}

	@PostMapping("/adicionar")
	public String adicionarPedido(@Valid @RequestParam Long clienteId, @Valid @RequestParam String formaPagamento,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "adicionarPedido";
		}

		String mensagem = pedidoService.adicionarPedido(clienteId, formaPagamento);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/adicionar";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditar(@PathVariable Long id, Model model) {
		Pedido pedido = pedidoService.buscarPorId(id);
		model.addAttribute("pedido", pedido);
		return "editarPedido";
	}

	@PostMapping("/editar/{id}")
	public String editarPedido(@PathVariable Long id, @Valid @RequestParam Long clienteId,
			@Valid @RequestParam float valorTotal, @Valid @RequestParam LocalDateTime dataPedido,
			@Valid @RequestParam LocalDateTime dataEntrega, @Valid @RequestParam String formaPagamento,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "editarPedido";
		}

		String mensagem = pedidoService.atualizarPedido(id, clienteId, valorTotal, dataPedido, dataEntrega,
				formaPagamento);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/editar/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluirPedido(@PathVariable Long id, Model model) {
		String mensagem = pedidoService.deletarPedido(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/excluir/" + id;
	}

	@GetMapping("/{id}/itens")
	public String listarItensDoPedido(@PathVariable Long id, Model model) {
		List<Item> itens = pedidoService.listarItensDoPedido(id);
		model.addAttribute("itens", itens);
		return "listarItensDoPedido";
	}

	@GetMapping("/{id}/adicionarItem")
	public String exibirFormularioAdicionarItem(@PathVariable Long id) {
		return "adicionarItemAoPedido";
	}

	@PostMapping("/{id}/adicionarItem")
	public String adicionarItemAoPedido(@PathVariable Long id, @Valid @RequestParam Long livroId,
			@Valid @RequestParam int quantidade, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "adicionarItemAoPedido";
		}

		String mensagem = pedidoService.adicionarItemAoPedido(id, livroId, quantidade);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/editar/" + id;
	}

	@GetMapping("/{pedidoId}/deletarItem/{itemId}")
	public String deletarItemDoPedido(@PathVariable Long pedidoId, @PathVariable Long itemId, Model model) {
		String mensagem = pedidoService.deletarItemDoPedido(pedidoId, itemId);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/editar/" + pedidoId;
	}

	@GetMapping("/{id}/finalizar")
	public String finalizarPedido(@PathVariable Long id, Model model) {
		String mensagem = pedidoService.finalizarPedido(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/pedido/editar/" + id;
	}
}
