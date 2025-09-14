package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoRequest;
import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import br.com.desenvolvimento.logica.estoque_service.exception.ValidationException;
import br.com.desenvolvimento.logica.estoque_service.model.Produto;
import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import br.com.desenvolvimento.logica.estoque_service.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll()
                .stream()
                .filter(produto -> produto.getSituacao() == Situacao.ATIVO)
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .map(this::toResponse)
                .toList();
    }

    public Produto consultarPorId(Long id) throws BadRequestException {
        return produtoRepository
                .findById(id)
                .orElseThrow(() -> new ValidationException("Produto não encontrado."));
    }

    public List<ProdutoResponse> buscar(final String filtro) {
        if (filtro == null || filtro.isBlank()) {
            return listar();
        }
        return produtoRepository.findByNomeContainingIgnoreCaseOrCodigoContainingIgnoreCase(filtro, filtro)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProdutoResponse> consultarProdutosComEstoqueBaixo() {
        return this.listar()
                .stream()
                .filter(produto -> produto.getQuantidadeAtual() <= produto.getQuantidadeMinima())
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .toList();
    }

    public ProdutoResponse criar(@Valid ProdutoRequest produtoRequest) {
        var produtoExistente = produtoRepository.findProdutoByNomeAndSituacao(
                produtoRequest.getNome().trim().toUpperCase(),
                Situacao.ATIVO
        );
        if (produtoExistente != null) {
            return toResponse(produtoExistente);
        }

        Produto produto = toEntity(produtoRequest);
        produto.setSituacao(Situacao.ATIVO);
        produtoRepository.save(produto);
        return toResponse(produto);
    }

    public ProdutoResponse atualizar(ProdutoRequest produtoRequest) throws BadRequestException {
        if (produtoRequest.getId() == null) {
            throw new ValidationException("ID é obrigatório");
        }
        Produto produto = consultarPorId(produtoRequest.getId());
        produto.setNome(produtoRequest.getNome());
        produto.setQuantidadeAtual(produtoRequest.getQuantidadeAtual());
        produto.setQuantidadeMinima(produtoRequest.getQuantidadeMinima());
        produto.setPrecoCompra(produtoRequest.getPrecoCompra());
        produto.setPercentualLucro(produtoRequest.getPercentualLucro());
        produtoRepository.save(produto);
        return toResponse(produto);
    }

    public void atualizar(Produto produto) {
        produtoRepository.save(produto);
    }

    public void deletar(Long id) throws BadRequestException {
        Produto produto = consultarPorId(id);
        produto.setSituacao(Situacao.INATIVO);
        atualizar(produto);
    }

    private Produto toEntity(final ProdutoRequest produtoRequest) {
        Produto produto = new Produto();
        produto.setNome(produtoRequest.getNome().trim().toUpperCase());
        produto.setCodigo(produtoRequest.getCodigo());
        produto.setQuantidadeAtual(produtoRequest.getQuantidadeAtual());
        produto.setQuantidadeMinima(produtoRequest.getQuantidadeMinima());
        produto.setPrecoCompra(produtoRequest.getPrecoCompra());
        produto.setPercentualLucro(produtoRequest.getPercentualLucro());
        return produto;
    }

    public ProdutoResponse toResponse(final Produto produto) {
        ProdutoResponse produtoResponse = new ProdutoResponse();
        produtoResponse.setId(produto.getId());
        produtoResponse.setNome(produto.getNome().toUpperCase());
        produtoResponse.setCodigo(produto.getCodigo());
        produtoResponse.setQuantidadeAtual(produto.getQuantidadeAtual());
        produtoResponse.setQuantidadeMinima(produto.getQuantidadeMinima());
        produtoResponse.setPrecoCompra(produto.getPrecoCompra());
        produtoResponse.setPrecoVenda(produto.getPrecoVenda());
        produtoResponse.setPercentualLucro(produto.getPercentualLucro());
        produtoResponse.setSituacao(produto.getSituacao());
        return produtoResponse;
    }
}
