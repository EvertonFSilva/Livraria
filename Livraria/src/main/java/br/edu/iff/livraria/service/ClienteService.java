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

	public Cliente buscarPorId(Long id) {
		return clienteRepository.buscarPorId(id);
	}

	public String adicionarCliente(String login, String senha, String cpf, String nome, String email, String telefone,
			String endereco) {
		Cliente clienteExistente = clienteRepository.buscarPorCPF(cpf);
		if (clienteExistente != null) {
			return "Cliente já cadastrado.";
		}

		Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, 1);
		if (novoUsuario == null) {
			return "Erro ao adicionar usuário.";
		}

		Cliente novoCliente = new Cliente(cpf, nome, email, telefone, endereco);
		novoCliente.setUsuario(novoUsuario);
		clienteRepository.saveAndFlush(novoCliente);
		return "Cliente adicionado com sucesso. Id: " + novoCliente.getId();
	}

	public String atualizarCliente(Long id, String cpf, String nome, String email, String endereco) {
		Cliente clienteExistente = buscarPorId(id);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		if (!clienteExistente.getCpf().equals(cpf)) {
			return "Esse Id não pertence a esse cliente.";
		}
		if (!cpf.isEmpty()) {
			clienteExistente.setCpf(cpf);
		}
		if (!nome.isEmpty()) {
			clienteExistente.setNome(nome);
		}
		if (!email.isEmpty()) {
			clienteExistente.setEmail(email);
		}
		if (!endereco.isEmpty()) {
			clienteExistente.setEndereco(endereco);
		}
		clienteRepository.saveAndFlush(clienteExistente);
		return "Cliente atualizado com sucesso.";
	}

	public String deletarCliente(Long id) {
		Cliente clienteExistente = buscarPorId(id);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		clienteRepository.delete(clienteExistente);
		return "Cliente deletado com sucesso.";
	}

	public List<Cliente> listarClientes() {
		return clienteRepository.listarClientes();
	}

	public String adicionarTelefone(Long id, String telefone) {
		Cliente clienteExistente = buscarPorId(id);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		if (telefone == null || telefone.trim().isEmpty()) {
			return "Telefone inválido.";
		}

		if (clienteExistente.getTelefones().size() >= 2) {
			return "Cliente já possui o número máximo de telefones permitidos.";
		}

		if (clienteRepository.existeTelefoneNoCPF(clienteExistente.getCpf(), telefone)) {
			return "Esse telefone já está cadastrado.";
		}

		clienteExistente.adicionarTelefone(telefone);
		clienteRepository.saveAndFlush(clienteExistente);
		return "Telefone adicionado com sucesso.";
	}

	public String deletarTelefone(Long id, String telefone) {
		Cliente clienteExistente = buscarPorId(id);

		if (clienteExistente == null) {
			return "Cliente não encontrado.";
		}

		if (telefone == null || telefone.trim().isEmpty()) {
			return "Telefone inválido.";
		}

		if (!clienteRepository.existeTelefoneNoCPF(clienteExistente.getCpf(), telefone)) {
			return "Telefone não encontrado.";
		}

		clienteExistente.deletarTelefone(telefone);
		clienteRepository.saveAndFlush(clienteExistente);
		return "Telefone deletado com sucesso.";
	}

	public List<String> listarTelefones(Long id) {
		Cliente cliente = buscarPorId(id);
		return (cliente != null) ? cliente.getTelefones() : null;
	}
}
