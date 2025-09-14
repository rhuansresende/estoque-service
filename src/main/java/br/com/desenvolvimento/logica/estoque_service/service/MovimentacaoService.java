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
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private MensagemService mensagemService;

    public List<MovimentacaoResponse> listar() {
        return movimentacaoRepository.findAll()
                .stream()
                .filter(movimentacao -> movimentacao.getSituacao() != Situacao.INATIVO)
                .sorted((m1, m2) -> m1.getData().compareTo(m2.getData()))
                .map(this::toResponse)
                .toList();
    }

    public Movimentacao consultarPorId(final Long id) throws BadRequestException {
        return movimentacaoRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Movimentação não encontrada."));
    }

    public MovimentacaoResponse registrar(MovimentacaoRequest movimentacaoRequest) throws BadRequestException {

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

    public MovimentacaoResponse atualizar(MovimentacaoRequest movimentacaoRequest) throws BadRequestException {
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

    public void excluir(final Long id, final String justificativa) throws BadRequestException {
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
