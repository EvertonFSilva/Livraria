package br.edu.iff.livraria.controller.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("livro")
public class LivroController {

	@Autowired
	private LivroService livroService;

	@GetMapping("")
	public String page(Model model) throws Exception {
		return "redirect:/livro/listarLivros";
	}

	@GetMapping("/addForm")
	public String addLivroForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("livro_add", new Livro());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "CRUD_Livro";
	}

	@PostMapping("/add")
	public String addLivro(@Valid @ModelAttribute Livro livro, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = livroService.adicionarLivro(livro.getTitulo(), livro.getAutor(), livro.getGenero(),
					livro.getQtdPaginas(), livro.getPreco());
			try {
				return "redirect:/livro/addForm?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	@GetMapping("/listarLivros")
	public String listarLivros(Model model, HttpServletRequest request) throws Exception {
		String titulo = request.getParameter("titulo");
		if (titulo == null) {
			model.addAttribute("livro_lista", livroService.listarLivros());
		} else {
			Livro livro = livroService.buscarPorTitulo(URLDecoder.decode(titulo, "UTF-8"));
			if (livro == null) {
				livro = new Livro();
			}
			model.addAttribute("livro_lista", livro);
		}
		return "CRUD_Livro";
	}

	@PostMapping("/buscaTitulo")
	public String buscarLivros(String titulo) throws Exception {
		return "redirect:/livro/listarLivros?titulo=" + URLEncoder.encode(titulo, "UTF-8");
	}

	@GetMapping("/editar")
	public String formEditar(@RequestParam Long id, Model model) throws Exception {
		model.addAttribute("livro_edit", livroService.buscarPorId(id));
		return "CRUD_Livro";
	}

	@PostMapping("/atualizar")
	public String atualizarLivro(@Valid @ModelAttribute Livro livro, BindingResult resultado, Model model) {
		String autor = livro.getAutor();
		String titulo = livro.getTitulo();
		String genero = livro.getGenero();
		int qtdPaginas = livro.getQtdPaginas();
		float preco = livro.getPreco();
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			livroService.atualizarLivro(livroService.buscarPorTitulo(titulo).getId(), titulo, autor, genero, qtdPaginas,
					preco);
			return "redirect:/livro/listarLivros";
		}
	}

	@GetMapping("/deletaPorTitulo")
	public String deletarLivroTitulo(String titulo) throws Exception {
		livroService.deletarLivro(livroService.buscarPorTitulo(titulo).getId());
		return "redirect:/livro/listarLivros";
	}

}