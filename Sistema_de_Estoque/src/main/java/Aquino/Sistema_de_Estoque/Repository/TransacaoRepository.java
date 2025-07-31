package Aquino.Sistema_de_Estoque.Repository;

import Aquino.Sistema_de_Estoque.Model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
 
  boolean existsByProdutoId(Long produtoId);
}