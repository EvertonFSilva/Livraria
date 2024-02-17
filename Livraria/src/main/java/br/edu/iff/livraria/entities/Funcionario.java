package br.edu.iff.livraria.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Funcionario extends Pessoa {

    @NotBlank(message = "Não pode ser em branco ou nulo")
    @Size(min = 4, max = 30, message = "Tem que ter entre 4 e 30 caractéres")
    @Column(unique = true, length = 30)
    private String cargo;

    @Positive(message = "Tem que ser maior que 0")
    private float salario;

    public Funcionario(String cpf, String nome, String email, String telefone, String endereco,
            String cargo, float salario) {
        super(cpf, nome, email, telefone, endereco);
        this.cargo = cargo;
        this.salario = salario;
    }

    public Funcionario() {
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }
}
