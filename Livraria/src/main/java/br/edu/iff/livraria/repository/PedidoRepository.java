package br.edu.iff.livraria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	@Query(value = "SELECT * FROM Pedido WHERE id = ?1", nativeQuery = true)
	Pedido buscarPorId(Long id);

	@Query(value = "SELECT * FROM Pedido WHERE cliente_id = ?1 AND finalizado = false", nativeQuery = true)
	List<Pedido> buscarPedidosEmProgresso(Long clienteId);

	@Query(value = "SELECT * FROM Pedido", nativeQuery = true)
	List<Pedido> listarPedidos();
}
