package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Movimentacao;
import br.com.desenvolvimento.logica.estoque_service.service.MovimentacaoService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(origins = "*")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public List<MovimentacaoResponse> listar() {
        return movimentacaoService.listar();
    }

    @PostMapping
    public MovimentacaoResponse registrar(@RequestBody MovimentacaoRequest movimentacao) throws BadRequestException {
        return movimentacaoService.registrar(movimentacao);
    }

}
