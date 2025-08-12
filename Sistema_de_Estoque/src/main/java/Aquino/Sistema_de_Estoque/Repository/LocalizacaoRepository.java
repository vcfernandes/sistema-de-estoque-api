package Aquino.Sistema_de_Estoque.Repository;

import Aquino.Sistema_de_Estoque.Model.Localizacao;
import Aquino.Sistema_de_Estoque.Model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
    // Método para encontrar todas as localizações de um produto específico
    List<Localizacao> findByProdutoId(Long produtoId);

@Modifying // Necessário para consultas de delete ou update
void deleteByProdutoId(Long produtoId);

boolean existsByPrateleiraId(Long prateleiraId);

@Query("SELECT l FROM Localizacao l WHERE l.produto.usuario = :usuario")
List<Localizacao> findAllByUsuario(@Param("usuario") Usuario usuario);
}