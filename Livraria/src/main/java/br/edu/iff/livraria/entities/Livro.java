package br.edu.iff.livraria.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Livro implements Serializable {

	protected static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Não pode ser em branco ou nulo")
	@Size(min = 1, max = 60, message = "Tem que ter entre 1 e 60 caractéres")
	@Column(unique = true, length = 60)
	private String titulo;

	@Size(min = 1, max = 60, message = "Tem que ter entre 1 e 60 caractéres")
	@Column(length = 60)
	private String autor;

	@Size(min = 1, max = 20, message = "Tem que ter entre 1 e 20 caractéres")
	@Column(length = 20)
	private String genero;

	@Positive(message = "Tem que ser maior que 0")
	private int qtdPaginas;

	public Livro(String titulo, String autor, String genero, int qtdPaginas) {
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.qtdPaginas = qtdPaginas;
	}

	public Livro() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getQtdPaginas() {
		return qtdPaginas;
	}

	public void setQtdPaginas(int qtdPaginas) {
		this.qtdPaginas = qtdPaginas;
	}
}