package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

	@Autowired
	private LivroRepository livroRepository;

	public Livro buscarPorId(Long id) {
		return livroRepository.buscarPorId(id);
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
				livroExistente.setPreco(preco);
			}
			livroRepository.saveAndFlush(livroExistente);
			return "Livro atualizado com sucesso.";
		}
		return "Livro não encontrado.";
	}

	public String deletarLivro(Long id) {
		Livro livroExistente = buscarPorId(id);
		if (livroExistente != null) {
			livroRepository.delete(livroExistente);
			return "Livro deletado com sucesso.";
		}
		return "Livro não encontrado.";
	}

	public List<Livro> listarLivros() {
		return livroRepository.listarLivros();
	}
}
