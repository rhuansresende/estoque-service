package br.com.desenvolvimento.logica.estoque_service.repository.custom;

import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProdutoRepositoryCustom {

    Page<Produto> filtrar(String nome,
                          Situacao situacao,
                          Pageable pageable);

    List<Produto> consultarProdutoPorNomeOuCodigoEAtivo(
            String nome,
            String codigo,
            Situacao situacao);
}
