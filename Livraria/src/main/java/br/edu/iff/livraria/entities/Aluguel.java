package br.edu.iff.livraria.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Entity
public class Aluguel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "aluguel", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Item> itens = new ArrayList<>();

	@PositiveOrZero(message = "O valor total deve ser maior ou igual a 0")
	private float valorTotal;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inicio")
	private LocalDateTime dataInicio;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_fim")
	private LocalDateTime dataFim;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_entrega")
	private LocalDateTime dataEntrega;

	@NotBlank(message = "A forma de pagamento não pode estar em branco ou nula")
	@Size(min = 3, max = 30, message = "A forma de pagamento deve ter entre 3 e 30 caracteres")
	@Column(name = "forma_pagamento")
	private String formaPagamento;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@JsonIgnore
	private Cliente cliente;

	private boolean finalizado;

	public Aluguel(Cliente cliente, String formaPagamento) {
		this.cliente = cliente;
		this.finalizado = false;
		this.valorTotal = 0;
		this.dataInicio = null;
		this.dataFim = null;
		this.dataEntrega = null;
		this.formaPagamento = formaPagamento;
	}

	public Aluguel() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}
	
	public Item getItemById(Long itemId) {
	    for (Item item : itens) {
	        if (item.getId().equals(itemId)) {
	            return item;
	        }
	    }
	    return null;
	}

    public Item getItemByLivroId(Long livroId) {
        for (Item item : itens) {
            if (item.getLivro().getId().equals(livroId)) {
                return item;
            }
        }
        return null;
    }

	public void adicionarItem(Item item) {
		this.itens.add(item);
		this.valorTotal += item.getQuantidade() * item.getLivro().getPreco();
		item.setAluguel(this);
	}

	public void removerItem(Item item) {
		this.itens.remove(item);
		this.valorTotal -= item.getQuantidade() * item.getLivro().getPreco();
		item.setAluguel(null);
	}

	public float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDateTime getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDateTime getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDateTime dataFim) {
		this.dataFim = dataFim;
	}

	public LocalDateTime getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(LocalDateTime dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

	public void finalizar() {
		this.dataInicio = LocalDateTime.now();
		this.dataFim = this.dataInicio.plusDays(10);
		this.dataEntrega = LocalDateTime.now().toLocalDate().atStartOfDay();
		this.finalizado = true;
	}
}
