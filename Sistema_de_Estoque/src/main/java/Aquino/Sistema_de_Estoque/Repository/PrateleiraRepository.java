package Aquino.Sistema_de_Estoque.Repository;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Model.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrateleiraRepository extends JpaRepository<Prateleira, Long> {
    List<Prateleira> findByUsuario(Usuario usuario);
    Optional<Prateleira> findByCodigoAndUsuario(String codigo, Usuario usuario);
}