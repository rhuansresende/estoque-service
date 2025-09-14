package br.com.desenvolvimento.logica.estoque_service.repository.custom;

import br.com.desenvolvimento.logica.estoque_service.model.Mensagem;
import br.com.desenvolvimento.logica.estoque_service.model.StatusMensagem;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface MensagemRepositoryCustom {

    Page<Mensagem> filtrar(StatusMensagem status,
                           String titulo,
                           TipoMensagem tipoMensagem,
                           LocalDateTime inicio,
                           LocalDateTime fim,
                           Pageable pageable);
}
