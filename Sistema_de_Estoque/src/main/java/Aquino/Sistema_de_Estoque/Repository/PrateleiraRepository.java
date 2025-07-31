package Aquino.Sistema_de_Estoque.Repository;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrateleiraRepository extends JpaRepository<Prateleira, Long> {
    
}