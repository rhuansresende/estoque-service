package br.com.desenvolvimento.logica.estoque_service.dto;

import br.com.desenvolvimento.logica.estoque_service.model.TipoMovimentacao;

import java.io.Serializable;
import java.math.BigDecimal;

public class MovimentacaoRequest implements Serializable {

    private Long produto;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private BigDecimal precoCompra;

    public MovimentacaoRequest() {
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
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
}
