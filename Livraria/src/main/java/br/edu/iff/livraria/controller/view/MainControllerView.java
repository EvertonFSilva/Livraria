package br.edu.iff.livraria.controller.view;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Livro;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.FuncionarioService;
import br.edu.iff.livraria.service.LivroService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path = "")
public class MainControllerView {

	@Autowired
	public ClienteService clienteService;

	@Autowired
	public FuncionarioService funcionarioService;

	@Autowired
	private LivroService livroService;

	@GetMapping("")
	public String index(Model model, HttpServletRequest request, @AuthenticationPrincipal User user)
			throws UnsupportedEncodingException {
		if (user != null) {
			if (user.getAuthorities().toString().compareTo("[ROLE_Cliente]") == 0) {
				Cliente cliente = clienteService.buscarPorLogin(user.getUsername());
				model.addAttribute("cliente", cliente);
				model.addAttribute("pedido", cliente.getPedido());
				model.addAttribute("aluguel", cliente.getAluguel());
				model.addAttribute("nome", cliente.getNome());
			} else {
				if (user.getUsername().toLowerCase().compareTo("admin") == 0) {
					model.addAttribute("nome", "Admin");
				} else {
					model.addAttribute("nome", funcionarioService.buscarPorLogin(user.getUsername()).getNome());
				}
			}
		}
		String produto = request.getParameter("produto");
		if (produto == null) {
			model.addAttribute("livro_lista", livroService.listarLivros());
		} else {
			Livro livro = livroService.buscarPorTitulo(URLDecoder.decode(produto, "UTF-8"));
			model.addAttribute("livro", livro);
		}
		return "index";
	}

	@PostMapping("/buscarProduto")
	public String buscarProduto(String produto) throws UnsupportedEncodingException {
		return "redirect:/?produto=" + URLEncoder.encode(produto, "UTF-8");
	}
}