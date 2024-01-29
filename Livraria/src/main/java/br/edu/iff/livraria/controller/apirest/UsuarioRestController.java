package br.edu.iff.livraria.controller.apirest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(path = "/api/v1/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("")
	@ResponseBody
	@Operation(summary = "Adicionar um usuário específico")
	public String adicionarUsuario(@RequestParam String login, @RequestParam String senha,
			@RequestParam int permissao) {
		Usuario resultado = usuarioService.adicionarUsuario(login, senha, permissao);
		return resultado != null ? "Usuário cadastrado com sucesso." : "Erro: Usuário já cadastrado com esse login.";
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um usuário específico")
	public String atualizarUsuario(@PathVariable("id") Long id, @RequestParam String login, @RequestParam String senha,
			@RequestParam int permissao) {
		return usuarioService.atualizarUsuario(id, login, senha, permissao);
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um usuário específico")
	public String deletarUsuario(@PathVariable("id") Long id) {
		return usuarioService.deletarUsuario(id);
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um usuário específico")
	public Usuario buscarUsuario(@PathVariable("id") Long id) {
		return usuarioService.buscarUsuario(id);
	}

	@GetMapping("")
	@ResponseBody
	@Operation(summary = "Listar todos os usuários")
	public List<Usuario> listarUsuarios() {
		return usuarioService.listarUsuarios();
	}
}
