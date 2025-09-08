package br.com.desenvolvimento.logica.estoque_service.repository;

import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
