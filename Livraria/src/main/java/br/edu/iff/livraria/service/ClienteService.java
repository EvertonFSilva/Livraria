package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Cliente;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ClienteRepository clienteRepository;

	public String adicionarCliente(String login, String senha, String cpf, String nome, String email, String telefone, String endereco) {
		Cliente novoCliente = new Cliente(cpf, nome, email, telefone, endereco);
		
		Cliente clienteExistente = clienteRepository.buscarPorCPF(novoCliente.getCpf());

		if (clienteExistente == null) {
			Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, 1);

			if (novoUsuario != null) {
				novoCliente.setUsuario(novoUsuario);
				clienteRepository.save(novoCliente);
				return "Cliente adicionado com sucesso.";
			} else {
				return "Erro ao adicionar usuário.";
			}
		} else {
			return "Cliente já cadastrado.";
		}
	}

	public String atualizarCliente(Long id, String cpf, String nome, String email, String endereco) {
		Cliente clienteExistente = buscarCliente(id);

		if (clienteExistente != null) {
			clienteExistente.setCpf(cpf);
			clienteExistente.setNome(nome);
			clienteExistente.setEmail(email);
			clienteExistente.setEndereco(endereco);
			clienteRepository.save(clienteExistente);
			return "Cliente atualizado com sucesso.";
		} else {
			return "Cliente não encontrado.";
		}
	}

	public String deletarCliente(Long id) {
		Cliente clienteExistente = buscarCliente(id);

		if (clienteExistente != null) {
			clienteRepository.delete(clienteExistente);
			return "Cliente deletado com sucesso.";
		} else {
			return "Cliente não encontrado.";
		}
	}

	public Cliente buscarCliente(Long id) {
		return clienteRepository.buscarPorId(id);
	}

	public List<Cliente> listarClientes() {
		return clienteRepository.listarClientes();
	}

	public String adicionarTelefone(Long id, String telefone) {
		Cliente clienteExistente = buscarCliente(id);

		if (clienteExistente != null) {
			if (telefone != null && !telefone.trim().isEmpty()) {
				if (clienteExistente.getTelefones().size() < 2) {
					if (!clienteRepository.existeTelefoneNoCPF(clienteExistente.getCpf(), telefone)) {
						clienteExistente.adicionarTelefone(telefone);
						clienteRepository.save(clienteExistente);
						return "Telefone adicionado com sucesso.";
					} else {
						return "Esse telefone já está cadastrado.";
					}
				} else {
					return "Cliente já possui o número máximo de telefones permitidos.";
				}
			} else {
				return "Telefone inválido.";
			}
		} else {
			return "Cliente não encontrado.";
		}
	}

	public String deletarTelefone(Long id, String telefone) {
		Cliente clienteExistente = buscarCliente(id);

		if (clienteExistente != null) {
			if (telefone != null && !telefone.trim().isEmpty()) {
				if (clienteRepository.existeTelefoneNoCPF(clienteExistente.getCpf(), telefone)) {
					clienteExistente.deletarTelefone(telefone);
					clienteRepository.save(clienteExistente);
					return "Telefone deletado com sucesso.";
				} else {
					return "Telefone não encontrado.";
				}
			} else {
				return "Telefone inválido.";
			}
		} else {
			return "Cliente não encontrado.";
		}
	}

	public String listarTelefones(Long id) {
		Cliente clienteExistente = buscarCliente(id);

		if (clienteExistente != null) {
			List<String> telefones = clienteExistente.getTelefones();
			if (!telefones.isEmpty()) {
				return String.join(", ", telefones);
			} else {
				return "Cliente não possui telefones cadastrados.";
			}
		} else {
			return "Cliente não encontrado.";
		}
	}
}
