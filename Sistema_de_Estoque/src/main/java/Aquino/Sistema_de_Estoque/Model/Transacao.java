package Aquino.Sistema_de_Estoque.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // INSTRUÇÃO: "Muitas 'Transacoes' se relacionam com um 'Produto'."
    @JoinColumn(name = "produto_id", nullable = false) //"Para fazer a junção, crie uma coluna chamada 'produto_id' na tabela 'transacoes' "
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;
}