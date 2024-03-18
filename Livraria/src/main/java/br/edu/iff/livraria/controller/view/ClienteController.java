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

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("cliente")
public class ClienteController {

	@Autowired
	public UsuarioService usuarioService;

	@Autowired
	public ClienteService clienteService;
	
	@GetMapping("/CRUD")
	public String page() throws Exception {
		return "redirect:/cliente/CRUD/listarClientes";
	}

	@GetMapping("/CRUD/addForm")
	public String addClienteForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("cliente_add", new Cliente());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "CRUD_Cliente";
	}

	@PostMapping("/CRUD/add")
	public String addCliente(@Valid @ModelAttribute Cliente cliente, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = clienteService.adicionarCliente(cliente.getUsuario().getLogin(),
					cliente.getUsuario().getSenha(), cliente.getCpf(), cliente.getNome(), cliente.getEmail(),
					cliente.getTelefones().get(0), cliente.getEndereco());
			try {
				return "redirect:/cliente/CRUD/addForm?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	@GetMapping("/CRUD/listarClientes")
	public String listarClientes(Model model, HttpServletRequest request) throws Exception {
		String cpf = request.getParameter("cpf");
		if (cpf == null) {
			model.addAttribute("cliente_lista", clienteService.listarClientes());
		} else {
			Cliente cliente = clienteService.buscarPorCPF(cpf);
			if (cliente == null) {
				cliente = new Cliente();
			}
			model.addAttribute("cliente_lista", cliente);
		}
		return "CRUD_Cliente";
	}

	@PostMapping("/CRUD/buscaCPF")
	public String buscarClienteCPF(String cpf) throws Exception {
		return "redirect:/cliente/CRUD/listarClientes?cpf=" + URLEncoder.encode(cpf, "UTF-8");
	}

	@GetMapping("/CRUD/editar")
	public String formEditar(@RequestParam Long id, Model model) throws Exception {
		Cliente cliente = clienteService.buscarPorId(id);
		model.addAttribute("cliente_edit", cliente);
		model.addAttribute("telefone_lista", clienteService.listarTelefones(cliente.getId()));
		return "CRUD_Cliente";
	}

	@PostMapping("/CRUD/atualizar")
	public String atualizarCliente(@Valid @ModelAttribute Cliente cliente ,BindingResult resultado, Model model) {
		String login = cliente.getUsuario().getLogin();
		String senha = cliente.getUsuario().getSenha();
		String cpf = cliente.getCpf();
		String nome = cliente.getNome();
		String email = cliente.getEmail();
		String endereco = cliente.getEndereco();
		if(resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		}else {			
			clienteService.atualizarCliente(clienteService.buscarPorCPF(cpf).getId(), cpf, nome, email, endereco);
			if (!login.isEmpty() || !senha.isEmpty()) {
				usuarioService.atualizarUsuario(clienteService.buscarPorCPF(cpf).getUsuario().getId(), login, senha, 0);
			}
		}
		return "CRUD_Cliente";
	}

	@GetMapping("/CRUD/deletePorCPF")
	public String deletarClienteCPF(String cpf) throws Exception {
		clienteService.deletarCliente(clienteService.buscarPorCPF(cpf).getId());
		return "redirect:/cliente/CRUD/listarClientes";
	}

	@GetMapping("/CRUD/removeTelefone")
	public String removeTelefone(String cpf, String telefone) throws Exception {
		if (clienteService.buscarPorCPF(cpf).getTelefones().size() > 1) {
			clienteService.deletarTelefone(clienteService.buscarPorCPF(cpf).getId(), telefone);
		}
		return "redirect:/cliente/CRUD/editar?id=" + clienteService.buscarPorCPF(cpf).getId();
	}

	@PostMapping("/CRUD/addTelefone")
	public String addTelefone(@Valid @ModelAttribute Cliente cliente, BindingResult resultado, Model model) {
		String cpf = cliente.getCpf();
		String telefone = cliente.getTelefones().get(0);
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			clienteService.adicionarTelefone(clienteService.buscarPorCPF(cpf).getId(), telefone);
			return "redirect:/cliente/CRUD/editar?id=" + clienteService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/editarPerfil/{id}")
	public String editarPerfil(@PathVariable("id") Long id, Model model) throws Exception {
		Cliente cliente = clienteService.buscarPorId(id);
		model.addAttribute("cliente_edit", cliente);
		model.addAttribute("telefone_lista", clienteService.listarTelefones(cliente.getId()));
		return "editarPerfil";
	}

	@PostMapping("/editarPerfil/atualizarValoresPerfil")
	public String atualizarValoresPerfil(@Valid @ModelAttribute Cliente cliente, BindingResult resultado, Model model) {
		String login = cliente.getUsuario().getLogin();
		String senha = cliente.getUsuario().getSenha();
		String cpf = cliente.getCpf();
		String nome = cliente.getNome();
		String email = cliente.getEmail();
		String endereco = cliente.getEndereco();

		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			clienteService.atualizarCliente(clienteService.buscarPorCPF(cpf).getId(), cpf, nome, email, endereco);
			if (!login.isEmpty() || !senha.isEmpty()) {
				usuarioService.atualizarUsuario(clienteService.buscarPorCPF(cpf).getUsuario().getId(), login, senha, 0);
			}
			return "redirect:/cliente/editarPerfil/" + clienteService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/editarPerfil/removeTelefone")
	public String removeTelefonePerfil(String cpf, String telefone) throws Exception {
		if (clienteService.buscarPorCPF(cpf).getTelefones().size() > 1) {
			clienteService.deletarTelefone(clienteService.buscarPorCPF(cpf).getId(), telefone);
		}
		return "redirect:/cliente/editarPerfil/" + clienteService.buscarPorCPF(cpf).getId();
	}

	@PostMapping("/editarPerfil/addTelefone")
	public String addTelefonePerfil(@Valid @ModelAttribute Cliente cliente, BindingResult resultado, Model model) {
		String cpf = cliente.getCpf();
		String telefone = cliente.getTelefones().get(0);
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			clienteService.adicionarTelefone(clienteService.buscarPorCPF(cpf).getId(), telefone);
			return "redirect:/cliente/editarPerfil/" + clienteService.buscarPorCPF(cpf).getId();
		}
	}

	@GetMapping("/cadastro")
	public String cadastroForm(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("cliente_add", new Cliente());
		String resposta = request.getParameter("resposta");
		if (resposta != null) {
			model.addAttribute("respostaAdd", URLDecoder.decode(resposta, "UTF-8"));
		}
		return "cadastro";
	}

	@PostMapping("/cadastro/add")
	public String addCadastro(@Valid @ModelAttribute Cliente cliente, BindingResult resultado, Model model) {
		if (resultado.hasErrors()) {
			model.addAttribute("mensagemErro", resultado.getAllErrors());
			return "";
		} else {
			String resposta = clienteService.adicionarCliente(cliente.getUsuario().getLogin(),
					cliente.getUsuario().getSenha(), cliente.getCpf(), cliente.getNome(), cliente.getEmail(),
					cliente.getTelefones().get(0), cliente.getEndereco());
			try {
				return "redirect:/cliente/cadastro?resposta=" + URLEncoder.encode(resposta, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return "";
			}
		}
	}

}