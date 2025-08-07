package Aquino.Sistema_de_Estoque.Repository;

import Aquino.Sistema_de_Estoque.Model.Produto;
import Aquino.Sistema_de_Estoque.Model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeAndUsuario(String nome, Usuario usuario);

    List<Produto> findAllByUsuarioOrderByIdAsc(Usuario usuario);

    // Adicione este de volta para o Admin
    List<Produto> findAllByOrderByIdAsc();
}