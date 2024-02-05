package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Usuario buscarPorId(Long id) {
		return usuarioRepository.buscarPorId(id);
	}
	
	public Usuario buscarPorLogin(String login) {
		return usuarioRepository.buscarPorLogin(login);
	}

	public Usuario adicionarUsuario(String login, String senha, int permissao) {
		Usuario usuarioExistente = usuarioRepository.buscarPorLogin(login);
		if (usuarioExistente == null) {
			Usuario novoUsuario = new Usuario(login, senha, permissao);
			usuarioRepository.saveAndFlush(novoUsuario);
			return novoUsuario;
		}
		return null;
	}

	public String atualizarUsuario(Long id, String login, String senha, int permissao) {
		Usuario usuarioExistente = buscarPorId(id);
		if (usuarioExistente == null) {
			return "Usuário não cadastrado.";
		}

		Usuario loginExistente = usuarioRepository.buscarPorLogin(login);
		if (loginExistente != null && !loginExistente.getId().equals(id)) {
			return "Id não é igual ou o login não existe.";
		}

		boolean usuarioAtualizado = atualizarUsuario(usuarioExistente.getLogin(), login, senha, permissao);
		return usuarioAtualizado ? "Usuário atualizado." : "Usuário não atualizado.";
	}

	public String deletarUsuario(Long id) {
		Usuario usuarioExistente = buscarPorId(id);
		if (usuarioExistente != null) {
			usuarioRepository.delete(usuarioExistente);
			return "Usuário deletado.";
		}
		return "Usuário não deletado.";
	}

	public List<Usuario> listarUsuarios() {
		return usuarioRepository.listarUsuarios();
	}

	private boolean atualizarUsuario(String loginAtual, String novoLogin, String senha, int permissao) {
		Usuario usuarioExistente = buscarPorLogin(loginAtual);
		if (usuarioExistente != null) {
			if (!loginAtual.equals(novoLogin)) {
				usuarioExistente.setLogin(novoLogin);
			}
			usuarioExistente.setSenha(senha);
			usuarioExistente.setPermissao(permissao);
			usuarioRepository.saveAndFlush(usuarioExistente);
			return true;
		}
		return false;
	}
}
