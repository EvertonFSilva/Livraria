package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private LivroService livroService;

	public Pedido buscarPorId(Long id) {
		return pedidoRepository.buscarPorId(id);
	}

	public String adicionarPedido(Long clienteId, String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);
		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		List<Pedido> pedidosEmProgresso = pedidoRepository.buscarPedidosEmProgresso(clienteId);
		if (!pedidosEmProgresso.isEmpty()) {
			return "Cliente já tem um pedido em progresso.";
		}

		Pedido novoPedido = new Pedido(clienteExistente, formaPagamento);
		clienteExistente.adicionarPedido(novoPedido);
		pedidoRepository.saveAndFlush(novoPedido);
		return "Pedido adicionado. Id: " + novoPedido.getId();
	}

	public String atualizarPedido(Long pedidoId, Long clienteId, float valorTotal, LocalDateTime dataPedido,
			LocalDateTime dataEntrega, String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);
		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		Pedido pedidoExistente = buscarPorId(pedidoId);
		if (pedidoExistente == null || !pedidoExistente.getCliente().getId().equals(clienteId)) {
			return "Pedido não encontrado ou não pertence a esse Cliente.";
		}

		if (pedidoExistente.isFinalizado()) {
			return "Pedido já finalizado.";
		}
		
	    if (valorTotal > 0) {
	        pedidoExistente.setValorTotal(valorTotal);
	    }
	    if (dataPedido != null) {
	        pedidoExistente.setDataPedido(dataPedido);
	    }
	    if (dataEntrega != null) {
	        pedidoExistente.setDataEntrega(dataEntrega);
	    }
	    if (!formaPagamento.isEmpty()) {
	        pedidoExistente.setFormaPagamento(formaPagamento);
	    }
	    pedidoRepository.saveAndFlush(pedidoExistente);
		return "Pedido atualizado com sucesso.";
	}

	public String deletarPedido(Long id) {
		Pedido pedidoExistente = buscarPorId(id);

		if (pedidoExistente == null) {
			return "Pedido não encontrado.";
		}

		Cliente clienteExistente = pedidoExistente.getCliente();
		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		List<Pedido> pedidosEmProgresso = pedidoRepository.buscarPedidosEmProgresso(clienteExistente.getId());
		if (pedidosEmProgresso.size() != 1) {
			return "O cliente não tem nenhum pedido.";
		}

		clienteExistente.removerPedido(pedidoExistente);
		pedidoRepository.delete(pedidoExistente);
		return "Pedido deletado com sucesso.";
	}

	public List<Pedido> listarPedidos() {
		return pedidoRepository.listarPedidos();
	}

	public String adicionarItemAoPedido(Long pedidoId, Long livroId, int quantidade) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente == null) {
			return "Pedido não encontrado.";
		}

		Livro livroExistente = livroService.buscarPorId(livroId);
		if (livroExistente == null) {
			return "Esse livro não existe.";
		}

		boolean itemExistente = pedidoExistente.getItens().stream()
				.anyMatch(item -> item.getLivro().getId().equals(livroId));

		if (itemExistente) {
			return "Item já adicionado ao Pedido.";
		}

		Item item = new Item(livroExistente, quantidade);
		boolean itemAdicionado = itemService.adicionarItem(item);

		if (itemAdicionado) {
			pedidoExistente.adicionarItem(item);
			pedidoRepository.saveAndFlush(pedidoExistente);
			return "Item adicionado ao pedido. ItemId: " + item.getId();
		} else {
			return "Erro ao adicionar o item.";
		}
	}

	public String deletarItemDoPedido(Long pedidoId, Long itemId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente == null) {
			return "Pedido não encontrado.";
		}

		if (pedidoExistente.isFinalizado()) {
			return "Pedido já finalizado.";
		}

		boolean itemExistente = pedidoExistente.getItens().stream().anyMatch(item -> item.getId().equals(itemId));

		if (!itemExistente) {
			return "Item não existe no pedido.";
		}

		Item itemParaRemover = itemService.buscarPorId(itemId);

		if (itemParaRemover == null) {
			return "Item não encontrado no pedido.";
		}

		pedidoExistente.removerItem(itemParaRemover);
		itemService.deletarItem(itemId);

		if (pedidoExistente.getItens().isEmpty()) {
			pedidoRepository.delete(pedidoExistente);
			return "Item removido do pedido e pedido fechado.";
		} else {
			pedidoRepository.saveAndFlush(pedidoExistente);
			return "Item removido do pedido com sucesso.";
		}
	}

	public String finalizarPedido(Long pedidoId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente == null) {
			return "Pedido não encontrado.";
		}

		if (pedidoExistente.isFinalizado()) {
			return "Pedido já finalizado.";
		}

		if (pedidoExistente.getItens().isEmpty()) {
			return "O pedido precisa ter pelo menos 1 item.";
		}

		pedidoExistente.finalizar();
		pedidoRepository.saveAndFlush(pedidoExistente);
		return "Pedido finalizado.";
	}

	public List<Item> listarItensDoPedido(Long pedidoId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);
		return (pedidoExistente != null) ? pedidoExistente.getItens() : null;
	}
	
	public List<Livro> listarLivrosDoPedido(Long pedidoId) {		
		List<Livro> listaLivros = new ArrayList<>();
		Pedido pedido = buscarPorId(pedidoId);

		if (pedido != null && !pedido.getItens().isEmpty()) {
			for (Item item : pedido.getItens()) {
				Livro livro = item.getLivro();
				if (livro != null) {
					listaLivros.add(livro);
				}
			}
		}
		return listaLivros;
	}
}
