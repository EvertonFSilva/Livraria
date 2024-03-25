package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Aluguel;
import br.edu.iff.livraria.entities.Item;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.entities.Pedido;
import br.edu.iff.livraria.repository.AluguelRepository;
import br.edu.iff.livraria.repository.ItemRepository;
import br.edu.iff.livraria.repository.LivroRepository;
import br.edu.iff.livraria.repository.PedidoRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private AluguelRepository aluguelRepository;

	public Livro buscarPorId(Long id) {
		return livroRepository.buscarPorId(id);
	}

	public List<Livro> buscarLivrosPorTituloContendo(String parteDoTitulo) {
		return livroRepository.buscarLivrosPorTituloContendo(parteDoTitulo);
	}

	public List<Livro> filtrarLivros(String titulo, String autor, String genero, Float precoMin, Float precoMax) {
		return livroRepository.filtrarLivros(titulo, autor, genero, precoMin, precoMax);
	}

	public String adicionarLivro(String titulo, String autor, String genero, int qtdPaginas, float preco) {
		Livro livroExistente = buscarPorTitulo(titulo);
		if (livroExistente != null) {
			return "Livro já cadastrado com esse título.";
		}

		Livro novoLivro = new Livro(titulo, autor, genero, qtdPaginas, preco);
		livroRepository.saveAndFlush(novoLivro);
		return "Livro adicionado com sucesso. Id: " + novoLivro.getId();
	}

	public Livro buscarPorTitulo(String titulo) {
		Livro livroExistente = livroRepository.buscarPorTitulo(titulo);
		if (livroExistente != null) {
			return livroExistente;
		}
		return null;
	}

	public String atualizarLivro(Long id, String novoTitulo, String autor, String genero, int qtdPaginas, float preco) {
		Livro livroExistente = buscarPorId(id);
		if (livroExistente != null) {
			if (novoTitulo != null && !novoTitulo.isEmpty()) {
				livroExistente.setTitulo(novoTitulo);
			}
			if (autor != null) {
				livroExistente.setAutor(autor);
			}
			if (genero != null) {
				livroExistente.setGenero(genero);
			}
			if (qtdPaginas > 0) {
				livroExistente.setQtdPaginas(qtdPaginas);
			}
			if (preco > 0) {
				List<Pedido> pedidosDoLivro = pedidoRepository.buscarPorLivroId(id);
				for (Pedido pedido : pedidosDoLivro) {
					if (!pedido.isFinalizado()) {
						Item item = pedido.getItemByLivroId(id);
						Livro livro = item.getLivro();
						float precoAntigo = livro.getPreco();
						livro.setPreco(preco);
						float diferencaPreco = Math.abs(preco - precoAntigo);
						float novoValorTotal = pedido.getValorTotal() - (diferencaPreco * item.getQuantidade());
						pedido.setValorTotal(novoValorTotal);
						pedidoRepository.saveAndFlush(pedido);
					}
				}

				List<Aluguel> alugueisDoLivro = aluguelRepository.buscarPorLivroId(id);
				for (Aluguel aluguel : alugueisDoLivro) {
					if (!aluguel.isFinalizado()) {
						Item item = aluguel.getItemByLivroId(id);
						Livro livro = item.getLivro();
						float precoAntigo = livro.getPreco();
						livro.setPreco(preco);
						float diferencaPreco = Math.abs(preco - precoAntigo);
						float novoValorTotal = aluguel.getValorTotal() - (diferencaPreco * item.getQuantidade());
						aluguel.setValorTotal(novoValorTotal);
						aluguelRepository.saveAndFlush(aluguel);
					}
				}

				livroExistente.setPreco(preco);
			}

			livroRepository.saveAndFlush(livroExistente);
			return "Livro atualizado com sucesso.";
		}
		return "Livro não encontrado.";
	}

	@Transactional
	public String deletarLivro(Long id) {
		Livro livroExistente = buscarPorId(id);
		if (livroExistente != null) {
			List<Pedido> pedidosDoLivro = pedidoRepository.buscarPorLivroId(id);
			for (Pedido pedido : pedidosDoLivro) {
				float valorDoLivro = pedido.getItens().stream().filter(item -> item.getLivro().getId().equals(id))
						.map(item -> item.getQuantidade() * item.getLivro().getPreco()).reduce(0f, Float::sum);
				pedido.setValorTotal(pedido.getValorTotal() - valorDoLivro);
				pedidoRepository.saveAndFlush(pedido);
			}
			List<Aluguel> alugueisDoLivro = aluguelRepository.buscarPorLivroId(id);
			for (Aluguel aluguel : alugueisDoLivro) {
				float valorDoLivro = aluguel.getItens().stream().filter(item -> item.getLivro().getId().equals(id))
						.map(item -> item.getQuantidade() * item.getLivro().getPreco()).reduce(0f, Float::sum);
				aluguel.setValorTotal(aluguel.getValorTotal() - valorDoLivro);
				aluguelRepository.saveAndFlush(aluguel);
			}
			List<Item> itensDoLivro = itemRepository.buscarPorLivroId(id);
			itemRepository.deleteInBatch(itensDoLivro);
			livroRepository.delete(livroExistente);
			return "Livro deletado com sucesso.";
		}
		return "Livro não encontrado.";
	}

	public List<Livro> listarLivros() {
		return livroRepository.listarLivros();
	}
}
