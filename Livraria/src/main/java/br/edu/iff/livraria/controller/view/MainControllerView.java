package br.edu.iff.livraria.controller.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.LivroService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class MainControllerView {

	@Autowired
	private LivroService livroService;

	@GetMapping
	public String exibirPaginaInicial(Model model, @RequestParam(required = false) String titulo) {
		List<Livro> livros;

		if (titulo != null) {
			livros = livroService.buscarLivrosPorTituloContendo(titulo);
		} else {
			livros = livroService.listarLivros();
		}

		model.addAttribute("livros", livros);
		return "index";
	}

	@GetMapping("/painel")
	public String exibirPainel(HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			return "painel";
		}
		return "redirect:/";

	}

	@GetMapping("/catalogo")
	public String exibirCatalogo(Model model, @RequestParam(required = false) String preco,
			@RequestParam(required = false) String genero, @RequestParam(required = false) String autor,
			@RequestParam(required = false) String titulo) {
		List<Livro> livros;
		if (preco != null || genero != null || autor != null || titulo != null) {
			float precoMin = 0;
			float precoMax = 9999;
			if (preco != null) {
				precoMin = Float.parseFloat(preco.split("-")[0]);
				precoMax = Float.parseFloat(preco.split("-")[1]);
			}
			livros = livroService.filtrarLivros(titulo, autor, genero, precoMin, precoMax);
		} else {
			livros = livroService.listarLivros();
		}
		model.addAttribute("livros", livros);
		return "catalogo";
	}
}