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
@RequestMapping(path = "/api/v1/funcionario")
public class FuncionarioRestController {

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um funcionario em expecifíco")
	public String adicionarFuncionario(String nome, String email, String telefone, String endereco, String cargo,
			float salario) {
		return "Funcionario adicionado: " + nome;
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um funcionario em expecifíco")
	public String atualizarFuncionario(@PathVariable("id") Long id, String nome, String email, String senha,
			String cargo, float salario) {
		return "Funcionario atualizado.";
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um funcionario em expecifíco")
	public String deletarFuncionario(@PathVariable("id") Long id) {
		return "Funcionario deletado.";
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um funcionario em expecifíco")
	public String buscarFuncionario(@PathVariable("id") Long id) {
		return "Funcionario retornado.";
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os funcionarios")
	public String listarFuncionarios() {
		return "Funcionarios listados.";
	}

	@PostMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Adicionar um telefone em um funcionario em expecifíco")
	public String adicionarTelefone(@PathVariable("id") Long id, String telefone) {
		return "Telefone adicionado ao funcionario.";
	}

	@DeleteMapping("/{id}/telefone")
	@ResponseBody
	@Operation(summary = "Deletar um telefone em um funcionario em expecifíco")
	public String deletarTelefone(@PathVariable("id") Long id, String telefone) {
		return "Telefone deletado do funcionario.";
	}

	@GetMapping("/{id}/telefones")
	@ResponseBody
	@Operation(summary = "Listar os telefones de um funcionario em expecifíco")
	public String listarTelefones(@PathVariable("id") Long id) {
		return "Lista de telefones.";
	}

	@PostMapping("/{id}/venda")
	@ResponseBody
	@Operation(summary = "Adicionar uma venda em um funcionario em expecifíco")
	public String adicionarVenda(@PathVariable("id") Long id, Long vId) {
		return "Venda adicionada ao funcionario.";
	}

	@DeleteMapping("/{id}/venda")
	@ResponseBody
	@Operation(summary = "Deletar uma venda em um funcionario em expecifíco")
	public String deletarVenda(@PathVariable("id") Long id, Long vId) {
		return "Venda deletada do funcionario.";
	}

	@GetMapping("/{id}/vendas")
	@ResponseBody
	@Operation(summary = "Listar as vendas de um funcionario em expecifíco")
	public String listarVendas(@PathVariable("id") Long id) {
		return "Lista de vendas do funcionaio.";
	}

	@PostMapping("/{id}/aluguel")
	@ResponseBody
	@Operation(summary = "Adicionar um aluguel em um funcionario em expecifíco")
	public String adicionarAluguel(@PathVariable("id") Long id, Long aId) {
		return "Aluguel adicionado ao funcionario.";
	}

	@DeleteMapping("/{id}/aluguel")
	@ResponseBody
	@Operation(summary = "Deletar uma venda em um funcionario em expecifíco")
	public String deletarAluguel(@PathVariable("id") Long id, Long aId) {
		return "Aluguel deletado do funcionario.";
	}

	@GetMapping("/{id}/alugueis")
	@ResponseBody
	@Operation(summary = "Listar as vendas de um funcionario em expecifíco")
	public String listarAluguel(@PathVariable("id") Long id) {
		return "Lista de alugueis do funcionaio.";
	}

}