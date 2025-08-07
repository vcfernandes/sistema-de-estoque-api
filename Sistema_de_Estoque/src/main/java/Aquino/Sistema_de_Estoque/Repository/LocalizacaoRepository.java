package Aquino.Sistema_de_Estoque.Repository;

import Aquino.Sistema_de_Estoque.Model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    // Método para encontrar todas as localizações de um produto específico
    List<Localizacao> findByProdutoId(Long produtoId);

@Modifying // Necessário para consultas de delete ou update
void deleteByProdutoId(Long produtoId);
}