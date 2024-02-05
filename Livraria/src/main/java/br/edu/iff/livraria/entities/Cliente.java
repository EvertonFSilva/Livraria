package br.edu.iff.livraria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente extends Pessoa {

	private static final long serialVersionUID = 1L;

	@OneToMany
	@JoinColumn(name = "cliente_id")
	private List<Pedido> pedido;

	@OneToMany
	@JoinColumn(name = "cliente_id")
	private List<Aluguel> aluguel;

	public Cliente(String cpf, String nome, String email, String telefone, String endereco) {
		super(cpf, nome, email, telefone, endereco);
		this.pedido = new ArrayList<>();
		this.aluguel = new ArrayList<>();
	}

	public Cliente() {
	}

	public void adicionarPedido(Pedido pedido) {
		this.pedido.add(pedido);
	}

	public void removerPedido(Pedido pedido) {
		this.pedido.remove(pedido);
	}

	public List<Pedido> getPedido() {
		return pedido;
	}

	public void adicionarAluguel(Aluguel aluguel) {
		this.aluguel.add(aluguel);
	}

	public void removerAluguel(Aluguel aluguel) {
		this.aluguel.remove(aluguel);
	}

	public List<Aluguel> getAluguel() {
		return aluguel;
	}

}