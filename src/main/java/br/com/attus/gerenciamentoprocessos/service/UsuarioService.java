package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
public interface UsuarioService {

    Usuario salvar(Usuario usuario);

    Usuario buscarPorId(Long id);

    void excluir(Long id);

    Optional<Usuario> buscarPorEmail(String email);

}
