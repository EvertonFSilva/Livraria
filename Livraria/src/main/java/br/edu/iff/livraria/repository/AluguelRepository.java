package br.edu.iff.livraria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Aluguel;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
	
	@Query(value = "SELECT * FROM Aluguel WHERE id = ?1", nativeQuery = true)
	Aluguel buscarPorId(Long id);

	@Query(value = "SELECT cliente_id FROM Aluguel WHERE id = ?1", nativeQuery = true)
	Long buscarAluguelPeloClienteId(Long clienteId);

	@Query(value = "SELECT * FROM Aluguel WHERE cliente_id = ?1 AND finalizado = false", nativeQuery = true)
	List<Aluguel> buscarAlugueisEmProgresso(Long clienteId);

	@Query(value = "SELECT * FROM Aluguel", nativeQuery = true)
	List<Aluguel> listarAlugueis();
}
