package br.edu.iff.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Entrega;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

}
