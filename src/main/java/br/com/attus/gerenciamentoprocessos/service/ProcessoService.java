package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public interface ProcessoService {

    Processo salvar(Processo processo);

    Processo buscarPorId(Long id);

    void excluir(Long id);

    void arquivarProcesso(Long id);

    Page<Processo> buscarPorFiltros(
            StatusProcesso status,
            LocalDate dataAbertura,
            String documento,
            Pageable paginacao
    );

    Page<Processo> listarTodos(Pageable pageable);

    boolean existsByAndamentoProcessualId(Long id);
}
