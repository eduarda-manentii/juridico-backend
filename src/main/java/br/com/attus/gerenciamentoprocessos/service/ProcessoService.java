package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Validated
public interface ProcessoService {

    Processo salvar(Processo processo);

    Processo buscarPorId(Long id);

    void excluir(Long id);

    void arquivarProcesso(Long id);

    List<Processo> buscarPorFiltros(StatusProcesso status, LocalDate dataAbertura, String documento);

}
