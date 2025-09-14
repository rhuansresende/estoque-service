package br.com.desenvolvimento.logica.estoque_service.model;

public enum Situacao {

    ATIVO("ATIVO", "Ativo"),
    INATIVO("INATIVO", "Inativo"),
    ALTERADO("ALTERADO", "Alterado");

    private String codigo;
    private final String descricao;

    Situacao(String codigo, String descricao) {
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
