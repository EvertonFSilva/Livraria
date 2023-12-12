package br.edu.iff.livraria.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Positive;

@Entity
public class Item implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Positive(message = "Tem que ser maior que 0")
	private double preco;

	@ManyToMany
	@JoinTable(name = "associacao_pedido_item", 
				joinColumns = @JoinColumn(name = "fk_pedido"),
				inverseJoinColumns = @JoinColumn(name = "fk_item"))
	private List<Pedido> pedido;

	public Item(float preco) {
		this.preco = preco;
	}

	public Item() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}
}