package br.com.desenvolvimento.logica.estoque_service.job;

import br.com.desenvolvimento.logica.estoque_service.service.MensagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MensagemJob {

    @Autowired
    private MensagemService mensagemService;

    @Scheduled(cron = "0 0 10,15,21 * * ?")
//    @Scheduled(fixedRate = 5000)
    public void executar() {
        mensagemService.verificarProdutos();
    }
}
