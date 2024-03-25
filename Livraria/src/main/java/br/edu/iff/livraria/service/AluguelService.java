package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.repository.AluguelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AluguelService {

	@Autowired
	private AluguelRepository aluguelRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private LivroService livroService;

	public Aluguel buscarPorId(Long id) {
		return aluguelRepository.buscarPorId(id);
	}

	public List<Aluguel> buscarAlugueisEmProgresso(Long clienteId) {
		return aluguelRepository.buscarAlugueisEmProgresso(clienteId);
	}

	public List<Aluguel> buscarAlugueisDoCliente(Long clienteId) {
		return aluguelRepository.buscarAlugueisDoCliente(clienteId);
	}

	public List<Aluguel> buscarAlugueisFinalizados(Long clienteId) {
		return aluguelRepository.buscarAlugueisFinalizados(clienteId);
	}

	public Aluguel adicionarAluguel(Long clienteId, String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);

		if (clienteExistente == null) {
			return null;
		}

		List<Aluguel> alugueisEmProgresso = aluguelRepository.buscarAlugueisEmProgresso(clienteId);
		if (!alugueisEmProgresso.isEmpty()) {
			return null;
		}

		Aluguel novoAluguel = new Aluguel(clienteExistente, formaPagamento);
		clienteExistente.adicionarAluguel(novoAluguel);
		aluguelRepository.saveAndFlush(novoAluguel);
		return novoAluguel;
	}

	public String atualizarAluguel(Long aluguelId, Long clienteId, float valorTotal, LocalDateTime dataInicio,
			LocalDateTime dataFim, LocalDateTime dataEntrega, String formaPagamento, boolean status) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null || !aluguelExistente.getCliente().getId().equals(clienteId)) {
			return "Aluguel não encontrado ou não pertence a esse Cliente.";
		}

		if (valorTotal > 0) {
			aluguelExistente.setValorTotal(valorTotal);
		}
		if (dataInicio != null) {
			aluguelExistente.setDataInicio(dataInicio);
		}
		if (dataFim != null) {
			aluguelExistente.setDataFim(dataFim);
		}
		if (dataEntrega != null) {
			aluguelExistente.setDataEntrega(dataEntrega);
		}
		if (!formaPagamento.isEmpty()) {
			aluguelExistente.setFormaPagamento(formaPagamento);
		}
		
		List<Aluguel> alugueisEmAberto = aluguelRepository.buscarAlugueisEmProgresso(clienteId);
		if (alugueisEmAberto.isEmpty() || !aluguelExistente.isFinalizado()) {
			aluguelExistente.setFinalizado(status);
		}

		aluguelRepository.saveAndFlush(aluguelExistente);
		return "Aluguel atualizado com sucesso.";
	}

	public String deletarAluguel(Long id) {
		Aluguel aluguelExistente = buscarPorId(id);

		if (aluguelExistente == null) {
			return "Aluguel não encontrado.";
		}

		Cliente clienteExistente = aluguelExistente.getCliente();
		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		List<Aluguel> alugueisEmProgresso = aluguelRepository.buscarAlugueisDoCliente(clienteExistente.getId());
		if (alugueisEmProgresso.size() == 0) {
			return "O cliente não tem nenhum aluguel.";
		}

		List<Item> itensDoAluguel = alugueisEmProgresso.get(0).getItens();
		for (Item item : itensDoAluguel) {
			itemService.deletarItem(item.getId());
		}

		clienteExistente.removerAluguel(aluguelExistente);
		aluguelRepository.delete(aluguelExistente);
		return "Aluguel deletado com sucesso.";
	}

	public List<Aluguel> listarAlugueis() {
		return aluguelRepository.listarAlugueis();
	}

	public String adicionarItemAoAluguel(Long aluguelId, Long livroId, int quantidade) {
		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null) {
			return "Aluguel não encontrado.";
		}

		Livro livroExistente = livroService.buscarPorId(livroId);

		if (livroExistente == null) {
			return "Esse livro não existe.";
		}

		Item itemExistente = aluguelExistente.getItemByLivroId(livroId);
		if (itemExistente != null) {
			itemExistente.setQuantidade(itemExistente.getQuantidade() + quantidade);
			aluguelExistente.setValorTotal(aluguelExistente.getValorTotal() + (livroExistente.getPreco() * quantidade));
			aluguelRepository.saveAndFlush(aluguelExistente);
			return "Quantidade do item atualizada no aluguel.";
		} else {
			Item item = new Item(livroExistente, quantidade);
			boolean itemAdicionado = itemService.adicionarItem(item);

			if (itemAdicionado) {
				aluguelExistente.adicionarItem(item);
				aluguelRepository.saveAndFlush(aluguelExistente);
				return "Item adicionado ao aluguel. ItemId: " + item.getId();
			} else {
				return "Erro ao adicionar o item.";
			}
		}
	}

	public String deletarItemDoAluguel(Long aluguelId, Long itemId) {
		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null) {
			return "Aluguel não encontrado.";
		}

		if (aluguelExistente.isFinalizado()) {
			return "Aluguel já finalizado.";
		}

		boolean itemExistente = aluguelExistente.getItens().stream().anyMatch(item -> item.getId().equals(itemId));

		if (!itemExistente) {
			return "Item não existe no aluguel.";
		}

		Item itemParaRemover = itemService.buscarPorId(itemId);

		if (itemParaRemover == null) {
			return "Item não encontrado no aluguel.";
		}

		aluguelExistente.removerItem(itemParaRemover);
		itemService.deletarItem(itemId);

		if (aluguelExistente.getItens().isEmpty()) {
			aluguelRepository.delete(aluguelExistente);
			return "Item removido do aluguel e aluguel fechado.";
		} else {
			aluguelRepository.saveAndFlush(aluguelExistente);
			return "Item removido do aluguel com sucesso.";
		}
	}

	public String atualizarItemDoAluguel(Long aluguelId, Long itemId, Long novoLivroId, int novaQuantidade) {
		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null) {
			return "Aluguel não encontrado.";
		}

		if (aluguelExistente.isFinalizado()) {
			return "Aluguel já finalizado.";
		}

		Item itemExistente = aluguelExistente.getItemById(itemId);

		if (itemExistente == null) {
			return "Item não encontrado no Aluguel.";
		}

		Livro novoLivro = null;
		if (novoLivroId != null) {
			novoLivro = livroService.buscarPorId(novoLivroId);
			if (novoLivro == null) {
				return "Livro não encontrado.";
			}
		}

		float precoUnitarioAtual = itemExistente.getLivro().getPreco();
		float novoValorTotal;

		if (novoLivro != null) {
			float precoUnitarioNovo = novoLivro.getPreco();
			float valorDoLivroAnterior = precoUnitarioAtual * itemExistente.getQuantidade();
			float valorDoNovoLivro = precoUnitarioNovo * novaQuantidade;
			itemExistente.setLivro(novoLivro);
			itemExistente.setQuantidade(novaQuantidade);
			novoValorTotal = aluguelExistente.getValorTotal() - valorDoLivroAnterior + valorDoNovoLivro;
		} else {
			int diferencaQuantidade = novaQuantidade - itemExistente.getQuantidade();
			itemExistente.setQuantidade(novaQuantidade);
			novoValorTotal = aluguelExistente.getValorTotal() + (diferencaQuantidade * precoUnitarioAtual);
		}

		aluguelExistente.setValorTotal(novoValorTotal);
		aluguelRepository.saveAndFlush(aluguelExistente);
		return "Item atualizado no Aluguel.";
	}

	public String finalizarAluguel(Long aluguelId) {
		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null) {
			return "Aluguel não encontrado.";
		}

		if (aluguelExistente.isFinalizado()) {
			return "Aluguel já finalizado.";
		}

		if (aluguelExistente.getItens().isEmpty()) {
			return "O aluguel precisa ter pelo menos 1 item.";
		}

		aluguelExistente.finalizar();
		aluguelRepository.saveAndFlush(aluguelExistente);
		return "Aluguel finalizado.";
	}

	public List<Item> listarItensDoAluguel(Long aluguelId) {
		Aluguel aluguelExistente = buscarPorId(aluguelId);
		return (aluguelExistente != null) ? aluguelExistente.getItens() : null;
	}

	public List<Livro> listarLivrosDoAluguel(Long aluguelId) {
		List<Livro> listaLivros = new ArrayList<>();
		Aluguel aluguel = buscarPorId(aluguelId);

		if (aluguel != null && !aluguel.getItens().isEmpty()) {
			for (Item item : aluguel.getItens()) {
				Livro livro = item.getLivro();
				if (livro != null) {
					listaLivros.add(livro);
				}
			}
		}
		return listaLivros;
	}
}