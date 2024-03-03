package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.repository.ItemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private LivroService livroService;

	public Item buscarPorId(Long id) {
		return itemRepository.buscarPorId(id);
	}

	public boolean adicionarItem(Item item) {
		Livro livroExistente = livroService.buscarPorTitulo(item.getLivro().getTitulo());
		if (livroExistente == null) {
			return false;
		}

		item.setLivro(livroExistente);
		itemRepository.saveAndFlush(item);
		return true;
	}

	public boolean atualizarItem(Long id, Livro livro, int quantidade) {
		Item itemExistente = buscarPorId(id);
		if (itemExistente == null) {
			return false;
		}
		
		if (!itemExistente.getLivro().getTitulo().equals(livro.getTitulo())) {
			itemExistente.setLivro(livro);			
		}
		if (itemExistente.getQuantidade() != quantidade) {
			itemExistente.setQuantidade(quantidade);			
		}
		itemRepository.saveAndFlush(itemExistente);
		return true;
	}

	public boolean deletarItem(Long id) {
		Item itemExistente = buscarPorId(id);
		if (itemExistente != null) {
			itemRepository.delete(itemExistente);
			return true;
		}
		return false;
	}
}
