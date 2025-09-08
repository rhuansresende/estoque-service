package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;
import br.com.desenvolvimento.logica.estoque_service.repository.MovimentacaoRepository;
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

    public List<Movimentacao> listar() {
        return movimentacaoRepository.findAll();
    }

    public Movimentacao registrar(Movimentacao movimentacao) throws BadRequestException {

        var produto = produtoService.consultarPorId(movimentacao.getProduto().getId());

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() + movimentacao.getQuantidade());
        } else {
            produto.setQuantidadeAtual(produto.getQuantidadeAtual() - movimentacao.getQuantidade());
        }

        produtoService.atualizar(produto);
        return movimentacaoRepository.save(movimentacao);
    }
}
