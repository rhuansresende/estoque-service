package br.com.desenvolvimento.logica.estoque_service.controller;

import br.com.desenvolvimento.logica.estoque_service.dto.MensagemResponse;
import br.com.desenvolvimento.logica.estoque_service.dto.TipoMensagemResponse;
import br.com.desenvolvimento.logica.estoque_service.model.TipoMensagem;
import br.com.desenvolvimento.logica.estoque_service.service.MensagemService;
import br.com.desenvolvimento.logica.estoque_service.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mensagens")
@CrossOrigin(origins = "*")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @GetMapping
    public Page<MensagemResponse> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "criadoEm") String sort,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String tipoMensagem,
            @RequestParam(required = false) @DateTimeFormat(pattern = DataUtil.PATTERN_DDMMYYYY) LocalDate dataCriacao
            ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        return mensagemService.listar(titulo, tipoMensagem, dataCriacao, pageable);
    }

    @GetMapping("/tipo-mensagem")
    public List<TipoMensagemResponse> listarTipoMensagens() {
        return Arrays
                .stream(TipoMensagem.values())
                .map(t -> new TipoMensagemResponse(t.name(), t.getDescricao()))
                .collect(Collectors.toList());
    }

    @PutMapping("/lerMensagem/{id}")
    public MensagemResponse lerMensagem(@PathVariable Long id) {
        return mensagemService.lerMensagem(id);
    }

    @DeleteMapping("/{id}")
    public void excluirMensagem(@PathVariable Long id) {
        mensagemService.excluirMensagem(id);
    }
}
