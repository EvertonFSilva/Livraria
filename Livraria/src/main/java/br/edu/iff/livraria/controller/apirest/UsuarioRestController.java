package br.edu.iff.livraria.controller.apirest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioRestController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	@ResponseBody
	@Operation(summary = "Adicionar um usuário específico")
	public ResponseEntity<String> adicionarUsuario(@RequestParam String login, @RequestParam String senha,
			@RequestParam int permissao) throws Exception {
		try {
			Usuario resultado = usuarioService.adicionarUsuario(login, senha, permissao);
			return ResponseEntity.ok(
					resultado != null ? "Usuário cadastrado com sucesso." : "Usuário já cadastrado com esse login.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar usuário.");
		}
	}

	@PutMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Atualizar um usuário específico")
	public ResponseEntity<String> atualizarUsuario(@PathVariable("id") Long id, @RequestParam String login,
			@RequestParam String senha, @RequestParam int permissao) throws Exception {
		try {
			String mensagem = usuarioService.atualizarUsuario(id, login, senha, permissao);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário.");
		}
	}

	@DeleteMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Deletar um usuário específico")
	public ResponseEntity<String> deletarUsuario(@PathVariable("id") Long id) throws Exception {
		try {
			String mensagem = usuarioService.deletarUsuario(id);
			return ResponseEntity.ok(mensagem);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar usuário.");
		}
	}

	@GetMapping("/{id}")
	@ResponseBody
	@Operation(summary = "Retornar um usuário específico")
	public ResponseEntity<Usuario> buscarUsuario(@PathVariable("id") Long id) throws Exception {
		try {
			Usuario usuario = usuarioService.buscarPorId(id);
			return ResponseEntity.ok(usuario);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	@ResponseBody
	@Operation(summary = "Listar todos os usuários")
	public ResponseEntity<List<Usuario>> listarUsuarios() throws Exception {
		List<Usuario> usuarios = usuarioService.listarUsuarios();
		return ResponseEntity.ok(usuarios);
	}
}