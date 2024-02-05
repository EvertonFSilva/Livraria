package br.edu.iff.livraria.repository;

import br.edu.iff.livraria.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = "SELECT * FROM Usuario WHERE id = ?1", nativeQuery = true)
	Usuario buscarPorId(@Param("id") Long id);

	@Query(value = "SELECT * FROM Usuario WHERE login = ?1", nativeQuery = true)
	Usuario buscarPorLogin(@Param("login") String login);

	@Query(value = "SELECT * FROM Usuario", nativeQuery = true)
	List<Usuario> listarUsuarios();
}