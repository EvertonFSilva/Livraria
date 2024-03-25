package br.edu.iff.livraria.controller.apirest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/funcionario")
public class FuncionarioRestController {

	@Autowired
	private FuncionarioService funcionarioService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um funcionário em específico")
	public ResponseEntity<String> adicionarFuncionario(@RequestParam String login, @RequestParam String senha,
			@RequestParam String cpf, @RequestParam String nome, @RequestParam String email,
			@RequestParam String telefone, @RequestParam String endereco, @RequestParam String cargo,
			@RequestParam float salario) throws Exception {
		try {
			String mensagem = funcionarioService.adicionarFuncionario(login, senha, cpf, nome, email, telefone,
					endereco, cargo, salario);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar funcionário.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um funcionário em específico")
	public ResponseEntity<String> atualizarFuncionario(@PathVariable Long id, @RequestParam String cpf,
			@RequestParam String nome, @RequestParam String email, @RequestParam String endereco,
			@RequestParam String cargo, @RequestParam float salario) throws Exception {
		try {
			String mensagem = funcionarioService.atualizarFuncionario(id, cpf, nome, email, endereco, cargo, salario);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar funcionário.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um funcionário em específico")
	public ResponseEntity<String> deletarFuncionario(@PathVariable Long id) throws Exception {
		try {
			String mensagem = funcionarioService.deletarFuncionario(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar funcionário.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um funcionário em específico")
	public ResponseEntity<Funcionario> buscarFuncionario(@PathVariable Long id) throws Exception {
		Funcionario funcionario = funcionarioService.buscarPorId(id);
		return ResponseEntity.ok(funcionario);
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os funcionários")
	public ResponseEntity<List<Funcionario>> listarFuncionarios() throws Exception {
		try {
			List<Funcionario> funcionarios = funcionarioService.listarFuncionarios();
			return ResponseEntity.ok(funcionarios);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PostMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Adicionar um telefone em um funcionário em específico")
	public ResponseEntity<String> adicionarTelefone(@PathVariable Long id, @RequestParam String telefone)
			throws Exception {
		try {
			String mensagem = funcionarioService.adicionarTelefone(id, telefone);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar telefone.");
		}
	}

	@DeleteMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Deletar um telefone em um funcionário em específico")
	public ResponseEntity<String> deletarTelefone(@PathVariable Long id, @RequestParam String telefone)
			throws Exception {
		try {
			String mensagem = funcionarioService.deletarTelefone(id, telefone);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar telefone.");
		}
	}

	@GetMapping("/{id}/telefones")
	@ResponseBody
	@Operation(summary = "Listar os telefones de um funcionário em específico")
	public ResponseEntity<List<String>> listarTelefones(@PathVariable Long id) throws Exception {
		try {
			List<String> telefones = funcionarioService.listarTelefones(id);
			return ResponseEntity.ok(telefones);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}