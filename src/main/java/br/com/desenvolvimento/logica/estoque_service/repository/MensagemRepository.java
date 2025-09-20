package br.com.desenvolvimento.logica.estoque_service.repository;

import br.com.desenvolvimento.logica.estoque_service.model.Mensagem;
import br.com.desenvolvimento.logica.estoque_service.repository.custom.MensagemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MensagemRepository extends JpaRepository<Mensagem, Long>, MensagemRepositoryCustom {

    Mensagem findByMensagemIgnoreCaseAndDataCriacaoBetween(String mensagem, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
