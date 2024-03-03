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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.service.FuncionarioService;
import br.edu.iff.livraria.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("funcionario")
public class FuncionarioController {

	@Autowired
	public UsuarioService usuarioService;

	@Autowired
	public FuncionarioService funcionarioService;

	@GetMapping("/CRUD")
	public String page() throws Exception {
		return "redirect:/funcionario/CRUD/listarFuncionarios";
	}

	@GetMapping("/CRUD/addForm")
	public String addFuncionarioForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("funcionario_add", new Funcionario());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "CRUD_Funcionario";
	}

	@PostMapping("/CRUD/add")
	public String addFuncionario(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = funcionarioService.adicionarFuncionario(funcionario.getUsuario().getLogin(),
					funcionario.getUsuario().getSenha(), funcionario.getCpf(), funcionario.getNome(),
					funcionario.getEmail(), funcionario.getTelefones().get(0), funcionario.getEndereco(),
					funcionario.getCargo(), funcionario.getSalario());
			try {
				return "redirect:/funcionario/CRUD/addForm?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	@GetMapping("/CRUD/listarFuncionarios")
	public String listarFuncionarios(Model model, HttpServletRequest request) throws Exception {
		String cpf = request.getParameter("cpf");
		if (cpf == null) {
			model.addAttribute("funcionario_lista", funcionarioService.listarFuncionarios());
		} else {
			Funcionario funcionario = funcionarioService.buscarPorCPF(cpf);
			if (funcionario == null) {
				funcionario = new Funcionario();
			}
			model.addAttribute("funcionario_lista", funcionario);
		}
		return "CRUD_Funcionario";
	}

	@PostMapping("/CRUD/buscaCPF")
	public String buscarFuncionarioCPF(String cpf) throws Exception {
		return "redirect:/funcionario/CRUD/listarFuncionarios?cpf=" + URLEncoder.encode(cpf, "UTF-8");
	}

	@GetMapping("/CRUD/editar")
	public String formEditar(@RequestParam Long id, Model model) throws Exception {
		Funcionario funcionario = funcionarioService.buscarPorId(id);
		model.addAttribute("funcionario_edit", funcionario);
		model.addAttribute("telefone_lista", funcionarioService.listarTelefones(funcionario.getId()));
		return "CRUD_Funcionario";
	}

	@PostMapping("/CRUD/atualizar")
	public String atualizarFuncionario(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado,
			Model model) {
		String login = funcionario.getUsuario().getLogin();
		String senha = funcionario.getUsuario().getSenha();
		String cpf = funcionario.getCpf();
		String nome = funcionario.getNome();
		String email = funcionario.getEmail();
		String endereco = funcionario.getEndereco();
		String cargo = funcionario.getCargo();
		Float salario = funcionario.getSalario();
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			funcionarioService.atualizarFuncionario(funcionarioService.buscarPorCPF(cpf).getUsuario().getId(), cpf, nome, email, endereco, cargo, salario);
			if (!login.isEmpty() || !senha.isEmpty()) {
				usuarioService.atualizarUsuario(funcionarioService.buscarPorCPF(cpf).getUsuario().getId(), login, senha, 1);
			}
		}
		return "CRUD_Funcionario";
	}

	@GetMapping("/CRUD/deletePorCPF")
	public String deletarFuncionarioCPF(String cpf) throws Exception {
		funcionarioService.deletarFuncionario(funcionarioService.buscarPorCPF(cpf).getId());
		return "redirect:/funcionario/CRUD/listarFuncionarios";
	}

	@GetMapping("/CRUD/removeTelefone")
	public String removeTelefone(String cpf, String telefone) throws Exception {
		if (funcionarioService.buscarPorCPF(cpf).getTelefones().size() > 1) {
			funcionarioService.deletarTelefone(funcionarioService.buscarPorCPF(cpf).getId(), telefone);
		}
		return "redirect:/funcionario/CRUD/editar?id=" + funcionarioService.buscarPorCPF(cpf).getId();
	}

	@PostMapping("/CRUD/addTelefone")
	public String addTelefone(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado, Model model) {
		String cpf = funcionario.getCpf();
		String telefone = funcionario.getTelefones().get(0);
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			funcionarioService.adicionarTelefone(funcionarioService.buscarPorCPF(cpf).getId(), telefone);
			return "redirect:/funcionario/CRUD/editar?id=" + funcionarioService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/editarPerfil/{id}")
	public String editarPerfil(@PathVariable("id") Long id, Model model) throws Exception {
		Funcionario funcionario = funcionarioService.buscarPorId(id);
		model.addAttribute("funcionario_edit", funcionario);
		model.addAttribute("telefone_lista", funcionarioService.listarTelefones(funcionario.getId()));
		return "editarPerfil";
	}

	@PostMapping("/editarPerfil/atualizarValoresPerfil")
	public String atualizarValoresPerfil(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado,
			Model model) {
		String login = funcionario.getUsuario().getLogin();
		String senha = funcionario.getUsuario().getSenha();
		String cpf = funcionario.getCpf();
		String nome = funcionario.getNome();
		String email = funcionario.getEmail();
		String endereco = funcionario.getEndereco();
		String cargo = funcionario.getCargo();
		Float salario = funcionario.getSalario();

		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			funcionarioService.atualizarFuncionario(funcionarioService.buscarPorCPF(cpf).getUsuario().getId(), cpf, nome, email, endereco, cargo, salario);
			if (!login.isEmpty() || !senha.isEmpty()) {
				usuarioService.atualizarUsuario(funcionarioService.buscarPorCPF(cpf).getUsuario().getId(), login, senha, 1);
			}
			return "redirect:/funcionario/editarPerfil/" + funcionarioService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/editarPerfil/removeTelefone")
	public String removeTelefonePerfil(String cpf, String telefone) throws Exception {
		if (funcionarioService.buscarPorCPF(cpf).getTelefones().size() > 1) {
			funcionarioService.deletarTelefone(funcionarioService.buscarPorCPF(cpf).getId(), telefone);
		}
		return "redirect:/funcionario/editarPerfil/" + funcionarioService.buscarPorCPF(cpf).getId();
	}

	@PostMapping("/editarPerfil/addTelefone")
	public String addTelefonePerfil(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado,
			Model model) {
		String cpf = funcionario.getCpf();
		String telefone = funcionario.getTelefones().get(0);
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			funcionarioService.adicionarTelefone(funcionarioService.buscarPorCPF(cpf).getId(), telefone);
			return "redirect:/funcionario/editarPerfil/" + funcionarioService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/cadastro")
	public String cadastroForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("funcionario_add", new Funcionario());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "cadastro";
	}

	@PostMapping("/cadastro/add")
	public String addCadastro(@Valid @ModelAttribute Funcionario funcionario, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = funcionarioService.adicionarFuncionario(funcionario.getUsuario().getLogin(),
					funcionario.getUsuario().getSenha(), funcionario.getCpf(), funcionario.getNome(),
					funcionario.getEmail(), funcionario.getTelefones().get(0), funcionario.getEndereco(),
					funcionario.getCargo(), funcionario.getSalario());
			try {
				return "redirect:/funcionario/cadastro?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

}