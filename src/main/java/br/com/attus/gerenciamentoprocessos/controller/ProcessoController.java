package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ProcessoMapper;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/processos")
public class ProcessoController {

    private final ProcessoService service;
    private final ProcessoMapper mapper;

    public ProcessoController(ProcessoService service, ProcessoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ProcessoDto> inserir(@Valid @RequestBody ProcessoDto processoDto) {
        Processo salvo = service.salvar(mapper.toEntity(processoDto));
        ProcessoDto dto = mapper.toDto(salvo);
        dto.add(linkTo(methodOn(ProcessoController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity
                .created(linkTo(methodOn(ProcessoController.class).buscarPorId(salvo.getId())).toUri())
                .body(dto);
    }

    @PutMapping
    public ResponseEntity<ProcessoDto> alterar(@Valid @RequestBody ProcessoDto processoDto) {
        if (processoDto.getId() == null) {
            throw new ObrigatoriedadeIdException();
        }
        Processo salvo = service.salvar(mapper.toEntity(processoDto));
        ProcessoDto dto = mapper.toDto(salvo);
        dto.add(linkTo(methodOn(ProcessoController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDto> buscarPorId(@PathVariable Long id) {
        Processo processo = service.buscarPorId(id);
        ProcessoDto dto = mapper.toDto(processo);
        dto.add(linkTo(methodOn(ProcessoController.class).buscarPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(ProcessoController.class).alterar(dto)).withRel("update"));
        dto.add(linkTo(methodOn(ProcessoController.class).excluir(id)).withRel("delete"));
        dto.add(linkTo(methodOn(ProcessoController.class).inserir(dto)).withRel("create"));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/arquivar")
    public ResponseEntity<Void> arquivar(@PathVariable Long id) {
        service.arquivarProcesso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/query")
    public ResponseEntity<Page<ProcessoDto>> buscarPorFiltros(
            @RequestParam(required = false) StatusProcesso status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAbertura,
            @RequestParam(required = false) String documento,
            @RequestParam(defaultValue = "0") int pagina
    ) {
        Pageable paginacao = PageRequest.of(pagina, 15);
        Page<Processo> processos = service.buscarPorFiltros(status, dataAbertura, documento, paginacao);
        Page<ProcessoDto> dtos = processos.map(processo -> {
            ProcessoDto dto = mapper.toDto(processo);
            dto.add(linkTo(methodOn(ProcessoController.class).buscarPorId(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(ProcessoController.class).alterar(dto)).withRel("update"));
            dto.add(linkTo(methodOn(ProcessoController.class).excluir(dto.getId())).withRel("delete"));
            dto.add(linkTo(methodOn(ProcessoController.class).inserir(dto)).withRel("create"));
            return dto;
        });
        return ResponseEntity.ok(dtos);
    }

}
