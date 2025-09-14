package br.com.desenvolvimento.logica.estoque_service.dto;

import java.io.Serializable;

public class TipoMensagemResponse implements Serializable {

    String codigo;
    String descricao;

    public TipoMensagemResponse() {
    }

    public TipoMensagemResponse(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
