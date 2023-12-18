package br.edu.iff.livraria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Funcionario extends Pessoa {

    @NotBlank(message = "Não pode ser em branco ou nulo")
    @Size(min = 1, max = 30, message = "Tem que ter entre 1 e 30 caractéres")
    @Column(unique = true, length = 30)
    private String cargo;

    @Positive(message = "Tem que ser maior que 0")
    private float salario;

    @OneToMany(mappedBy = "funcionario")
    private List<Pedido> historicoVendas = new ArrayList<>();

    @ManyToMany
    private List<Aluguel> historicoAlugueis = new ArrayList<>();

    public Funcionario(String cpf, String nome, String senha, String email, String telefone, String endereco,
            String cargo, float salario) {
        super(cpf, nome, senha, email, telefone, endereco);
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

    public List<Pedido> getHistoricoVendas() {
        return historicoVendas;
    }

    public void adicionarVendaAoHistorico(Pedido venda) {
        this.historicoVendas.add(venda);
    }

    public void removerVendaDoHistorico(Pedido venda) {
        this.historicoVendas.remove(venda);
    }

    public void adicionarAluguelAoHistorico(Aluguel aluguel) {
        this.historicoAlugueis.add(aluguel);
    }

    public void removerAluguelDoHistorico(Aluguel aluguel) {
        this.historicoAlugueis.remove(aluguel);
    }

    public List<Aluguel> getHistoricoAlugueis() {
        return historicoAlugueis;
    }
}
