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

	public String adicionarFuncionario(String login, String senha, String cpf, String nome, String email,
			String telefone, String endereco, String cargo, float salario) {
		Funcionario novoFuncionario = new Funcionario(cpf, nome, email, telefone, endereco, cargo, salario);

		Funcionario funcionarioExistente = funcionarioRepository.buscarPorCPF(novoFuncionario.getCpf());

		if (funcionarioExistente == null) {
			Usuario novoUsuario = usuarioService.adicionarUsuario(login, senha, 1);

			if (novoUsuario != null) {
				novoFuncionario.setUsuario(novoUsuario);
				funcionarioRepository.save(novoFuncionario);
				return "Funcionário adicionado com sucesso.";
			} else {
				return "Erro ao adicionar usuário.";
			}
		} else {
			return "Funcionário já cadastrado.";
		}
	}

	public String atualizarFuncionario(Long id, String cpf, String nome, String email, String endereco, String cargo,
			float salario) {
		Funcionario funcionarioExistente = buscarFuncionario(id);

		if (funcionarioExistente != null) {
			funcionarioExistente.setCpf(cpf);
			funcionarioExistente.setNome(nome);
			funcionarioExistente.setEmail(email);
			funcionarioExistente.setEndereco(endereco);
			funcionarioExistente.setCargo(cargo);
			funcionarioExistente.setSalario(salario);
			funcionarioRepository.save(funcionarioExistente);
			return "Funcionário atualizado com sucesso.";
		} else {
			return "Funcionário não encontrado.";
		}
	}

	public String deletarFuncionario(Long id) {
		Funcionario funcionarioExistente = buscarFuncionario(id);

		if (funcionarioExistente != null) {
			funcionarioRepository.delete(funcionarioExistente);
			return "Funcionário deletado com sucesso.";
		} else {
			return "Funcionário não encontrado.";
		}
	}

	public Funcionario buscarFuncionario(Long id) {
		return funcionarioRepository.buscarPorId(id);
	}

	public List<Funcionario> listarFuncionarios() {
		return funcionarioRepository.listarFuncionarios();
	}

	public String adicionarTelefone(Long id, String telefone) {
		Funcionario funcionarioExistente = buscarFuncionario(id);

		if (funcionarioExistente != null) {
			if (telefone != null && !telefone.trim().isEmpty()) {
				if (funcionarioExistente.getTelefones().size() < 2) {
					if (!funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefone)) {
						funcionarioExistente.adicionarTelefone(telefone);
						funcionarioRepository.save(funcionarioExistente);
						return "Telefone adicionado com sucesso.";
					} else {
						return "Esse telefone já está cadastrado.";
					}
				} else {
					return "Funcionário já possui o número máximo de telefones permitidos.";
				}
			} else {
				return "Telefone inválido.";
			}
		} else {
			return "Funcionário não encontrado.";
		}
	}

	public String deletarTelefone(Long id, String telefone) {
		Funcionario funcionarioExistente = buscarFuncionario(id);

		if (funcionarioExistente != null) {
			if (telefone != null && !telefone.trim().isEmpty()) {
				if (funcionarioRepository.existeTelefoneNoCPF(funcionarioExistente.getCpf(), telefone)) {
					funcionarioExistente.deletarTelefone(telefone);
					funcionarioRepository.save(funcionarioExistente);
					return "Telefone deletado com sucesso.";
				} else {
					return "Telefone não encontrado.";
				}
			} else {
				return "Telefone inválido.";
			}
		} else {
			return "Funcionário não encontrado.";
		}
	}

	public String listarTelefones(Long id) {
		Funcionario funcionarioExistente = buscarFuncionario(id);

		if (funcionarioExistente != null) {
			List<String> telefones = funcionarioExistente.getTelefones();
			if (!telefones.isEmpty()) {
				return String.join(", ", telefones);
			} else {
				return "Funcionário não possui telefones cadastrados.";
			}
		} else {
			return "Funcionário não encontrado.";
		}
	}
}
