package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoResponse;
import br.com.desenvolvimento.logica.estoque_service.exception.ValidationException;
import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;
import br.com.desenvolvimento.logica.estoque_service.repository.MovimentacaoRepository;
import br.com.desenvolvimento.logica.estoque_service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoService produtoService;

    public Page<MovimentacaoResponse> listar(Long idProduto, String tipo, LocalDate data, Pageable pageable) {
        LocalDateTime inicio = null;
        LocalDateTime fim = null;

        if (data != null) {
            inicio = data.atStartOfDay();
            fim = data.atTime(LocalTime.MAX);
        }

        Page<Movimentacao> page = movimentacaoRepository.filtrar(
                idProduto != null ? produtoService.consultarPorId(idProduto) : null,
                tipo != null && !tipo.isBlank() ? TipoMovimentacao.valueOf(tipo.toUpperCase()) : null,
                inicio,
                fim,
                pageable
                );

        return page.map(this::toResponse);
    }

    public Movimentacao consultarPorId(final Long id) {
        return movimentacaoRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Movimentação não encontrada."));
    }

    public MovimentacaoResponse registrar(MovimentacaoRequest movimentacaoRequest) {

        var produto = produtoService.consultarPorId(movimentacaoRequest.getProduto().getId());

        if (movimentacaoRequest.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() + movimentacaoRequest.getQuantidade());
            produto.setPrecoCompra(movimentacaoRequest.getPrecoCompra());
        } else {
            if (produto.getQuantidadeAtual() < movimentacaoRequest.getQuantidade()) {
                throw new ValidationException("Quantidade em estoque inferior a quantidade de saída.");
            }
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - movimentacaoRequest.getQuantidade());
        }

        produtoService.atualizar(produto);
        Movimentacao movimentacao = movimentacaoRepository.save(toEntity(movimentacaoRequest, produto));
        return toResponse(movimentacao);
    }

    public MovimentacaoResponse atualizar(MovimentacaoRequest movimentacaoRequest) {
        if (movimentacaoRequest.getId() == null) {
            throw new ValidationException("ID é obrigatório");
        }

        if (movimentacaoRequest.getJustificativa() == null) {
            throw new ValidationException("Justificativa é obrigatório");
        }

        Movimentacao movimentacao = consultarPorId(movimentacaoRequest.getId());

        var produto = produtoService.consultarPorId(movimentacaoRequest.getProduto().getId());
        if (movimentacaoRequest.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() + movimentacaoRequest.getQuantidade());
            produto.setPrecoCompra(movimentacaoRequest.getPrecoCompra());
        } else {
            if (produto.getQuantidadeAtual() < movimentacaoRequest.getQuantidade()) {
                throw new ValidationException("Quantidade em estoque inferior a quantidade de saída.");
            }
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - movimentacaoRequest.getQuantidade());
        }
        produtoService.atualizar(produto);
        movimentacao.setProduto(produto);
        movimentacao.setTipo(movimentacaoRequest.getTipo());
        movimentacao.setQuantidade(movimentacaoRequest.getQuantidade());
        movimentacao.setJustificativa(movimentacaoRequest.getJustificativa());
        movimentacao.setData(LocalDateTime.now());
        movimentacao.setSituacao(Situacao.ALTERADO);
        movimentacaoRepository.save(movimentacao);
        return toResponse(movimentacao);
    }

    public void excluir(final Long id, final String justificativa) {
        Movimentacao movimentacao = consultarPorId(id);
        movimentacao.setJustificativa(justificativa);
        movimentacao.setSituacao(Situacao.INATIVO);
        movimentacao.setData(LocalDateTime.now());
        movimentacaoRepository.save(movimentacao);
    }

    private Movimentacao toEntity(MovimentacaoRequest movimentacaoRequest, Produto produto) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(movimentacaoRequest.getTipo());
        movimentacao.setQuantidade(movimentacaoRequest.getQuantidade());
        movimentacao.setSituacao(Situacao.ATIVO);
        return movimentacao;
    }

    private MovimentacaoResponse toResponse(Movimentacao movimentacao) {
        MovimentacaoResponse movimentacaoResponse = new MovimentacaoResponse();
        movimentacaoResponse.setId(movimentacao.getId());
        movimentacaoResponse.setProduto(produtoService.toResponse(movimentacao.getProduto()));
        movimentacaoResponse.setTipo(movimentacao.getTipo().name());
        movimentacaoResponse.setQuantidade(movimentacao.getQuantidade());
        movimentacaoResponse.setDataMovimentacao(DataUtil.LocalDateTimeToString(
                movimentacao.getData(),
                DataUtil.PATTERN_DDMMYYYYHHMMSS)
        );
        return movimentacaoResponse;
    }
}
