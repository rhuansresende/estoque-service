package br.com.desenvolvimento.logica.estoque_service.repository.impl;

import br.com.desenvolvimento.logica.estoque_service.model.Mensagem;
import br.com.desenvolvimento.logica.estoque_service.model.StatusMensagem;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMensagem;
import br.com.desenvolvimento.logica.estoque_service.repository.custom.MensagemRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class MensagemRepositoryImpl implements MensagemRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Mensagem> filtrar(StatusMensagem status,
                                  String titulo,
                                  TipoMensagem tipoMensagem,
                                  LocalDateTime inicio,
                                  LocalDateTime fim,
                                  Pageable pageable) {

        List<Mensagem> result = result(status, titulo, tipoMensagem, inicio, fim, pageable);
        long total = count(status, titulo, tipoMensagem, inicio, fim);
        return new PageImpl<>(result, pageable, total);
    }

    private List<Mensagem> result(StatusMensagem status,
                                  String titulo,
                                  TipoMensagem tipoMensagem,
                                  LocalDateTime inicio,
                                  LocalDateTime fim,
                                  Pageable pageable) {

        String sql = select(false) +
                from() +
                where(status, titulo, tipoMensagem, inicio, fim) +
                "ORDER BY m.criado_em DESC, m.titulo ASC";

        Query query = em.createNativeQuery(sql, Mensagem.class);
        setParameters(query, status, titulo, tipoMensagem, inicio, fim);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    private long count(StatusMensagem status,
                       String titulo,
                       TipoMensagem tipoMensagem,
                       LocalDateTime inicio,
                       LocalDateTime fim) {

        String sql = select(true) +
                from() +
                where(status, titulo, tipoMensagem, inicio, fim);

        Query countQuery = em.createNativeQuery(sql);
        setParameters(countQuery, status, titulo, tipoMensagem, inicio, fim);

        return ((Number) countQuery.getSingleResult()).longValue();
    }

    private String select(boolean isCount) {
        if (isCount) {
            return "SELECT COUNT(*) \n";
        } else {
            return "SELECT m.* \n"; // nativo precisa de colunas
        }
    }

    private String from() {
        return "FROM mensagem m \n";
    }

    private String where(StatusMensagem status,
                         String titulo,
                         TipoMensagem tipoMensagem,
                         LocalDateTime inicio,
                         LocalDateTime fim) {

        StringBuilder sql = new StringBuilder("WHERE 1=1 \n");

        if (status != null) {
            sql.append(" AND m.status = :status \n");
        }
        if (titulo != null && !titulo.isBlank()) {
            sql.append(" AND unaccent(LOWER(m.titulo)) LIKE unaccent(LOWER(:titulo)) \n");
        }
        if (tipoMensagem != null) {
            sql.append(" AND m.tipo_mensagem = :tipoMensagem \n");
        }
        if (inicio != null && fim != null) {
            sql.append(" AND m.criado_em BETWEEN :inicio AND :fim \n");
        }
        return sql.toString();
    }

    private void setParameters(Query query,
                               StatusMensagem status,
                               String titulo,
                               TipoMensagem tipoMensagem,
                               LocalDateTime inicio,
                               LocalDateTime fim) {

        if (status != null) query.setParameter("status", status.name());
        if (titulo != null && !titulo.isBlank()) query.setParameter("titulo", "%" + titulo + "%");
        if (tipoMensagem != null) query.setParameter("tipoMensagem", tipoMensagem.name());
        if (inicio != null && fim != null) {
            query.setParameter("inicio", inicio);
            query.setParameter("fim", fim);
        }
    }
}

