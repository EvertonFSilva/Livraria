package br.edu.iff.livraria.controller.view;

import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public String listarUsuarios(Model model) {
		List<Usuario> usuarios = usuarioService.listarUsuarios();
		model.addAttribute("usuarios", usuarios);
		return "listarUsuarios";
	}

	@GetMapping("/adicionar")
	public String exibirFormularioAdicionar() {
		return "adicionarUsuario";
	}

	@PostMapping("/adicionar")
	public String adicionarUsuario(@Valid @RequestParam String login, @Valid @RequestParam String senha,
			@Valid @RequestParam int permissao, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "adicionarUsuario";
		}

		Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, permissao);
		if (novoUsuario == null) {
			model.addAttribute("mensagem", "Erro ao adicionar usuário.");
			return "redirect:/adicionarUsuario";
		}

		model.addAttribute("mensagem", "Usuário adicionado com sucesso. Id: " + novoUsuario.getId());
		return "redirect:/adicionarUsuario";
	}

	@GetMapping("/editar/{id}")
	public String exibirFormularioEditar(@PathVariable Long id, Model model) {
		Usuario usuario = usuarioService.buscarPorId(id);
		model.addAttribute("usuario", usuario);
		return "editarUsuario";
	}

	@PostMapping("/editar/{id}")
	public String editarUsuario(@PathVariable Long id, @Valid @RequestParam String login,
			@Valid @RequestParam String senha, @Valid @RequestParam int permissao, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "editarUsuario";
		}

		String mensagem = usuarioService.atualizarUsuario(id, login, senha, permissao);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/editarUsuario/" + id;
	}

	@GetMapping("/excluir/{id}")
	public String excluirUsuario(@PathVariable Long id, Model model) {
		String mensagem = usuarioService.deletarUsuario(id);
		model.addAttribute("mensagem", mensagem);
		return "redirect:/usuario/excluir/" + id;
	}
}
