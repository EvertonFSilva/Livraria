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

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Não pode ser em branco ou nulo")
	@Pattern(regexp = "[0-9]{11}", message = "Deve seguir o padrão do CPF")
	@Column(unique = true, length = 11)
	private String cpf;

	@Size(min = 3, max = 60, message = "Tem que ter entre 3 e 60 caractéres")
	@Column(length = 60)
	private String nome;

	@Email(message = "Tem que ser em formato de email")
	@Column(length = 60)
	private String email;

	@Nullable
	@ElementCollection
	@Size(min = 1, max = 2, message = "Tem que ter entre 1 e 2 telefones")
	@Column(length = 11)
	private List<@Pattern(regexp = "[0-9]{9,11}", message = "Deve seguir o padrão do Telefone") String> telefones;

	@Size(min = 10, max = 100, message = "Tem que ter entre 20 e 100 caractéres")
	@Column(length = 100)
	private String endereco;

	@ManyToOne()
	@JoinColumn(name = "pessoa_fk")
	private Usuario usuario;

	public Pessoa(String cpf, String nome, String email, String telefone, String endereco) {
		this.cpf = cpf;
		this.nome = nome;
		this.email = email;
		this.telefones = new ArrayList<>();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelefones(List<String> telefones) {
		this.telefones = telefones;
	}

	public void adicionarTelefone(String telefone) {
		this.telefones.add(telefone);
	}

	public void deletarTelefone(String telefone) {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
