package br.edu.iff.livraria.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Cliente extends Pessoa {

    @OneToMany
    @JoinColumn(name = "cliente_id")
    private List<Pedido> pedido;


	public Cliente(String cpf, String nome, String email, String telefone, String endereco) {
		super(cpf, nome, email, telefone, endereco);
		this.pedido = new ArrayList<>();
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
}