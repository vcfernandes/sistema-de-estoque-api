package Aquino.Sistema_de_Estoque.Model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "prateleiras")
public class Prateleira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Um código único para a estante inteira, ex: "ESTANTE-PRINCIPAL"
    @Column(nullable = false, unique = true)
    private String codigo;

}