package br.com.desenvolvimento.logica.estoque_service.repository.impl;

import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;
import br.com.desenvolvimento.logica.estoque_service.repository.custom.MovimentacaoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MovimentacaoRepositoryImpl implements MovimentacaoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Movimentacao> filtrar(
            Produto produto,
            TipoMovimentacao tipoMovimentacao,
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable) {

        List<Movimentacao> result = result(produto, tipoMovimentacao, inicio, fim, pageable);
        long total = count(produto, tipoMovimentacao, inicio, fim);
        return new PageImpl<>(result, pageable, total);
    }

    private List<Movimentacao> result(Produto produto,
                                      TipoMovimentacao tipoMovimentacao,
                                      LocalDateTime inicio,
                                      LocalDateTime fim,
                                      Pageable pageable) {
        String sql = select(false) +
                from(produto) +
                where(produto, tipoMovimentacao, inicio, fim) +
                "ORDER BY m.data_criacao DESC";

        Query query = em.createNativeQuery(sql, Movimentacao.class);
        setParameters(query, produto, tipoMovimentacao, inicio, fim);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    private long count(Produto produto,
                       TipoMovimentacao tipoMovimentacao,
                       LocalDateTime inicio,
                       LocalDateTime fim) {

        String sql = select(true) +
                from(produto) +
                where(produto, tipoMovimentacao, inicio, fim);

        Query countQuery = em.createNativeQuery(sql);
        setParameters(countQuery, produto, tipoMovimentacao, inicio, fim);

        return ((Number) countQuery.getSingleResult()).longValue();
    }

    private String select(boolean isCount) {
        if (isCount) {
            return "SELECT COUNT(*) \n";
        } else {
            return "SELECT m.* \n";
        }
    }

    private String from(Produto produto) {
        StringBuilder sql = new StringBuilder("FROM Movimentacao AS m \n");
        if (produto != null) {
            sql.append(" INNER JOIN Produto AS p \n");
            sql.append(" ON m.produto_id = p.id \n");
        }
        return  sql.toString();
    }

    private String where(Produto produto,
                         TipoMovimentacao tipoMovimentacao,
                         LocalDateTime inicio,
                         LocalDateTime fim) {
        StringBuilder sql = new StringBuilder("WHERE 1=1");

        sql.append(" AND m.situacao <> :situacao \n");

        if (produto != null) {
            sql.append(" AND p.id = :produto \n");
        }
        if (tipoMovimentacao != null) {
            sql.append(" AND m.tipo = :tipoMovimentacao \n");
        }
        if (inicio != null && fim != null) {
            sql.append(" AND m.data_criacao BETWEEN :inicio AND :fim \n");
        }

        return sql.toString();
    }

    private void setParameters(Query query,
                               Produto produto,
                               TipoMovimentacao tipoMovimentacao,
                               LocalDateTime inicio,
                               LocalDateTime fim) {

        query.setParameter("situacao", Situacao.INATIVO.name());

        if (produto != null) {
            query.setParameter("produto", produto.getId());
        }
        if (tipoMovimentacao != null) {
            query.setParameter("tipoMovimentacao", tipoMovimentacao.name());
        }
        if (inicio != null && fim != null) {
            query.setParameter("inicio", inicio);
            query.setParameter("fim", fim);
        }
    }
}
