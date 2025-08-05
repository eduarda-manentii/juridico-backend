package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.mapper.ParteEnvolvidaMapper;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/partes_envolvidas")
public class ParteEnvolvidaController {

    private final ParteEnvolvidaService service;
    private final ParteEnvolvidaMapper parteEnvolvidaMapper;

    public ParteEnvolvidaController(ParteEnvolvidaService service, ParteEnvolvidaMapper parteEnvolvidaMapper) {
        this.service = service;
        this.parteEnvolvidaMapper = parteEnvolvidaMapper;
    }

    @PostMapping
    public ResponseEntity<ParteEnvolvida> inserir(@RequestBody ParteEnvolvidaDto parteEnvolvidaDto) {
        ParteEnvolvida parteEnvolvidaSalva = service.salvar(parteEnvolvidaMapper.toEntity(parteEnvolvidaDto));
        return ResponseEntity.created(URI.create("/partes_envolvidas/id" + parteEnvolvidaSalva.getId())).build();
    }

    @PutMapping
    public ResponseEntity<ParteEnvolvidaDto> alterar(@RequestBody ParteEnvolvidaDto parteEnvolvidaDto) {
        ParteEnvolvida parteEnvolvidaSalva = service.salvar(parteEnvolvidaMapper.toEntity(parteEnvolvidaDto));
        return ResponseEntity.ok(parteEnvolvidaMapper.toDto(parteEnvolvidaSalva));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParteEnvolvidaDto> buscarPorId(@PathVariable("id") Long id) {
        ParteEnvolvida parteEnvolvidaEncontrada = service.buscarPorId(id);
        return ResponseEntity.ok(parteEnvolvidaMapper.toDto(parteEnvolvidaEncontrada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ParteEnvolvidaDto> excluir(@PathVariable("id") Long id) {
        ParteEnvolvida parteEnvolvidaEncontrada = service.excluir(id);
        return ResponseEntity.ok(parteEnvolvidaMapper.toDto(parteEnvolvidaEncontrada));
    }

}
