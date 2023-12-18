package br.edu.iff.livraria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente extends Pessoa {

	@OneToMany
	@JoinColumn(name = "id_cliente")
	private List<Pedido> pedido;

	@ElementCollection
	@Column(name = "historico_compras")
	private List<Pedido> historicoPedidos;

	public Cliente(String cpf, String nome, String senha, String email, String telefone, String endereco) {
		super(cpf, nome, senha, email, telefone, endereco);
		this.pedido = new ArrayList<>();
		this.historicoPedidos = new ArrayList<>();
	}

	public Cliente() {
	}

	public void adicionarPedido(Pedido pedido) {
		this.pedido.add(pedido);
	}

	public void removerPedido(Pedido pedido) {
		this.pedido.remove(pedido);
	}

	public void adicionarPedidoAoHistorico(Pedido pedido) {
		this.historicoPedidos.add(pedido);
	}

	public void removerPedidoDoHistorico(Pedido pedido) {
		this.historicoPedidos.remove(pedido);
	}
}