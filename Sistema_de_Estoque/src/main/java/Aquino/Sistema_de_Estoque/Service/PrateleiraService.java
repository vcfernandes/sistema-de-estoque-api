package Aquino.Sistema_de_Estoque.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import Aquino.Sistema_de_Estoque.DTO.PrateleiraDto;
import Aquino.Sistema_de_Estoque.Exception.BusinessException;
import Aquino.Sistema_de_Estoque.Exception.ResourceNotFoundException;
import Aquino.Sistema_de_Estoque.Model.Prateleira;
import Aquino.Sistema_de_Estoque.Model.Usuario;
import Aquino.Sistema_de_Estoque.Repository.LocalizacaoRepository;
import Aquino.Sistema_de_Estoque.Repository.PrateleiraRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PrateleiraService {
    private final PrateleiraRepository prateleiraRepository;
    private final LocalizacaoRepository localizacaoRepository;

        public Prateleira criarPrateleira(PrateleiraDto dto, Usuario usuarioLogado) {
        // Validação para garantir que um usuário não tenha duas prateleiras com o mesmo código
        if (prateleiraRepository.findByCodigoAndUsuario(dto.getCodigo(), usuarioLogado).isPresent()) {
            throw new BusinessException("Prateleira com o código '" + dto.getCodigo() + "' já existe para este usuário.");
        }

        Prateleira novaPrateleira = new Prateleira();
        novaPrateleira.setCodigo(dto.getCodigo());
        novaPrateleira.setUsuario(usuarioLogado); 

        return prateleiraRepository.save(novaPrateleira);
    }

    public List<Prateleira> listarPrateleiras(Usuario usuarioLogado) {
        return prateleiraRepository.findByUsuario(usuarioLogado);
    }

    @Transactional
    public Prateleira atualizarPrateleira(Long prateleiraId, PrateleiraDto prateleiraDto, Usuario usuarioLogado) {
        Prateleira prateleiraExistente = prateleiraRepository.findById(prateleiraId)
                .orElseThrow(() -> new ResourceNotFoundException("Prateleira com ID " + prateleiraId + " não encontrada."));
        if (!isAdmin(usuarioLogado) && !prateleiraExistente.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new SecurityException("Acesso negado: Você não tem permissão para modificar esta prateleira.");
}
        prateleiraRepository.findByCodigoAndUsuario(prateleiraDto.getCodigo(), prateleiraExistente.getUsuario())
        .ifPresent(prateleiraEncontrada -> {
            if (!prateleiraEncontrada.getId().equals(prateleiraId)) {
                throw new BusinessException("O código '" + prateleiraDto.getCodigo() + "' já está em uso por outra prateleira deste usuário.");
            }
        });
        prateleiraExistente.setCodigo(prateleiraDto.getCodigo());


        return prateleiraRepository.save(prateleiraExistente);
    }

    // ... dentro da classe PrateleiraService

@Transactional
public void deletarPrateleira(Long prateleiraId, Usuario usuarioLogado) {
    Prateleira prateleira = prateleiraRepository.findById(prateleiraId)
            .orElseThrow(() -> new ResourceNotFoundException("Prateleira com ID " + prateleiraId + " não encontrada."));

    if (!prateleira.getUsuario().getId().equals(usuarioLogado.getId())) {
        throw new SecurityException("Acesso negado: Você não tem permissão para deletar esta prateleira.");
    }
    
    if (localizacaoRepository.existsByPrateleiraId(prateleiraId)) {
        throw new BusinessException("Não é possível deletar a prateleira pois ela está em uso por uma ou mais localizações de produtos.");
    }

    prateleiraRepository.delete(prateleira);
}

    private boolean isAdmin(Usuario usuario) {
        return usuario.getRoles().stream()
            .anyMatch(role -> role.getNome().equals("ROLE_ADMIN"));
    }
}