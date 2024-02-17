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

    @Query(value = "SELECT a.* FROM Aluguel a INNER JOIN Item i ON a.item_id = i.id WHERE i.id = ?1", nativeQuery = true)
    Aluguel buscarPorItemId(Long itemId);

	@Query(value = "SELECT * FROM Aluguel", nativeQuery = true)
	List<Aluguel> listarAlugueis();
	
	@Query(value = "SELECT a.* FROM Aluguel a INNER JOIN Item i ON a.item_id = i.id INNER JOIN Pedido p ON i.pedido_id = p.id WHERE p.cliente_id = ?1", nativeQuery = true)
	List<Aluguel> listarAlugueisPorCliente(Long clienteId);
}
