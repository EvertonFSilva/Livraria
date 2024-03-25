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

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.AluguelService;
import br.edu.iff.livraria.service.ClienteService;
import br.edu.iff.livraria.service.PedidoService;
import br.edu.iff.livraria.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	@Autowired
	public UsuarioService usuarioService;

	@Autowired
	public ClienteService clienteService;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private AluguelService aluguelService;

	@GetMapping("/listar")
	public String listarClientes(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			model.addAttribute("clientes", clienteService.listarClientes());
			return "admin/clientes";
		}
		return "redirect:/";
	}

	@GetMapping("/register")
	public String exibirFormularioRegistro(Model model) {
		model.addAttribute("novoCliente", new Cliente());
		return "register";
	}

	@PostMapping("/register")
	public String realizarRegistro(@Valid @ModelAttribute Cliente cliente, Model model, HttpSession session,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("mensagemErro", result.getAllErrors());
			return "error";
		} else {
			String mensagem = clienteService.adicionarCliente(cliente.getUsuario().getLogin(),
					cliente.getUsuario().getSenha(), cliente.getCpf(), cliente.getNome(), cliente.getEmail(),
					cliente.getTelefones().get(0), cliente.getEndereco());
			if (mensagem.contains("sucesso")) {
				session.setAttribute("usuarioLogado", cliente.getUsuario());
				return "redirect:/";
			} else {
				model.addAttribute("novoCliente", new Cliente());
				model.addAttribute("mensagem", mensagem);
				return "register";
			}
		}
	}

	@GetMapping("/perfil")
	public String exibirPerfil(Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
		if (cliente != null) {
			model.addAttribute("perfil", cliente);
			model.addAttribute("pedidos", pedidoService.buscarPedidosFinalizados(cliente.getId()));
			model.addAttribute("alugueis", aluguelService.buscarAlugueisFinalizados(cliente.getId()));
			return "perfil";
		}
		return "redirect:/";
	}

	@PostMapping("/editarPerfil")
	public String editarPerfil(@Valid @ModelAttribute Cliente cliente, Model model, HttpSession session,
			BindingResult result) {
		if (result.hasErrors()) {
			model.addAttribute("mensagemErro", result.getAllErrors());
			return "error";
		} else {
			Cliente clienteExistente = clienteService.buscarPorLogin(cliente.getUsuario().getLogin());
			if (clienteExistente != null
					&& clienteExistente.getUsuario().getSenha().equals(cliente.getUsuario().getSenha())) {
				if (clienteExistente.getTelefones().isEmpty()) {
					clienteService.adicionarTelefone(clienteExistente.getId(), cliente.getTelefones().get(0));
				} else {
					clienteService.atualizarTelefone(clienteExistente.getId(), clienteExistente.getTelefones().get(0),
							cliente.getTelefones().get(0));
				}
				String mensagem = clienteService.atualizarCliente(clienteExistente.getId(), cliente.getCpf(),
						cliente.getNome(), cliente.getEmail(), cliente.getEndereco());
				model.addAttribute("perfil", clienteExistente);
				model.addAttribute("mensagem", mensagem);
			}
			return "perfil";
		}
	}

	@PostMapping("/adicionar")
	public String adicionarCliente(@RequestParam("login") String login, @RequestParam("senha") String senha,
			@RequestParam("cpf") String cpf, @RequestParam("nome") String nome, @RequestParam("email") String email,
			@RequestParam("telefone") String telefone, @RequestParam("endereco") String endereco, Model model,
			HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			clienteService.adicionarCliente(login, senha, cpf, nome, email, telefone, endereco);
			return "redirect:/cliente/listar";
		}
		return "redirect:/";
	}

	@PostMapping("/editar")
	public String editarCliente(@RequestParam("id") Long id, @RequestParam("login") String login,
			@RequestParam("senha") String senha, @RequestParam("cpf") String cpf, @RequestParam("nome") String nome,
			@RequestParam("email") String email, @RequestParam("telefone") String telefone,
			@RequestParam("endereco") String endereco, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			Cliente cliente = clienteService.buscarPorLogin(usuario.getLogin());
			if (cliente != null) {
				usuarioService.atualizarUsuario(id, login, senha, 0);
				if (!telefone.isEmpty()) {
					if (cliente.getTelefones().isEmpty()) {
						clienteService.adicionarTelefone(id, telefone);
					} else {
						clienteService.atualizarTelefone(id, cliente.getTelefones().get(0), telefone);
					}					
				}
				clienteService.atualizarCliente(id, cpf, nome, email, endereco);
				return "redirect:/cliente/listar";
			}
		}
		return "redirect:/";
	}

	@PostMapping("/deletar")
	public String deletarCliente(@RequestParam("id") Long id, Model model, HttpSession session) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		if (usuario != null && usuario.getPermissao() == 2) {
			clienteService.deletarCliente(id);
			return "redirect:/cliente/listar";
		}
		return "redirect:/";
	}
}