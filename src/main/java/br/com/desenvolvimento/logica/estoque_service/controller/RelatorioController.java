package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/sugestoes-compras")
    public Page<ProdutoResponse> sugestoesCompras(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "criadoEm") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return relatorioService.sugestoesCompras(pageable);
    }

    @GetMapping("/sugestoes-compras/pdf")
    public byte[] sugestoesComprasPDF() {
        return relatorioService.sugestoesComprasPDF();
    }

    @GetMapping("/sugestoes-precos")
    public Page<ProdutoResponse> sugestoesPrecos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "criadoEm") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return relatorioService.sugestoesPrecos(pageable);
    }

    @GetMapping("/sugestoes-precos/pdf")
    public byte[] sugestoesPrecosPDF() {
        return relatorioService.sugestoesPrecosPDF();
    }
}
