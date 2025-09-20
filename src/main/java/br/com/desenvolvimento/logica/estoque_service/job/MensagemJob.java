package br.com.desenvolvimento.logica.estoque_service.job;

import br.com.desenvolvimento.logica.estoque_service.service.MensagemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MensagemJob {

    private static final Logger logger = LogManager.getLogger(MensagemJob.class);

    @Autowired
    private MensagemService mensagemService;

    @Scheduled(fixedDelayString = "${estoque-service.intervalo}")
    public void executar() {
        logger.info("Executando MensagemJob");
        mensagemService.verificarProdutos();
        logger.info("Finalizando MensagemJob");
    }
}
