package br.edu.iff.livraria.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.LivroService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/livro")
public class LivroController {

	@Autowired
	private LivroService livroService;

	@GetMapping("/listar")
	public String listarLivros(Model model) {
		model.addAttribute("livros", livroService.listarLivros());
		return "livro/listar";
	}

	@GetMapping("/novo")
	public String exibirFormularioNovoLivro(Model model) {
		model.addAttribute("livro", new Livro());
		return "livro/formulario";
	}

	@PostMapping("/novo")
	public String adicionarLivro(@Valid @ModelAttribute Livro livro, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "error";
		}

		livroService.adicionarLivro(livro);
		return "redirect:/livro/listar";
	}

	@GetMapping("/detalhes/{id}")
	public String exibirDetalhesLivro(@PathVariable Long id, Model model) {
		Livro livro = livroService.buscarPorId(id);
		model.addAttribute("livro", livro);
		return "livro/detalhes";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditarLivro(@PathVariable Long id, Model model) {
		Livro livro = livroService.buscarPorId(id);
		model.addAttribute("livro", livro);
		return "livro/formulario";
	}

	@PostMapping("/editar/{id}")
	public String editarLivro(@PathVariable Long id, @Valid @ModelAttribute Livro livro, BindingResult resultado,
			Model model) {
		if (resultado.hasErrors()) {
			return "error";
		}

		String titulo = livro.getTitulo();
		String genero = livro.getGenero();
		String autor = livro.getAutor();
		int qtdPaginas = livro.getQtdPaginas();
		float preco = livro.getPreco();

		livroService.atualizarLivro(id, titulo, autor, genero, qtdPaginas, preco);
		return "redirect:/livro/listar";
	}

	@GetMapping("/excluir/{id}")
	public String excluirLivro(@PathVariable Long id) {
		livroService.deletarLivro(id);
		return "redirect:/livro/listar";
	}
}