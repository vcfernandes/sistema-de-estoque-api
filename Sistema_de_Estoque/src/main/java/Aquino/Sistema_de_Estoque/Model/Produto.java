package Aquino.Sistema_de_Estoque.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data // Gera @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor // Gera um construtor sem argumentos (obrigatório para JPA)
@AllArgsConstructor // Gera um construtor com todos os argumentos (útil para testes)
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // INSTRUÇÃO: "A coluna 'nome' não pode ser vazia e nenhum produto pode ter o mesmo nome."
    private String nome;

    @Column
    private String descricao;

    @Column(nullable = false)
    private int quantidadeEmEstoque;

    @Column(nullable = false)
    private BigDecimal preco;

    @ManyToMany(fetch = FetchType.LAZY) // LAZY é a melhor prática para performance
    @JoinTable(
        name = "produto_prateleira", // Nome da tabela de junção que será criada
        joinColumns = @JoinColumn(name = "produto_id"), // Coluna que se refere a esta entidade (Produto)
        inverseJoinColumns = @JoinColumn(name = "prateleira_id") // Coluna que se refere à outra entidade (Prateleira)
    )
    @JsonIgnoreProperties("produtos") // Evita recursão infinita ao serializar para JSON
    private Set<Prateleira> prateleiras = new HashSet<>(); // Um conjunto (Set) de prateleiras associadas a este produto

}