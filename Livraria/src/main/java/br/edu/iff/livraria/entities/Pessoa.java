package br.edu.iff.livraria.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public class Pessoa implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Não pode ser em branco ou nulo")
	@Pattern(regexp = "[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}", message = "Deve seguir o padrão do CPF")
	@Column(unique = true, length = 14)
	private String cpf;

	@Size(min = 1, max = 60, message = "Tem que ter entre 1 e 60 caractéres")
	@Column(length = 60)
	private String nome;

	@Size(min = 1, max = 20, message = "Tem que ter entre 1 e 20 caractéres")
	@Column(length = 20)
	private String senha;

	@Email(message = "Tem que ser em formato de email")
	@Column(length = 60)
	private String email;

	@Nullable
	@ElementCollection
	@Size(min = 1, max = 2, message = "Tem que ter entre 1 e 2 telefones")
	@Column(length = 16)
	private List<@Pattern(regexp = "\\([0-9]{2}\\) [0-9]{5}-[0-9]{4}", message = "Deve seguir o padrão do Telefone") String> telefones = new ArrayList<String>();

	@Size(min = 20, max = 100, message = "Tem que ter entre 20 e 100 caractéres")
	@Column(length = 100)
	private String endereco;

	@ManyToOne()
	@JoinColumn(name = "fk_pessoa")
	private Usuario usuario;

	public Pessoa(String cpf, String nome, String senha, String email, String telefone, String endereco) {
		this.cpf = cpf;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		this.telefones.add(telefone);
		this.endereco = endereco;
	}

	public Pessoa() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.nome = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void adicionarTelefone(String telefone) {
		this.telefones.add(telefone);
	}

	public void removerTelefone(String telefone) {
		this.telefones.remove(telefone);
	}

	public List<String> getTelefones() {
		return telefones;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
}
