package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.mapper.AndamentoProcessualMapper;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        AndamentoProcessual salvo = service.salvar(andamentoProcessualMapper.toEntity(andamentoProcessualDto));
        AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(salvo);
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AndamentoProcessualDto> buscarPorId(@PathVariable("id") Long id) {
        AndamentoProcessual andamentoProcessual = service.buscarPorId(id);
        AndamentoProcessualDto dto = andamentoProcessualMapper.toDto(andamentoProcessual);
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).buscarPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).alterar(dto)).withRel("update"));
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).excluir(id)).withRel("delete"));
        dto.add(linkTo(methodOn(AndamentoProcessualController.class).inserir(dto)).withRel("create"));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
