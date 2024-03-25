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

	public Funcionario buscarPorCPF(String cpf) {
		return funcionarioRepository.buscarPorCPF(cpf);
	}

	public Funcionario buscarPorLogin(String login) {
		return funcionarioRepository.buscarPorLogin(login);
	}

	public String adicionarFuncionario(String login, String senha, String cpf, String nome, String email,
			String telefone, String endereco, String cargo, float salario) {
		Funcionario funcionarioExistente = funcionarioRepository.buscarPorCPF(cpf);
		if (funcionarioExistente != null) {
			return "Funcionário já cadastrado com esse CPF.";
		}

		Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, 2);
		if (novoUsuario == null) {
			return "Este nome de usuário já está em uso.";
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

		if (!cpf.isEmpty()) {
			funcionarioExistente.setCpf(cpf);
		}
		if (!nome.isEmpty()) {
			funcionarioExistente.setNome(nome);
		}
		if (!email.isEmpty()) {
			funcionarioExistente.setEmail(email);
		}
		if (!endereco.isEmpty()) {
			funcionarioExistente.setEndereco(endereco);
		}
		if (!cargo.isEmpty()) {
			funcionarioExistente.setCargo(cargo);
		}
		if (salario > 0) {
			funcionarioExistente.setSalario(salario);
		}
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

	public String atualizarTelefone(Long id, String telefoneAntigo, String telefoneNovo) {
		Funcionario funcionarioExistente = buscarPorId(id);
		if (funcionarioExistente == null) {
			return "Funcionário não encontrado.";
		}

		if (telefoneAntigo == null || telefoneAntigo.trim().isEmpty() || telefoneNovo == null
				|| telefoneNovo.trim().isEmpty()) {
			return "Telefones inválidos.";
		}

		if (!funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefoneAntigo)) {
			return "Telefone antigo não encontrado.";
		}

		if (funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefoneNovo)) {
			return "O novo telefone já está cadastrado para este funcionário.";
		}

		funcionarioExistente.atualizarTelefone(telefoneAntigo, telefoneNovo);
		funcionarioRepository.saveAndFlush(funcionarioExistente);
		return "Telefone atualizado com sucesso.";
	}

	public List<String> listarTelefones(Long id) {
		Funcionario funcionario = buscarPorId(id);
		return (funcionario != null) ? funcionario.getTelefones() : null;
	}
}
