package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ProcessoService {

    Processo salvar(Processo processo);

    Processo buscarPorId(Long id);

    void excluir(Long id);

}
