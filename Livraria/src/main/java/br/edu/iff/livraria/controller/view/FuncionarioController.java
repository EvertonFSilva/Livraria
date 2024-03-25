package br.edu.iff.livraria.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.FuncionarioService;
import br.edu.iff.livraria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	public UsuarioService usuarioService;

	@Autowired
	public FuncionarioService funcionarioService;

	@GetMapping("/listar")
	public String listarFuncionarios(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("funcionarios", funcionarioService.listarFuncionarios());
			return "admin/funcionarios";
		}
		return "redirect:/";
	}

	@GetMapping("/perfil")
	public String exibirPerfil(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Funcionario funcionario = funcionarioService.buscarPorLogin(usuario.getLogin());
		System.out.println(usuario.getLogin());
		if (funcionario != null) {
			model.addAttribute("perfil", funcionario);
			return "perfil";
		}
		return "redirect:/";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@Valid @ModelAttribute Funcionario funcionario, Model model, HttpSession session,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("mensagemErro", result.getAllErrors());
			return "error";
		} else {
			Funcionario funcionarioExistente = funcionarioService.buscarPorLogin(funcionario.getUsuario().getLogin());
			if (funcionarioExistente != null
					&& funcionarioExistente.getUsuario().getSenha().equals(funcionario.getUsuario().getSenha())) {
				if (funcionarioExistente.getTelefones().isEmpty()) {
					funcionarioService.adicionarTelefone(funcionarioExistente.getId(),
							funcionario.getTelefones().get(0));
				} else {
					funcionarioService.atualizarTelefone(funcionarioExistente.getId(),
							funcionarioExistente.getTelefones().get(0), funcionario.getTelefones().get(0));
				}
				String mensagem = funcionarioService.atualizarFuncionario(funcionarioExistente.getId(),
						funcionario.getCpf(), funcionario.getNome(), funcionario.getEmail(), funcionario.getEndereco(),
						funcionario.getCargo(), funcionario.getSalario());
				model.addAttribute("perfil", funcionarioExistente);
				model.addAttribute("mensagem", mensagem);
			}
			return "perfil";
		}
	}

	@PostMapping("/adicionar")
	public String adicionarFuncionario(@RequestParam("login") String login, @RequestParam("senha") String senha,
			@RequestParam("cpf") String cpf, @RequestParam("nome") String nome, @RequestParam("email") String email,
			@RequestParam("telefone") String telefone, @RequestParam("endereco") String endereco,
			@RequestParam("cargo") String cargo, @RequestParam("salario") float salario, Model model,
			HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			funcionarioService.adicionarFuncionario(login, senha, cpf, nome, email, telefone, endereco, cargo, salario);
			return "redirect:/funcionario/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/editar")
	public String editarFuncionario(@RequestParam("id") Long id, @RequestParam("login") String login,
			@RequestParam("senha") String senha, @RequestParam("cpf") String cpf, @RequestParam("nome") String nome,
			@RequestParam("email") String email, @RequestParam("telefone") String telefone,
			@RequestParam("endereco") String endereco, @RequestParam("cargo") String cargo,
			@RequestParam("salario") float salario, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			Funcionario funcionario = funcionarioService.buscarPorLogin(usuario.getLogin());
			if (funcionario != null) {
				usuarioService.atualizarUsuario(id, login, senha, 0);
				if (funcionario.getTelefones().isEmpty()) {
					funcionarioService.adicionarTelefone(id, telefone);
				} else {
					funcionarioService.atualizarTelefone(id, funcionario.getTelefones().get(0), telefone);
				}
				funcionarioService.atualizarFuncionario(id, cpf, nome, email, endereco, cargo, salario);
				return "redirect:/funcionario/listar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/deletar")
	public String deletarFuncionario(@RequestParam("id") Long id, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			funcionarioService.deletarFuncionario(id);
			return "redirect:/funcionario/listar";
		}
		return "redirect:/";
	}
}