package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.dto.SituacaoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public Page<ProdutoResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String situacao
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return produtoService.listar(nome, situacao, pageable);
    }

    @GetMapping("/buscar")
    public List<ProdutoResponse> buscar(@RequestParam(name = "q", required = false) final String filtro) {
        return produtoService.buscar(filtro);
    }

    @GetMapping("/situacao-produto")
    public List<SituacaoResponse> listarSituacoesProduto() {
        return Arrays
                .stream(Situacao.values())
                .filter(s -> s != Situacao.ALTERADO)
                .map(s -> new SituacaoResponse(s.getCodigo(), s.getDescricao()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public ProdutoResponse criar(@RequestBody ProdutoRequest produto) {
        return produtoService.criar(produto);
    }

    @PutMapping
    public ProdutoResponse atualizar(@RequestBody ProdutoRequest produto) {
        return produtoService.atualizar(produto);
    }

    @PutMapping("/ativar-produto/{id}")
    public void ativar(@PathVariable Long id) {
        produtoService.ativar(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}
