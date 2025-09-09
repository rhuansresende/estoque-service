package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/sugestoes-compras")
    public List<ProdutoResponse> sugestoesCompras() {
        return relatorioService.sugestoesCompras();
    }

    @GetMapping("/sugestoes-compras/pdf")
    public byte[] sugestoesComprasPDF() {
        return relatorioService.sugestoesComprasPDF();
    }

    @GetMapping("/sugestoes-precos")
    public List<ProdutoResponse> sugestoesPrecos() {
        return relatorioService.sugestoesPrecos();
    }

    @GetMapping("/sugestoes-precos/pdf")
    public byte[] sugestoesPrecosPDF() {
        return relatorioService.sugestoesPrecosPDF();
    }
}
