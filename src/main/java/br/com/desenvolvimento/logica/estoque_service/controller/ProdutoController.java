package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.service.ProdutoService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoService.listar();
    }

    @GetMapping("/buscar")
    public List<ProdutoResponse> buscar(@RequestParam(name = "q", required = false) final String filtro) {
        return produtoService.buscar(filtro);
    }

    @PostMapping
    public ProdutoResponse criar(@RequestBody ProdutoRequest produto) {
        return produtoService.criar(produto);
    }

    @PutMapping
    public ProdutoResponse atualizar(@RequestBody ProdutoRequest produto) throws BadRequestException {
        return produtoService.atualizar(produto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) throws BadRequestException {
        produtoService.deletar(id);
    }
}
