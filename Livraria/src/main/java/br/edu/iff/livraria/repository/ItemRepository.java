package br.edu.iff.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}