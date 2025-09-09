package br.com.desenvolvimento.logica.estoque_service.repository;

import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findProdutoByNomeAndSituacao(String nome, Situacao situacao);

    List<Produto> findByNomeContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nome, String codigo);
}
