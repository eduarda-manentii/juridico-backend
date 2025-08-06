package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AndamentoProcessualService {

    AndamentoProcessual salvar(AndamentoProcessual andamentoProcessual);

    AndamentoProcessual buscarPorId(Long id);

    void excluir(Long id);

    Page<AndamentoProcessual> listarTodos(Pageable pageable);

}
