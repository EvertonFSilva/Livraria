package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("cliente")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	public String listarClientes(Model model) {
		List<Cliente> clientes = clienteService.listarClientes();
		model.addAttribute("clientes", clientes);
		return "listarClientes";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioAdicionar() {
		return "adicionarCliente";
	}

	@PostMapping("/adicionar")
	public String adicionarCliente(@Valid @RequestParam String login, @Valid @RequestParam String senha,
			@Valid @RequestParam String cpf, @Valid @RequestParam String nome, @Valid @RequestParam String email,
			@Valid @RequestParam String telefone, @Valid @RequestParam String endereco, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "adicionarCliente";
		}

		String mensagem = clienteService.adicionarCliente(login, senha, cpf, nome, email, telefone, endereco);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/cliente/adicionar";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditar(@PathVariable Long id, Model model) {
		Cliente cliente = clienteService.buscarPorId(id);
		model.addAttribute("cliente", cliente);
		return "editarCliente";
	}

	@PostMapping("/editar/{id}")
	public String editarCliente(@PathVariable Long id, @Valid @RequestParam String cpf,
			@Valid @RequestParam String nome, @Valid @RequestParam String email, @Valid @RequestParam String endereco,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "editarCliente";
		}

		String mensagem = clienteService.atualizarCliente(id, cpf, nome, email, endereco);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/cliente/editar/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluirCliente(@PathVariable Long id, Model model) {
		String mensagem = clienteService.deletarCliente(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/cliente/excluir/" + id;
	}

	@GetMapping("/{id}/telefones")
	public String listarTelefones(@PathVariable Long id, Model model) {
		List<String> telefones = clienteService.listarTelefones(id);
		model.addAttribute("telefones", telefones);
		return "listarTelefones";
	}

	@GetMapping("/{id}/adicionarTelefone")
	public String exibirFormularioAdicionarTelefone(@PathVariable Long id) {
		return "adicionarTelefone";
	}

	@PostMapping("/{id}/adicionarTelefone")
	public String adicionarTelefone(@PathVariable Long id, @RequestParam String telefone, Model model) {
		String mensagem = clienteService.adicionarTelefone(id, telefone);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/cliente";
	}

	@GetMapping("/{id}/deletarTelefone")
	public String exibirFormularioDeletarTelefone(@PathVariable Long id) {
		return "deletarTelefone";
	}

	@PostMapping("/{id}/deletarTelefone")
	public String deletarTelefone(@PathVariable Long id, @RequestParam String telefone, Model model) {
		String mensagem = clienteService.deletarTelefone(id, telefone);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/cliente";
	}
}
