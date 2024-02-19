package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.LivroService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("livro")
public class LivroController {

	@Autowired
	private LivroService livroService;

	@GetMapping
	public String listarLivros(Model model) {
		List<Livro> livros = livroService.listarLivros();
		model.addAttribute("livros", livros);
		return "listarLivros";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioAdicionar() {
		return "adicionarLivro";
	}

	@PostMapping("/adicionar")
	public String adicionarLivro(@Valid @RequestParam String titulo, @Valid @RequestParam String autor,
			@Valid @RequestParam String genero, @Valid @RequestParam int qtdPaginas, @Valid @RequestParam float preco,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "adicionarLivro";
		}

		String mensagem = livroService.adicionarLivro(titulo, autor, genero, qtdPaginas, preco);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/livro/adicionar";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditar(@PathVariable Long id, Model model) {
		Livro livro = livroService.buscarPorId(id);
		model.addAttribute("livro", livro);
		return "editarLivro";
	}

	@PostMapping("/editar/{id}")
	public String editarLivro(@PathVariable Long id, @Valid @RequestParam String titulo,
			@Valid @RequestParam String autor, @Valid @RequestParam String genero, @Valid @RequestParam int qtdPaginas,
			@Valid @RequestParam float preco, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "editarLivro";
		}

		String mensagem = livroService.atualizarLivro(id, titulo, autor, genero, qtdPaginas, preco);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/livro/editar/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluirLivro(@PathVariable Long id, Model model) {
		String mensagem = livroService.deletarLivro(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/livro/excluir/" + id;
	}
}
