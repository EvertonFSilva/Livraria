package br.edu.iff.livraria.controller.apirest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1/cliente")
public class ClienteRestController {

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um cliente em expecifíco")
	public String adicionarCliente(String nome, String email, String telefone, String endereco) {
		return "Cliente adicionado: " + nome;
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um cliente em expecifíco")
	public String atualizarCliente(@PathVariable("id") Long id, String nome, String email, String senha) {
		return "Cliente atualizado.";
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um cliente em expecifíco")
	public String deletarCliente(@PathVariable("id") Long id) {
		return "Cliente deletado.";
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um cliente em expecifíco")
	public String buscarCliente(@PathVariable("id") Long id) {
		return "Cliente retornado.";
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os clientes")
	public String listarClientes() {
		return "Clientes listados.";
	}

	@PostMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Adicionar um telefone em um cliente em expecifíco")
	public String adicionarTelefone(@PathVariable("id") Long id, String telefone) {
		return "Telefone adicionado ao cliente.";
	}

	@DeleteMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Deletar um telefone em um cliente em expecifíco")
	public String deletarTelefone(@PathVariable("id") Long id, String telefone) {
		return "Telefone deletado do cliente.";
	}
	
	@GetMapping("/{id}/telefones")
	@ResponseBody
	@Operation(summary = "Listar os telefones de um cliente em expecifíco")
	public String listarTelefones(@PathVariable("id") Long id) {
		return "Lista de telefones.";
	}
	
}
