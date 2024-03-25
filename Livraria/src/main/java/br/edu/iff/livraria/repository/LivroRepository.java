package br.edu.iff.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Livro;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

	@Query(value = "SELECT * FROM Livro WHERE id = ?1", nativeQuery = true)
	Livro buscarPorId(Long livroId);

	@Query(value = "SELECT * FROM Livro WHERE titulo = ?1", nativeQuery = true)
	Livro buscarPorTitulo(String titulo);

	@Query(value = "SELECT * FROM Livro WHERE titulo LIKE %:parteDoTitulo%", nativeQuery = true)
	List<Livro> buscarLivrosPorTituloContendo(String parteDoTitulo);

	@Query("SELECT l FROM Livro l WHERE " + "(:titulo IS NULL OR l.titulo LIKE %:titulo%) AND "
			+ "(:autor IS NULL OR l.autor LIKE %:autor%) AND " + "(:genero IS NULL OR l.genero LIKE %:genero%) AND "
			+ "(:precoMin IS NULL OR l.preco >= :precoMin) AND " + "(:precoMax IS NULL OR l.preco <= :precoMax)")
	List<Livro> filtrarLivros(String titulo, String autor, String genero, Float precoMin, Float precoMax);

	@Query(value = "SELECT * FROM Livro", nativeQuery = true)
	List<Livro> listarLivros();
}
