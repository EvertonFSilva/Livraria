package br.edu.iff.livraria.service;

import br.edu.iff.livraria.entities.Funcionario;
import br.edu.iff.livraria.entities.Usuario;
import br.edu.iff.livraria.repository.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuncionarioService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	public Funcionario buscarPorId(Long id) {
		return funcionarioRepository.buscarPorId(id);
	}

	public String adicionarFuncionario(String login, String senha, String cpf, String nome, String email,
			String telefone, String endereco, String cargo, float salario) {
		Funcionario funcionarioExistente = funcionarioRepository.buscarPorCPF(cpf);
		if (funcionarioExistente != null) {
			return "Funcionário já cadastrado.";
		}

		Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, 1);
		if (novoUsuario == null) {
			return "Erro ao adicionar usuário.";
		}

		Funcionario novoFuncionario = new Funcionario(cpf, nome, email, telefone, endereco, cargo, salario);
		novoFuncionario.setUsuario(novoUsuario);
		funcionarioRepository.saveAndFlush(novoFuncionario);
		return "Funcionário adicionado com sucesso. Id: " + novoFuncionario.getId();
	}

	public String atualizarFuncionario(Long id, String cpf, String nome, String email, String endereco, String cargo,
			float salario) {
		Funcionario funcionarioExistente = buscarPorId(id);
		if (funcionarioExistente == null) {
			return "Funcionário não encontrado.";
		}

		if (!funcionarioExistente.getCpf().equals(cpf)) {
			return "Esse Id não pertence a esse funcionário.";
		}

		funcionarioExistente.setCpf(cpf);
		funcionarioExistente.setNome(nome);
		funcionarioExistente.setEmail(email);
		funcionarioExistente.setEndereco(endereco);
		funcionarioExistente.setCargo(cargo);
		funcionarioExistente.setSalario(salario);
		funcionarioRepository.saveAndFlush(funcionarioExistente);
		return "Funcionário atualizado com sucesso.";
	}

	public String deletarFuncionario(Long id) {
		Funcionario funcionarioExistente = buscarPorId(id);
		if (funcionarioExistente == null) {
			return "Funcionário não encontrado.";
		}

		funcionarioRepository.delete(funcionarioExistente);
		return "Funcionário deletado com sucesso.";
	}

	public List<Funcionario> listarFuncionarios() {
		return funcionarioRepository.listarFuncionarios();
	}

	public String adicionarTelefone(Long id, String telefone) {
		Funcionario funcionarioExistente = buscarPorId(id);
		if (funcionarioExistente == null) {
			return "Funcionário não encontrado.";
		}

		if (telefone == null || telefone.trim().isEmpty()) {
			return "Telefone inválido.";
		}

		if (funcionarioExistente.getTelefones().size() >= 2) {
			return "Funcionário já possui o número máximo de telefones permitidos.";
		}

		if (funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefone)) {
			return "Esse telefone já está cadastrado.";
		}

		funcionarioExistente.adicionarTelefone(telefone);
		funcionarioRepository.saveAndFlush(funcionarioExistente);
		return "Telefone adicionado com sucesso.";
	}

	public String deletarTelefone(Long id, String telefone) {
		Funcionario funcionarioExistente = buscarPorId(id);
		if (funcionarioExistente == null) {
			return "Funcionário não encontrado.";
		}

		if (telefone == null || telefone.trim().isEmpty()) {
			return "Telefone inválido.";
		}

		if (!funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefone)) {
			return "Telefone não encontrado.";
		}

		funcionarioExistente.deletarTelefone(telefone);
		funcionarioRepository.saveAndFlush(funcionarioExistente);
		return "Telefone deletado com sucesso.";
	}

	public List<String> listarTelefones(Long id) {
		Funcionario funcionario = buscarPorId(id);
		return (funcionario != null) ? funcionario.getTelefones() : null;
	}
}
