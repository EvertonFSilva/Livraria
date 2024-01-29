package br.edu.iff.livraria.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import jakarta.validation.constraints.Size;

@Entity
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Item> itens = new ArrayList<>();

    private float valorTotal;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_pedido")
    private Date dataPedido;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_entrega")
    private Date dataEntrega;

    @NotBlank(message = "Tipo não pode ser em branco ou nulo")
    @Size(min = 3, max = 30, message = "Tipo deve ter entre 3 e 30 caracteres")
    private String formaPagamento;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private boolean finalizado;
	
    public Pedido(Cliente cliente) {
        this.cliente = cliente;
    	this.finalizado = false;
        this.valorTotal = 0;
        this.dataPedido = null;
        this.dataEntrega = null;
        this.formaPagamento = "";
    }

    public Pedido() {
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

    public Item getItemById(Long itemId) {
        for (Item item : itens) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }

    public void adicionarItem(Item item) {
        this.itens.add(item);
    }

    public void removerItem(Item item) {
        this.itens.remove(item);
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
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
	
	public boolean getFinalizado() {
		return finalizado;
	}
		
	public void finalizar() {
		this.finalizado = true;
		this.dataPedido = new Date();
	}
}