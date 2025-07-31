package Aquino.Sistema_de_Estoque.Repository;

import Aquino.Sistema_de_Estoque.Model.Produto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository // Anotação que marca esta interface como um componente de repositório do Spring.
            // Ajuda na tradução de exceções do JPA para exceções do Spring.
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
  
  Optional<Produto> findByNome(String nome); // O Spring entende que você quer buscar um produto pelo seu atributo 'nome'.

  @Query("SELECT p FROM Produto p ORDER BY p.id ASC")
   List<Produto> findAllByOrderByIdAsc();

}