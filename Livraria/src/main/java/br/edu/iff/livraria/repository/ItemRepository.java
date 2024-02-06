package br.edu.iff.livraria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.edu.iff.livraria.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM Item WHERE id = ?1", nativeQuery = true)
    Item buscarPorId(Long id);
    
    @Query(value = "SELECT * FROM Item", nativeQuery = true)
    List<Item> listarItens();    
}
