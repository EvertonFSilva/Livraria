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

	public String adicionarLivro(String titulo, String autor, String genero, int qtdPaginas, float precoVenda,
			float precoAluguel) {
		Livro livroExistente = livroRepository.buscarPorTitulo(titulo);

		if (livroExistente == null) {
			Livro novoLivro = new Livro(titulo, autor, genero, qtdPaginas, precoVenda, precoAluguel);
			livroRepository.save(novoLivro);
			return "Livro adicionado com sucesso.";
		} else {
			return "Livro já cadastrado com esse título.";
		}
	}

	public Livro buscarPorId(Long id) {
		return livroRepository.buscarPorId(id);
	}

	public Livro buscarPorTitulo(String titulo) {
		return livroRepository.buscarPorTitulo(titulo);
	}

	public String atualizarLivro(Long id, String titulo, String autor, String genero, int qtdPaginas, float precoVenda,
			float precoAluguel) {
		Livro livroExistente = buscarPorId(id);

		if (livroExistente != null) {
			livroExistente.setTitulo(titulo);
			livroExistente.setAutor(autor);
			livroExistente.setGenero(genero);
			livroExistente.setQtdPaginas(qtdPaginas);
			livroExistente.setPrecoVenda(precoVenda);
			livroExistente.setPrecoAluguel(precoAluguel);
			livroRepository.save(livroExistente);
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

	public String adicionarLivro(Livro livro) {
		if (livroRepository.buscarPorTitulo(livro.getTitulo()) != null) {
			return "Livro já cadastrado.";
		} else {
			Livro novoLivro = livroRepository.save(livro);
			return "Registrado no id " + novoLivro.getId();
		}
	}
}
