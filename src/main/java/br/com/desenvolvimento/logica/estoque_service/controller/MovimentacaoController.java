package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.MovimentacaoResponse;
import br.com.desenvolvimento.logica.estoque_service.service.MovimentacaoService;
import br.com.desenvolvimento.logica.estoque_service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(origins = "*")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping
    public Page<MovimentacaoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sort,
            @RequestParam(required = false) Long produto,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) @DateTimeFormat(pattern = DataUtil.PATTERN_DDMMYYYY) LocalDate data
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return movimentacaoService.listar(produto, tipo, data, pageable);
    }

    @PostMapping
    public MovimentacaoResponse registrar(@RequestBody MovimentacaoRequest movimentacao) {
        return movimentacaoService.registrar(movimentacao);
    }

    @PutMapping
    public MovimentacaoResponse atualizar(@RequestBody MovimentacaoRequest movimentacao) {
        return movimentacaoService.atualizar(movimentacao);
    }

    @DeleteMapping("/{id}/{justificativa}")
    public void excluir(@PathVariable(name = "id") Long id,
                        @PathVariable(name = "justificativa") final String justificativa) {
        movimentacaoService.excluir(id, justificativa);
    }

}
