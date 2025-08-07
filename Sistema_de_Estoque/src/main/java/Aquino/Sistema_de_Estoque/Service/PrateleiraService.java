package Aquino.Sistema_de_Estoque.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import Aquino.Sistema_de_Estoque.DTO.PrateleiraDto;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.PrateleiraRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrateleiraService {
    private final PrateleiraRepository prateleiraRepository;

        public Prateleira criarPrateleira(PrateleiraDto dto, Usuario usuarioLogado) {
        // Validação para garantir que um usuário não tenha duas prateleiras com o mesmo código
        if (prateleiraRepository.findByCodigoAndUsuario(dto.getCodigo(), usuarioLogado).isPresent()) {
            throw new IllegalStateException("Prateleira com o código '" + dto.getCodigo() + "' já existe para este usuário.");
        }

        Prateleira novaPrateleira = new Prateleira();
        novaPrateleira.setCodigo(dto.getCodigo());
        novaPrateleira.setUsuario(usuarioLogado); // Associa a prateleira ao dono

        return prateleiraRepository.save(novaPrateleira);
    }

    public List<Prateleira> listarPrateleiras(Usuario usuarioLogado) {
        // Por enquanto, não há lógica de admin aqui, cada usuário vê apenas as suas.
        return prateleiraRepository.findByUsuario(usuarioLogado);
    }
    // Adicionar métodos para deletar, etc. com verificação de dono
}