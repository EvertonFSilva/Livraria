package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.repository.AluguelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

	public String adicionarAluguel(Long clienteId, String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		List<Aluguel> alugueisEmProgresso = aluguelRepository.buscarAlugueisEmProgresso(clienteId);
		if (!alugueisEmProgresso.isEmpty()) {
			return "Cliente já tem um aluguel em progresso.";
		}

		Aluguel novoAluguel = new Aluguel(clienteExistente, formaPagamento);
		clienteExistente.adicionarAluguel(novoAluguel);
		aluguelRepository.saveAndFlush(novoAluguel);
		return "Aluguel adicionado. Id: " + novoAluguel.getId();
	}

	public String atualizarAluguel(Long aluguelId, Long clienteId, float valorTotal, LocalDateTime dataInicio,
			LocalDateTime dataFim, LocalDateTime dataEntrega, String formaPagamento) {
		Cliente clienteExistente = clienteService.buscarPorId(clienteId);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		Aluguel aluguelExistente = buscarPorId(aluguelId);

		if (aluguelExistente == null || !aluguelExistente.getCliente().getId().equals(clienteId)) {
			return "Aluguel não encontrado ou não pertence a esse Cliente.";
		}

		if (aluguelExistente.isFinalizado()) {
			return "Aluguel já finalizado.";
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

		List<Aluguel> alugueisEmProgresso = aluguelRepository.buscarAlugueisEmProgresso(clienteExistente.getId());
		if (alugueisEmProgresso.size() != 1) {
			return "O cliente não tem nenhum aluguel.";
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

		boolean itemExistente = aluguelExistente.getItens().stream()
				.anyMatch(item -> item.getLivro().getId().equals(livroId));

		if (itemExistente) {
			return "Item já adicionado ao Aluguel.";
		}

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
}