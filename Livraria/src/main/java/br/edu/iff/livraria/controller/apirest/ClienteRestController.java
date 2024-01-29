package br.edu.iff.livraria.controller.apirest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1/cliente")
public class ClienteRestController {

	@Autowired
	private ClienteService clienteService;

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um cliente em específico")
	public String adicionarCliente(String login, String senha, String cpf, String nome, String email, String telefone,
			String endereco) {
		return clienteService.adicionarCliente(login, senha, cpf, nome, email, telefone, endereco);
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um cliente em específico")
	public String atualizarCliente(@PathVariable("id") Long id, String cpf, String nome, String email,
			String endereco) {
		return clienteService.atualizarCliente(id, cpf, nome, email, endereco);
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um cliente em específico")
	public String deletarCliente(@PathVariable("id") Long id) {
		return clienteService.deletarCliente(id);
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um cliente em específico")
	public Cliente buscarCliente(@PathVariable("id") Long id) {
		return clienteService.buscarCliente(id);
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os clientes")
	public List<Cliente> listarClientes() {
		return clienteService.listarClientes();
	}

	@PostMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Adicionar um telefone em um cliente em específico")
	public String adicionarTelefone(@PathVariable("id") Long id, String telefone) {
		return clienteService.adicionarTelefone(id, telefone);
	}

	@DeleteMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Deletar um telefone em um cliente em específico")
	public String deletarTelefone(@PathVariable("id") Long id, String telefone) {
		return clienteService.deletarTelefone(id, telefone);
	}

	@GetMapping("/{id}/telefones")
	@ResponseBody
	@Operation(summary = "Listar os telefones de um cliente em específico")
	public String listarTelefones(@PathVariable("id") Long id) {
		return clienteService.listarTelefones(id);
	}
}
