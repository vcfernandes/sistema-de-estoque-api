package Aquino.Sistema_de_Estoque.Controller;
import Aquino.Sistema_de_Estoque.DTO.EntradaSaidaDto;
import Aquino.Sistema_de_Estoque.Model.Transacao;
import Aquino.Sistema_de_Estoque.Service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    // Endpoint para registrar uma ENTRADA de estoque
    // POST http://localhost:5433/api/transacoes/entrada
    @Operation(summary = "Registra Entrada no Estoque",description = "Registra uma entrada de estoque para um produto específico. O corpo da requisição deve conter os detalhes da entrada.")
    @PostMapping("/entrada")
    public ResponseEntity<Transacao> registrarEntrada(@Valid @RequestBody EntradaSaidaDto entradaDto) {
        Transacao novaTransacao = transacaoService.registrarEntrada(entradaDto);
        return ResponseEntity.ok(novaTransacao);
    }

    // Endpoint para registrar uma SAÍDA de estoque
    // POST http://localhost:5433/api/transacoes/saida
    @Operation(summary = "Registra Saida no Estoque",description = "Registra uma saída de estoque para um produto específico. O corpo da requisição deve conter os detalhes da saída.")
    @PostMapping("/saida")
    public ResponseEntity<Transacao> registrarSaida(@Valid @RequestBody EntradaSaidaDto saidaDto) {
        Transacao novaTransacao = transacaoService.registrarSaida(saidaDto);
        return ResponseEntity.ok(novaTransacao);
    }
}