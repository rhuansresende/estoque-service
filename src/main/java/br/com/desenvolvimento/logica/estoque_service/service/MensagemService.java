package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.MensagemResponse;
import br.com.desenvolvimento.logica.estoque_service.exception.ValidationException;
import br.com.desenvolvimento.logica.estoque_service.model.Mensagem;
import br.com.desenvolvimento.logica.estoque_service.model.StatusMensagem;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMensagem;
import br.com.desenvolvimento.logica.estoque_service.repository.MensagemRepository;
import br.com.desenvolvimento.logica.estoque_service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ProdutoService produtoService;

    public Page<MensagemResponse> listar(String titulo, String tipoMensagem, LocalDate dataCriacao, Pageable pageable) {
        LocalDateTime inicio = null;
        LocalDateTime fim = null;

        if (dataCriacao != null) {
            inicio = dataCriacao.atStartOfDay();
            fim = dataCriacao.atTime(LocalTime.MAX);
        }

        Page<Mensagem> page = mensagemRepository.filtrar(
                StatusMensagem.NAO_LIDA,
                titulo != null && !titulo.isBlank() ? titulo : null,
                tipoMensagem != null && !tipoMensagem.isBlank() ? TipoMensagem.valueOf(tipoMensagem.toUpperCase()) : null,
                inicio,
                fim,
                pageable
                );

        return page.map(this::toResponse);
    }

    public Mensagem consultarPorId(final Long id) {
        return mensagemRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Mensagem não encontrada."));
    }

    public void verificarProdutos() {
        produtoService.consultarProdutosComEstoqueBaixo()
                .forEach(produtoResponse -> {
                    String textoMensagem = String.format(
                            "O produto %s está com o estoque crítico. Faça a compra imediatamente.",
                            produtoResponse.getNome()
                    );

                    Mensagem mensagem = new Mensagem();
                    mensagem.setTitulo("Produto com estoque crítico.");
                    mensagem.setMensagem(textoMensagem);
                    mensagem.setTipoMensagem(TipoMensagem.CRITICA);
                    mensagemRepository.save(mensagem);
                });
    }

    public MensagemResponse lerMensagem(final Long id) {
        Mensagem mensagem = consultarPorId(id);
        mensagem.setStatus(StatusMensagem.LIDA);
        mensagem.setAtualizadoEm(LocalDateTime.now());
        mensagemRepository.save(mensagem);
        return toResponse(mensagem);
    }

    public void excluirMensagem(final Long id) {
        Mensagem mensagem = consultarPorId(id);
        mensagem.setStatus(StatusMensagem.EXCLUIDA);
        mensagem.setAtualizadoEm(LocalDateTime.now());
        mensagemRepository.save(mensagem);
    }

    private MensagemResponse toResponse(Mensagem mensagem) {
        MensagemResponse mensagemResponse = new MensagemResponse();
        mensagemResponse.setId(mensagem.getId());
        mensagemResponse.setTitulo(mensagem.getTitulo());
        mensagemResponse.setMensagem(mensagem.getMensagem());
        mensagemResponse.setTipoMensagem(mensagem.getTipoMensagem());
        mensagemResponse.setDataCriacao(DataUtil.LocalDateTimeToString(
                mensagem.getCriadoEm(),
                DataUtil.PATTERN_DDMMYYYYHHMMSS));
        return mensagemResponse;
    }
}
