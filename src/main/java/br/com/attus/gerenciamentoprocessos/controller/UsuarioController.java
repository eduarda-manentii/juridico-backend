package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.*;
import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.UsuarioMapper;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.security.TokenService;
import br.com.attus.gerenciamentoprocessos.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper mapper;

    public UsuarioController(UsuarioService service,
                             PasswordEncoder passwordEncoder,
                             TokenService tokenService,
                             UsuarioMapper mapper
    ) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.mapper = mapper;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto body){
        Optional<Usuario> usuario = service.buscarPorEmail(body.getEmail());
        if(usuario.isPresent() && passwordEncoder.matches(body.getSenha(), usuario.get().getSenha())) {
                String token = this.tokenService.generateToken(usuario.get());
                return ResponseEntity.ok(new ResponseDto(usuario.get().getId(), usuario.get().getNome(), token));
            }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping
    public ResponseEntity<ResponseDto> inserir(@RequestBody UsuarioDto dto){
        Optional<Usuario> usuario = service.buscarPorEmail(dto.getEmail());
        if(usuario.isEmpty()) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
            novoUsuario.setEmail(dto.getEmail());
            novoUsuario.setNome(dto.getNome());
            this.service.salvar(novoUsuario);
            String token = this.tokenService.generateToken(novoUsuario);
            return ResponseEntity.ok(new ResponseDto(novoUsuario.getId(), novoUsuario.getNome(), token));
        } else {
            throw new EntidadeEmUsoException("Usuário já existente");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        UsuarioDto dto = mapper.toDto(usuario);
        dto.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        dto.add(linkTo(methodOn(UsuarioController.class).alterar(dto)).withRel("update"));
        dto.add(linkTo(methodOn(UsuarioController.class).excluir(id)).withRel("delete"));
        dto.add(linkTo(methodOn(UsuarioController.class).inserir(dto)).withRel("create"));
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<UsuarioDto> alterar(@Valid @RequestBody UsuarioDto usuarioDto) {
        if (usuarioDto.getId() == null) {
            throw new ObrigatoriedadeIdException();
        }
        Usuario usuarioSalvo = service.salvar(mapper.toEntity(usuarioDto));
        UsuarioDto dto = mapper.toDto(usuarioSalvo);
        dto.add(linkTo(methodOn(ParteEnvolvidaController.class).buscarPorId(usuarioSalvo.getId())).withSelfRel());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
