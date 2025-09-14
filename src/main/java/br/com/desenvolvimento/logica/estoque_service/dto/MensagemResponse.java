package br.com.desenvolvimento.logica.estoque_service.dto;

import br.com.desenvolvimento.logica.estoque_service.model.TipoMensagem;

import java.io.Serializable;

public class MensagemResponse implements Serializable {

    private Long id;
    private String titulo;
    private String mensagem;
    private TipoMensagem tipoMensagem;
    private String dataCriacao;

    public MensagemResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
