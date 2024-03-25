package br.edu.iff.livraria.controller.apirest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteRestController {

	@Autowired
	private ClienteService clienteService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um cliente em específico")
	public ResponseEntity<String> adicionarCliente(@RequestParam String login, @RequestParam String senha,
			@RequestParam String cpf, @RequestParam String nome, @RequestParam String email,
			@RequestParam String telefone, @RequestParam String endereco) throws Exception {
		try {
			String mensagem = clienteService.adicionarCliente(login, senha, cpf, nome, email, telefone, endereco);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar cliente.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um cliente em específico")
	public ResponseEntity<String> atualizarCliente(@PathVariable Long id, @RequestParam String cpf,
			@RequestParam String nome, @RequestParam String email, @RequestParam String endereco) throws Exception {
		try {
			String mensagem = clienteService.atualizarCliente(id, cpf, nome, email, endereco);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar cliente.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um cliente em específico")
	public ResponseEntity<String> deletarCliente(@PathVariable Long id) throws Exception {
		try {
			String mensagem = clienteService.deletarCliente(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar cliente.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um cliente em específico")
	public ResponseEntity<Cliente> buscarCliente(@PathVariable Long id) throws Exception {
		try {
			Cliente cliente = clienteService.buscarPorId(id);
			return ResponseEntity.ok(cliente);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os clientes")
	public ResponseEntity<List<Cliente>> listarClientes() throws Exception {
		try {
			List<Cliente> clientes = clienteService.listarClientes();
			return ResponseEntity.ok(clientes);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Adicionar um telefone em um cliente em específico")
	public ResponseEntity<String> adicionarTelefone(@PathVariable Long id, @RequestParam String telefone)
			throws Exception {
		try {
			String mensagem = clienteService.adicionarTelefone(id, telefone);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar telefone.");
		}
	}

	@DeleteMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Deletar um telefone em um cliente em específico")
	public ResponseEntity<String> deletarTelefone(@PathVariable Long id, @RequestParam String telefone)
			throws Exception {
		try {
			String mensagem = clienteService.deletarTelefone(id, telefone);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar telefone.");
		}
	}

	@GetMapping("/{id}/telefones")
	@ResponseBody
	@Operation(summary = "Listar os telefones de um cliente em específico")
	public ResponseEntity<List<String>> listarTelefones(@PathVariable Long id) throws Exception {
		try {
			List<String> telefones = clienteService.listarTelefones(id);
			return ResponseEntity.ok(telefones);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}