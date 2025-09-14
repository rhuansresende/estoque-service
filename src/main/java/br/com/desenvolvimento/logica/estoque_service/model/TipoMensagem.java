package br.com.desenvolvimento.logica.estoque_service.model;

public enum TipoMensagem {
    CRITICA("CRITICA", "Crítica"),
    ALERTA("ALERTA", "Alerta"),
    INFORMACAO("INFORMACAO", "Informação");

    private final String codigo;
    private final String descricao;

    TipoMensagem(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
