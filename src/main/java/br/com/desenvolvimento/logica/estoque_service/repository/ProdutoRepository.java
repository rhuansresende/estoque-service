package br.com.desenvolvimento.logica.estoque_service.repository;

import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.repository.custom.ProdutoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long>, ProdutoRepositoryCustom {

    Produto findProdutoByNomeAndSituacao(String nome, Situacao situacao);


}
