package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private LivroService livroService;

	@Autowired
	private AluguelService aluguelService;

	public Item buscarPorId(Long id) {
		return itemRepository.buscarPorId(id);
	}

	public boolean adicionarItem(Item item) {
		Livro livroExistente = livroService.buscarPorTitulo(item.getLivro().getTitulo());
		if (livroExistente != null) {
			itemRepository.save(item);

			if (item.getTipo() == 1) {
				adicionarAluguel(item);
			}

			return true;
		}
		return false;
	}

	public boolean atualizarItem(Long id, Livro livro, int quantidade, int tipo, float precoUnitario, Date dataInicio,
			Date dataFim) {
		Item itemExistente = buscarPorId(id);

		if (itemExistente != null) {
			itemExistente.setLivro(livro);
			itemExistente.setQuantidade(quantidade);
			itemExistente.setTipo(tipo);
			itemExistente.setPrecoUnitario(precoUnitario);

			if (itemExistente.getTipo() == 1 && dataInicio != null && dataFim != null) {
				aluguelService.atualizarAluguel(itemExistente.getAluguel(), dataInicio, dataFim);
			}

			itemRepository.save(itemExistente);
			return true;
		}
		return false;
	}

	public boolean deletarItem(Long id) {
		Item itemExistente = buscarPorId(id);

		if (itemExistente != null) {
			itemRepository.delete(itemExistente);

			Aluguel aluguelExistente = aluguelService.buscarPorId(itemExistente.getAluguel().getId());

			if (aluguelExistente != null) {
				aluguelService.deletarAluguel(aluguelExistente.getId());
			}
			return true;
		}
		return false;
	}

	public List<Item> listarItens() {
		return itemRepository.listarItens();
	}
	
	public Aluguel buscarAluguel(Long itemId) {
		return aluguelService.buscarPorId(itemId);
	}

	public boolean adicionarAluguel(Item item) {
		return aluguelService.adicionarAluguel(item);
	}
	
	public boolean deletarAluguel(Long itemId) {
		return aluguelService.deletarAluguel(itemId);
	}
	
	public List<Aluguel> listarAlugueisPorCliente(Long clienteId) {
		return aluguelService.listarAlugueisPorCliente(clienteId);
	}
}
