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

	public Usuario adicionarUsuario(String login, String senha, int permissao) {
		Usuario usuario = usuarioRepository.buscarPorLogin(login);

		if (usuario == null) {
			Usuario novoUsuario = new Usuario(login, senha, permissao);
			usuarioRepository.save(novoUsuario);
			return novoUsuario;
		}
		return null;
	}

	public String atualizarUsuario(Long id, String login, String senha, int permissao) {
		Usuario usuarioExistente = buscarPorId(id);

		if (usuarioExistente != null) {
			Usuario loginExistente = usuarioRepository.buscarPorLogin(login);

			if (loginExistente == null || loginExistente.getId().equals(id)) {
				boolean usuarioAtualizado = atualizarUsuario(usuarioExistente.getLogin(), login, senha, permissao);
				return usuarioAtualizado ? "Usuário atualizado." : "Usuário não atualizado.";
			}
		}

		return "Usuário não cadastrado.";
	}

	public String deletarUsuario(Long id) {
		Usuario usuarioExistente = usuarioRepository.buscarPorId(id);

		if (usuarioExistente != null) {
			usuarioRepository.delete(usuarioExistente);
			return "Usuário deletado.";
		} else {
			return "Usuário não deletado.";
		}
	}

	public Usuario buscarUsuario(Long id) {
		return buscarPorId(id);
	}

	public List<Usuario> listarUsuarios() {
		return usuarioRepository.listarUsuarios();
	}

	private Usuario buscarPorId(Long id) {
		return usuarioRepository.buscarPorId(id);
	}

	private boolean atualizarUsuario(String loginAtual, String novoLogin, String senha, int permissao) {
		Usuario usuarioExistente = buscarPorLogin(loginAtual);

		if (usuarioExistente != null) {
			if (!loginAtual.equals(novoLogin)) {
				usuarioExistente.setLogin(novoLogin);
			}
			usuarioExistente.setSenha(senha);
			usuarioExistente.setPermissao(permissao);
			usuarioRepository.save(usuarioExistente);
			return true;
		}

		return false;
	}

	private Usuario buscarPorLogin(String login) {
		return usuarioRepository.buscarPorLogin(login);
	}
}
