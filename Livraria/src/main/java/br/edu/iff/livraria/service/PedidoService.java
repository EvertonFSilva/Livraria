package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

	public String adicionarPedido(Long clienteId) {
		Cliente clienteExistente = clienteService.buscarCliente(clienteId);
		if (clienteExistente != null) {
			if (pedidoRepository.buscarPedidosEmProgresso(clienteId).size() != 0) {
				Pedido novoPedido = new Pedido(clienteService.buscarCliente(clienteId));
				clienteExistente.adicionarPedido(novoPedido);
				pedidoRepository.save(novoPedido);
				pedidoRepository.flush();
			} else {
				return "Cliente já tem uma compra em progresso.";
			}
		}

		return "Cliente não encontrado.";
	}

	public String atualizarPedido(Long pedidoId, Long clienteId, float valorTotal, Date dataPedido, Date dataEntrega,
			String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarCliente(clienteId);
		if (clienteExistente != null) {

			Pedido pedidoExistente = buscarPorId(pedidoId);

			if (pedidoExistente != null) {
				if (!pedidoExistente.getFinalizado()) {
					pedidoExistente.setValorTotal(valorTotal);
					pedidoExistente.setDataPedido(dataPedido);
					pedidoExistente.setDataEntrega(dataEntrega);
					pedidoExistente.setFormaPagamento(formaPagamento);
					pedidoRepository.save(pedidoExistente);
					pedidoRepository.flush();
					return "Pedido atualizado com sucesso.";
				} else {
					return "Compra já finalizada.";
				}
			} else {
				return "Pedido não encontrado.";
			}
		}
		return "Cliente não encontrado.";
	}

	public String deletarPedido(Long id) {
		Pedido pedidoExistente = buscarPorId(id);

		if (pedidoExistente != null) {
			Cliente clienteExistente = pedidoExistente.getCliente();
			if (clienteExistente != null) {
				clienteExistente.removerPedido(pedidoExistente);
				return "Pedido deletado com sucesso.";
			} else {
				return "Cliente não encontrado.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public List<Pedido> listarPedidos() {
		return pedidoRepository.listarPedidos();
	}

	public String adicionarItemAoPedido(Long pedidoId, String titulo, int quantidade, int tipo) {

		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			
	    	Livro livro =  livroService.buscarPorTitulo(titulo);
	    	Item item = new Item(livro, quantidade, tipo, tipo == 1 ? livro.getPrecoAluguel() : livro.getPrecoVenda());

			boolean itemExistente = itemService.adicionarItem(item);
			if (itemExistente) {
				pedidoExistente.adicionarItem(item);
				return "Item adicionado.";
			} else {
				return "Item já adicionado.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public String deletarItemDoPedido(Long pedidoId, Long itemId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			if (!pedidoExistente.getFinalizado()) {

				Item itemParaRemover = pedidoExistente.getItemById(itemId);

				if (itemParaRemover != null) {
					pedidoExistente.getItens().remove(itemParaRemover);
					itemService.deletarItem(itemId);
					return "Item removido do pedido com sucesso.";
				} else {
					return "Item não encontrado no pedido.";
				}
			} else {
				return "Compra já finalizada.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public String adicionarAluguelAoItem(Long pedidoId, Long itemId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			Item itemExistente = pedidoExistente.getItemById(itemId);

			if (itemExistente != null) {
				boolean aluguelAdicionado = itemService.adicionarAluguel(itemExistente);
				if (aluguelAdicionado) {
					return "O aluguel foi adicionado ao item.";
				} else {
					return "O aluguel não foi adicionado ao item.";
				}
			} else {
				return "Item não encontrado no pedido.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public String deletarAluguelDoItem(Long pedidoId, Long itemId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			Item itemExistente = pedidoExistente.getItemById(itemId);

			if (itemExistente != null) {
				boolean aluguelRemovido = itemService.deletarAluguel(itemExistente.getAluguel().getId());
				if (aluguelRemovido) {
					return "O aluguel foi removido do item.";
				} else {
					return "O aluguel não foi removido do item.";
				}
			} else {
				return "Item não encontrado no pedido.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public String buscarAluguel(Long pedidoId, Long itemId, Long aluguelId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			Item itemExistente = pedidoExistente.getItemById(itemId);

			if (itemExistente != null) {
				Aluguel aluguel = itemService.buscarAluguel(aluguelId);

				if (aluguel != null) {
					return "Aluguel encontrado: " + aluguel.toString();
				} else {
					return "Aluguel não encontrado.";
				}
			} else {
				return "Item não encontrado no pedido.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}

	public List<Aluguel> listarAlugueisPorCliente(Long pedidoId, Long clienteId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			return itemService.listarAlugueisPorCliente(clienteId);
		}
		return null;
	}

	public String finalizarPedido(Long pedidoId) {
		Pedido pedidoExistente = buscarPorId(pedidoId);

		if (pedidoExistente != null) {
			if (!pedidoExistente.getFinalizado()) {
				if (pedidoExistente.getItens().size() > 0) {
					pedidoExistente.finalizar();
					pedidoRepository.flush();
					return "Compra finalizada.";
				} else {
					return "A compra precisa ter pelo menos 1 item.";
				}
			} else {
				return "Compra já finalizada.";
			}
		} else {
			return "Pedido não encontrado.";
		}
	}
	
    public List<Item> listarItensDoPedido(Long pedidoId) {
        Pedido pedidoExistente = buscarPorId(pedidoId);

        if (pedidoExistente != null) {
            return pedidoExistente.getItens();
        }
        return null;
    }
}
