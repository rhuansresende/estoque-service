package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;
import br.com.desenvolvimento.logica.estoque_service.repository.MovimentacaoRepository;
import br.com.desenvolvimento.logica.estoque_service.util.DataUtil;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ProdutoService produtoService;

    public List<MovimentacaoResponse> listar() {
        return movimentacaoRepository.findAll()
                .stream()
                .sorted((m1, m2) -> m1.getData().compareTo(m2.getData()))
                .map(this::toResponse)
                .toList();
    }

    public MovimentacaoResponse registrar(MovimentacaoRequest movimentacaoRequest) throws BadRequestException {

        var produto = produtoService.consultarPorId(movimentacaoRequest.getProduto());

        if (movimentacaoRequest.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() + movimentacaoRequest.getQuantidade());
            produto.setPrecoCompra(movimentacaoRequest.getPrecoCompra());
        } else {
            if (produto.getQuantidadeAtual() < movimentacaoRequest.getQuantidade()) {
                throw new BadRequestException("Quantidade em estoque inferior a quantidade de saída.");
            }
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - movimentacaoRequest.getQuantidade());
        }

        produtoService.atualizar(produto);
        Movimentacao movimentacao = movimentacaoRepository.save(toEntity(movimentacaoRequest, produto));
        return toResponse(movimentacao);
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
        movimentacaoResponse.setNomeProduto(movimentacao.getProduto().getNome());
        movimentacaoResponse.setTipo(movimentacao.getTipo().name());
        movimentacaoResponse.setQuantidade(movimentacao.getQuantidade());
        movimentacaoResponse.setDataMovimentacao(DataUtil.LocalDateTimeToString(
                movimentacao.getData(),
                DataUtil.PATTERN_DDMMYYYYHHMMSS)
        );
        return movimentacaoResponse;
    }
}
