package br.edu.iff.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Cliente;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	@Query(value = "SELECT * FROM Cliente WHERE id = ?1", nativeQuery = true)
	Cliente buscarPorId(Long id);

	@Query(value = "SELECT * FROM Cliente WHERE cpf = ?1", nativeQuery = true)
	Cliente buscarPorCPF(String cpf);

	@Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM Cliente c JOIN c.telefones t WHERE c.cpf = ?1 AND ?2 IN elements(t)) THEN true ELSE false END")
	boolean existeTelefoneNoCPF(String cpf, String telefone);

	@Query(value = "SELECT * FROM Cliente", nativeQuery = true)
	List<Cliente> listarClientes();
}
