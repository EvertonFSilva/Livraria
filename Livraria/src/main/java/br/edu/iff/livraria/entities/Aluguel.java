package br.edu.iff.livraria.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Aluguel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Livro> livrosAlugados;

    @ManyToOne
    private Cliente cliente;

    @ManyToMany
    @JoinTable(
        name = "aluguel_funcionario",
        joinColumns = @JoinColumn(name = "aluguel_id"),
        inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private List<Funcionario> funcionarios = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    public Aluguel(List<Livro> livrosAlugados, Cliente cliente, Date dataInicio, Date dataFim) {
        this.livrosAlugados = livrosAlugados;
        this.cliente = cliente;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Aluguel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Livro> getLivrosAlugados() {
        return livrosAlugados;
    }

    public void adicionarLivroAlugado(Livro livro) {
        this.livrosAlugados.add(livro);
    }

    public void removerLivroAlugado(Livro livro) {
        this.livrosAlugados.remove(livro);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public void adicionarFuncionario(Funcionario funcionario) {
        this.funcionarios.add(funcionario);
    }

    public void removerFuncionario(Funcionario funcionario) {
        this.funcionarios.remove(funcionario);
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
