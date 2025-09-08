package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoService produtoService;

    public List<ProdutoResponse> sugestoesCompras() {
        return produtoService.listar()
                .stream()
                .filter(p -> p.getQuantidadeAtual() <= p.getQuantidadeMinima())
                .toList();
    }

    public List<ProdutoResponse> sugestoesPrecos() {
        return produtoService.listar();
    }
}
