package br.edu.iff.livraria.entities;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Login não pode ser em branco ou nulo")
    @Size(min = 3, max = 30, message = "Login deve ter entre 3 e 30 caracteres")
    private String login;

    @NotBlank(message = "Senha não pode ser em branco ou nula")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotBlank(message = "permissao não pode ser em branco ou nulo")
	@Positive(message = "Tem que ser maior que 0")
    private int permissao;

    public Usuario(String login, String senha, int permissao) {
        this.login = login;
        this.senha = senha;
        this.permissao = permissao;
    }
    
    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }
}
