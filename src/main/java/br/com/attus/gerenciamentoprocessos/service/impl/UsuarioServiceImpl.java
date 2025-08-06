package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.repository.UsuariosRepository;
import br.com.attus.gerenciamentoprocessos.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuariosRepository usuariosRepository;

    public UsuarioServiceImpl(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> existente = usuariosRepository.findByNomeAndEmail(usuario.getNome(), usuario.getEmail());
        if (existente.isPresent() && (usuario.getId() == null || !existente.get().getId().equals(usuario.getId()))) {
                throw new EntidadeEmUsoException("Já existe um usuário com as credenciais informadas.");
            }
        return usuariosRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuariosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para o ID informado."));
    }

    @Override
    public void excluir(Long id) {
        usuariosRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }


}
