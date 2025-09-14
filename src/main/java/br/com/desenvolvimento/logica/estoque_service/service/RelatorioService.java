package br.com.desenvolvimento.logica.estoque_service.service;

import br.com.desenvolvimento.logica.estoque_service.dto.ProdutoResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class RelatorioService {

    @Autowired
    private ProdutoService produtoService;

    public Page<ProdutoResponse> sugestoesCompras(Pageable pageable) {

        List<ProdutoResponse> filtrados = produtoService.listar()
                .stream()
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .filter(p -> p.getQuantidadeAtual() <= p.getQuantidadeMinima())
                .toList();

        int total = filtrados.size();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<ProdutoResponse> pagina;
        if (start > end) {
            pagina = List.of();
        } else {
            pagina = filtrados.subList(start, end);
        }

        return new PageImpl<>(pagina, pageable, total);
    }

    private List<ProdutoResponse> sugestoesCompras() {
        return produtoService.listar()
                .stream()
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .filter(p -> p.getQuantidadeAtual() <= p.getQuantidadeMinima())
                .toList();
    }

    public byte[] sugestoesComprasPDF() {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        //Titulo
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("Relatório de Compras", font);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(Chunk.NEWLINE);

        //Tabela
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 2, 2});

        //Cabeçalho
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        table.addCell(new Phrase("Produto", headerFont));
        table.addCell(new Phrase("Quantidade Atual", headerFont));
        table.addCell(new Phrase("Quantidade Mínima", headerFont));

        //Linhas
        List<ProdutoResponse> produtos = sugestoesCompras();
        for (ProdutoResponse produto : produtos) {
            table.addCell(produto.getNome());
            table.addCell(produto.getQuantidadeAtual().toString());
            table.addCell(produto.getQuantidadeMinima().toString());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public Page<ProdutoResponse> sugestoesPrecos(Pageable pageable) {
        List<ProdutoResponse> filtrados = produtoService.listar()
                .stream()
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .filter(p -> p.getPrecoVenda() != null)
                .toList();

        int total = filtrados.size();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<ProdutoResponse> pagina;
        if (start > end) {
            pagina = List.of();
        } else {
            pagina = filtrados.subList(start, end);
        }

        return new PageImpl<>(pagina, pageable, total);
    }

    private List<ProdutoResponse> sugestoesPrecos() {
        return produtoService.listar()
                .stream()
                .sorted((p1, p2) -> p1.getNome().compareTo(p2.getNome()))
                .filter(p -> p.getPrecoVenda() != null)
                .toList();
    }

    public byte[] sugestoesPrecosPDF() {

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        //Titulo
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("Relatório de Preços", font);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(Chunk.NEWLINE);

        //Tabela
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 2, 2, 2});

        //Cabeçalho
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        table.addCell(new Phrase("Produto", headerFont));
        table.addCell(new Phrase("Preço de Compra", headerFont));
        table.addCell(new Phrase("Preço de Venda", headerFont));
        table.addCell(new Phrase("Percentual de Lucro", headerFont));

        //Linhas
        List<ProdutoResponse> produtos = sugestoesPrecos();
        for (ProdutoResponse produto : produtos) {
            table.addCell(produto.getNome());
            table.addCell(String.format("R$ %.2f", produto.getPrecoCompra()));
            table.addCell(String.format("R$ %.2f", produto.getPrecoVenda()));
            table.addCell(produto.getPercentualLucro().stripTrailingZeros().toPlainString() + "%");
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
