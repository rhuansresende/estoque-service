package br.com.desenvolvimento.logica.estoque_service.repository.custom;

import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface MovimentacaoRepositoryCustom {

    Page<Movimentacao> filtrar(
            Produto produto,
            TipoMovimentacao tipoMovimentacao,
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable);
}
