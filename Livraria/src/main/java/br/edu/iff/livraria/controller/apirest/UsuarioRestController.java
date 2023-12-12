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
@RequestMapping(path = "/api/v1/usuario")

public class UsuarioRestController {

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um usuario em expecifíco")
	public String adicionarUsuario(String login, String senha, int permissao) {
		return "Usuario adicionado.";
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um usuario em expecifíco")
	public String atualizarUsuario(@PathVariable("id") Long id, String senha, int permissao) {
		return "Usuario atualizado.";
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um usuario em expecifíco")
	public String deletarUsuario(@PathVariable("id") Long id) {
		return "Usuario deletado.";
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um usuario em expecifíco")
	public String buscarUsuario(@PathVariable("id") Long id) {
		return "Usuario retornado.";
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os usuarios")
	public String listarUsuarios() {
		return "Lista de usuarios.";
	}
}
