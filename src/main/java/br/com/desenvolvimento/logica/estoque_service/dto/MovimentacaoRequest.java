package br.com.desenvolvimento.logica.estoque_service.dto;

import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovimentacaoRequest implements Serializable {

    private Long id;
    private ProdutoRequest produto;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private BigDecimal precoCompra;
    private String justificativa;

    public MovimentacaoRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoRequest getProduto() {
        return produto;
    }

    public void setProduto(ProdutoRequest produto) {
        this.produto = produto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
