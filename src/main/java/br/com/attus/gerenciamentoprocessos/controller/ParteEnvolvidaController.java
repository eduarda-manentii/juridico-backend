package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.mapper.ParteEnvolvidaMapper;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        ParteEnvolvida salvo = service.salvar(parteEnvolvidaMapper.toEntity(parteEnvolvidaDto));
        ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(salvo);
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(salvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParteEnvolvidaDto> buscarPorId(@PathVariable("id") Long id) {
        ParteEnvolvida parteEnvolvida = service.buscarPorId(id);
        ParteEnvolvidaDto dto = parteEnvolvidaMapper.toDto(parteEnvolvida);
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).alterar(dto)).withRel("update"));
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).excluir(id)).withRel("delete"));
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).inserir(dto)).withRel("create"));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
