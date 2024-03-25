package br.edu.iff.livraria.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.FuncionarioService;
import br.edu.iff.livraria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	public ClienteService clienteService;

	@Autowired
	public FuncionarioService funcionarioService;

	@GetMapping("/listar")
	public String listarUsuarios(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("usuarios", usuarioService.listarUsuarios());
			return "admin/usuarios";
		}
		return "redirect:/";
	}

	@GetMapping("/login")
	public String exibirFormularioLogin(Model model) {
		return "login";
	}

	@PostMapping("/login")
	public String realizarLogin(@RequestParam String username, @RequestParam String password, HttpSession session,
			Model model) {
		Usuario usuario = usuarioService.buscarPorLogin(username);
		if (usuario != null && usuario.getSenha().equals(password)) {
			session.setAttribute("usuarioLogado", usuario);
			return "redirect:/";
		} else {
			model.addAttribute("error", "Usuário ou senha inválidos");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String realizarLogout(HttpSession session) {
		session.removeAttribute("usuarioLogado");
		return "redirect:/";
	}

	@PostMapping("/adicionar")
	public String adicionarUsuario(@RequestParam("login") String login, @RequestParam("senha") String senha,
			@RequestParam("permissao") int permissao, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			usuarioService.adicionarUsuario(login, senha, permissao);
			return "redirect:/usuario/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/editar")
	public String editarUsuario(@RequestParam("id") Long id, @RequestParam("login") String login,
			@RequestParam("senha") String senha, @RequestParam("permissao") int permissao, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			usuarioService.atualizarUsuario(id, login, senha, permissao);
			/*
			Funcionario funcionario = funcionarioService.buscarPorLogin(login);
			Cliente cliente = clienteService.buscarPorLogin(login);
			if (funcionario != null && permissao == 1) {
				clienteService.adicionarCliente(login, senha, funcionario.getCpf(), funcionario.getNome(),
						funcionario.getEmail(), funcionario.getTelefones().get(0), funcionario.getEndereco());
			} else if (cliente != null && permissao == 2) {
				funcionarioService.adicionarFuncionario(login, senha, cliente.getCpf(), cliente.getNome(),
						cliente.getEmail(), cliente.getTelefones().get(0), cliente.getEndereco(), "Funcionário", 1);
			}
			*/
			return "redirect:/usuario/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/deletar")
	public String deletarUsuario(@RequestParam("id") Long id, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			usuarioService.deletarUsuario(id);
			return "redirect:/usuario/listar";
		}
		return "redirect:/";
	}

}