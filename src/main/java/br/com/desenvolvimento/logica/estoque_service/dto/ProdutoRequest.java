package br.com.desenvolvimento.logica.estoque_service.dto;

import br.com.desenvolvimento.logica.estoque_service.model.Situacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProdutoRequest implements Serializable {

    private Long id;

    @NotBlank(message = "Nome do produto é obrigatório")
    private String nome;
    private String codigo;
    @NotNull(message = "Quantidade atual é obrigatória")
    @Min(value = 1, message = "Quantidade atual deve ser maior que zero")
    private Integer quantidadeAtual;
    @NotNull(message = "Quantidade mínima é obrigatória")
    @Min(value = 1, message = "Quantidade mínima deve ser maior que zero")
    private Integer quantidadeMinima;
    private BigDecimal precoCompra;
    private BigDecimal percentualLucro;

    @NotNull(message = "Situação é obrigatória")
    private Situacao situacao;

    public ProdutoRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    public BigDecimal getPrecoCompra() {
        return precoCompra;
    }

    public void setPrecoCompra(BigDecimal precoCompra) {
        this.precoCompra = precoCompra;
    }

    public BigDecimal getPercentualLucro() {
        return percentualLucro;
    }

    public void setPercentualLucro(BigDecimal percentualLucro) {
        this.percentualLucro = percentualLucro;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
}
