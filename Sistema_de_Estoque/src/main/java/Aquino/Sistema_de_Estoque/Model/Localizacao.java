package Aquino.Sistema_de_Estoque.Model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "localizacoes",
       // Garante que não se pode colocar o mesmo produto no mesmo lugar duas vezes
       uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "prateleira_id", "linha", "coluna"}))
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prateleira_id", nullable = false)
    private Prateleira prateleira;

    // As coordenadas da sua imagem!
    @Column(nullable = false)
    private String linha; // Ex: "A", "B", "C"

    @Column(nullable = false)
    private String coluna; // Ex: "1", "2", "3"
}