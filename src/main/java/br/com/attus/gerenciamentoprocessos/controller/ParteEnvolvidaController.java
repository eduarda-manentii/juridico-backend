package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ParteEnvolvidaMapper;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/partes-envolvidas")
public class ParteEnvolvidaController {

    private final ParteEnvolvidaService service;
    private final ParteEnvolvidaMapper parteEnvolvidaMapper;

    public ParteEnvolvidaController(ParteEnvolvidaService service, ParteEnvolvidaMapper parteEnvolvidaMapper) {
        this.service = service;
        this.parteEnvolvidaMapper = parteEnvolvidaMapper;
    }

    @PostMapping
    public ResponseEntity<ParteEnvolvidaDto> inserir(@Valid @RequestBody ParteEnvolvidaDto parteEnvolvidaDto) {
        ParteEnvolvida salvo = service.salvar(parteEnvolvidaMapper.toEntity(parteEnvolvidaDto));
        ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(salvo);
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity
                .created(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(salvo.getId())).toUri())
                .body(dto);
    }

    @PutMapping
    public ResponseEntity<ParteEnvolvidaDto> alterar(@Valid @RequestBody ParteEnvolvidaDto parteEnvolvidaDto) {
        if (parteEnvolvidaDto.getId() == null) {
            throw new ObrigatoriedadeIdException();
        }
        ParteEnvolvida salvo = service.salvar(parteEnvolvidaMapper.toEntity(parteEnvolvidaDto));
        ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(salvo);
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParteEnvolvidaDto> buscarPorId(@PathVariable("id") Long id) {
        try {
            ParteEnvolvida parteEnvolvida = service.buscarPorId(id);
            ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(parteEnvolvida);
            dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(id)).withSelfRel());
            dto.add(linkTo(methodOn(ParteEnvolvidaController.class).alterar(dto)).withRel("update"));
            dto.add(linkTo(methodOn(ParteEnvolvidaController.class).excluir(id)).withRel("delete"));
            dto.add(linkTo(methodOn(ParteEnvolvidaController.class).inserir(dto)).withRel("create"));
            return ResponseEntity.ok(dto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ParteEnvolvidaDto>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ParteEnvolvida> pageEntidades = service.listarTodos(pageable);

        var dtos = pageEntidades.stream()
                .map(entidade -> {
                    ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(entidade);
                    dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(entidade.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(ParteEnvolvidaController.class).alterar(dto)).withRel("update"));
                    dto.add(linkTo(methodOn(ParteEnvolvidaController.class).excluir(entidade.getId())).withRel("delete"));
                    dto.add(linkTo(methodOn(ParteEnvolvidaController.class).inserir(dto)).withRel("create"));
                    return dto;
                })
                .toList();

        Page<ParteEnvolvidaDto> pageDto = new PageImpl<>(dtos, pageable, pageEntidades.getTotalElements());
        return ResponseEntity.ok(pageDto);
    }

}
