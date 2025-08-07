package Aquino.Sistema_de_Estoque.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


import com.fasterxml.jackson.annotation.JsonIgnore;


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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @Column
    private String descricao;

    @Column(nullable = false)
    private int quantidadeEmEstoque;

    @Column(nullable = false)
    private BigDecimal preco;

    
  
}