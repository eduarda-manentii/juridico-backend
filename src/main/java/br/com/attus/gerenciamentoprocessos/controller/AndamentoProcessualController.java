package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.AndamentoProcessualMapper;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/andamentos-processuais")
public class AndamentoProcessualController {

    private final AndamentoProcessualService service;
    private final AndamentoProcessualMapper andamentoProcessualMapper;

    public AndamentoProcessualController(AndamentoProcessualService service, AndamentoProcessualMapper andamentoProcessualMapper) {
        this.service = service;
        this.andamentoProcessualMapper = andamentoProcessualMapper;
    }

    @PostMapping
    public ResponseEntity<AndamentoProcessualDto> inserir(@Valid @RequestBody AndamentoProcessualDto andamentoProcessualDto) {
        AndamentoProcessual salvo = service.salvar(andamentoProcessualMapper.toEntity(andamentoProcessualDto));
        AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(salvo);
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity
                .created(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(salvo.getId())).toUri())
                .body(dto);
    }

    @PutMapping
    public ResponseEntity<AndamentoProcessualDto> alterar(@Valid @RequestBody AndamentoProcessualDto andamentoProcessualDto) {
        if (andamentoProcessualDto.getId() == null) {
            throw new ObrigatoriedadeIdException();
        }
        AndamentoProcessual salvo = service.salvar(andamentoProcessualMapper.toEntity(andamentoProcessualDto));
        AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(salvo);
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AndamentoProcessualDto> buscarPorId(@PathVariable("id") Long id) {
        try {
            AndamentoProcessual andamentoProcessual = service.buscarPorId(id);
            AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(andamentoProcessual);
            dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(id)).withSelfRel());
            dto.add(linkTo(methodOn(AndamentoProcessualController.class).alterar(dto)).withRel("update"));
            dto.add(linkTo(methodOn(AndamentoProcessualController.class).excluir(id)).withRel("delete"));
            dto.add(linkTo(methodOn(AndamentoProcessualController.class).inserir(dto)).withRel("create"));
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
    public ResponseEntity<Page<AndamentoProcessualDto>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AndamentoProcessual> pageEntidades = service.listarTodos(pageable);

        var dtos = pageEntidades.stream()
                .map(entidade -> {
                    AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(entidade);
                    dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(entidade.getId())).withSelfRel());
                    dto.add(linkTo(methodOn(AndamentoProcessualController.class).alterar(dto)).withRel("update"));
                    dto.add(linkTo(methodOn(AndamentoProcessualController.class).excluir(entidade.getId())).withRel("delete"));
                    dto.add(linkTo(methodOn(AndamentoProcessualController.class).inserir(dto)).withRel("create"));
                    return dto;
                })
                .toList();

        Page<AndamentoProcessualDto> pageDto = new PageImpl<>(dtos, pageable, pageEntidades.getTotalElements());
        return ResponseEntity.ok(pageDto);
    }

}
