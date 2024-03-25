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
import jakarta.validation.Valid;
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

    @NotBlank(message = "O CPF não pode ser em branco ou nulo")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
    @Column(unique = true, length = 11)
    private String cpf;

    @NotBlank(message = "O nome não pode ser em branco ou nulo")
    @Size(min = 3, max = 60, message = "O nome deve conter entre 3 e 60 caracteres")
    @Column(length = 60)
    private String nome;

    @Email(message = "O email deve ser válido")
    @NotBlank(message = "O email não pode ser em branco ou nulo")
    @Column(length = 60)
    private String email;

    @Nullable
    @ElementCollection
    @Size(min = 1, max = 2, message = "Deve haver entre 1 e 2 números de telefone")
    @Valid
    private List<@NotBlank(message = "O telefone não pode ser em branco ou nulo") @Pattern(regexp = "\\d{9,11}", message = "O telefone deve conter entre 9 e 11 dígitos numéricos") String> telefones;

    @Size(min = 10, max = 100, message = "O endereço deve conter entre 10 e 100 caracteres")
    @NotBlank(message = "O endereço não pode ser em branco ou nulo")
    @Column(length = 100)
    private String endereco;

	@ManyToOne
	@JoinColumn(name = "usuario_fk")
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

	public void atualizarTelefone(String telefoneAntigo, String telefoneNovo) {
	    if (this.telefones != null && !this.telefones.isEmpty()) {
	        int index = this.telefones.indexOf(telefoneAntigo);
	        if (index != -1) {
	            this.telefones.set(index, telefoneNovo);
	        }
	    }
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
