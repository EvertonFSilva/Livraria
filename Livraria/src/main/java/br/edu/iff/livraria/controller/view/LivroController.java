package br.edu.iff.livraria.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.LivroService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("livro")
public class LivroController {

	@Autowired
	private LivroService livroService;

	@GetMapping("/listar")
	public String listarLivros(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("livros", livroService.listarLivros());
			return "admin/livros";
		}
		return "redirect:/";
	}

	@PostMapping("/editar")
	public String editarLivro(@Valid @ModelAttribute Livro livro, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			livroService.atualizarLivro(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getGenero(),
					livro.getQtdPaginas(), livro.getPreco());
			return "redirect:/livro/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/adicionar")
	public String adicionarLivro(@Valid @ModelAttribute Livro livro, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			livroService.adicionarLivro(livro.getTitulo(), livro.getAutor(), livro.getGenero(), livro.getQtdPaginas(),
					livro.getPreco());
			return "redirect:/livro/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/deletar")
	public String deletarLivro(@RequestParam("id") Long id, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			livroService.deletarLivro(id);
			return "redirect:/livro/listar";
		}
		return "redirect:/";
	}
}