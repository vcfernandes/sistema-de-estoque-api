package Aquino.Sistema_de_Estoque.Controller;

import Aquino.Sistema_de_Estoque.DTO.PrateleiraDto;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Repository.PrateleiraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prateleiras")
@RequiredArgsConstructor
public class PrateleiraController {

    private final PrateleiraRepository prateleiraRepository; 

    @PostMapping
    public ResponseEntity<Prateleira> criarPrateleira(@RequestBody PrateleiraDto dto) {
        Prateleira novaPrateleira = new Prateleira();
        novaPrateleira.setCodigo(dto.getCodigo());
        Prateleira prateleiraSalva = prateleiraRepository.save(novaPrateleira);
        return new ResponseEntity<>(prateleiraSalva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Prateleira>> listarPrateleiras() {
        return ResponseEntity.ok(prateleiraRepository.findAll());
    }
}