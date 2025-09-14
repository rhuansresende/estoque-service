package br.com.desenvolvimento.logica.estoque_service.repository.impl;

import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.repository.custom.ProdutoRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Produto> filtrar(String nome,
                                 Situacao situacao,
                                 Pageable pageable) {

        List<Produto> result = result(nome, situacao, pageable);
        long total = count(nome, situacao);
        return new PageImpl<>(result, pageable, total);
    }

    private List<Produto> result(String nome, Situacao situacao, Pageable pageable) {
        String sql = select(false) +
                from() +
                where(nome, situacao) +
                "ORDER BY p.nome ASC";

        Query query = em.createNativeQuery(sql, Produto.class);
        setParameters(query, nome, situacao);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    private long count(String nome, Situacao situacao) {
        String sql = select(true) +
                from() +
                where(nome, situacao);

        Query countQuery = em.createNativeQuery(sql);
        setParameters(countQuery, nome, situacao);

        return ((Number) countQuery.getSingleResult()).longValue();
    }

    private String select(boolean isCount) {
        if (isCount) {
            return "SELECT COUNT(*) \n";
        } else {
            return "SELECT p.* \n";
        }
    }

    private String from() {
        return "FROM Produto p \n";
    }

    private String where(String nome, Situacao situacao) {

        StringBuilder sql = new StringBuilder("WHERE 1=1 \n");

        if (nome != null && !nome.isEmpty()) {
            sql.append("AND unaccent(LOWER(p.nome)) LIKE unaccent(LOWER(:nome)) \n");
        }
        if (situacao != null) {
            sql.append("AND p.situacao = :situacao \n");
        }
        return sql.toString();
    }

    private void setParameters(Query query, String nome, Situacao situacao) {
        if (nome != null && !nome.isEmpty()) {
            query.setParameter("nome", "%" + nome + "%");
        }
        if (situacao != null) {
            query.setParameter("situacao", situacao.name());
        }
    }
}
