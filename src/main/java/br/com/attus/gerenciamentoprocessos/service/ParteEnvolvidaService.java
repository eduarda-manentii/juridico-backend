package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ParteEnvolvidaService {

    ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida);

    void atualizarTipoPorId(Long id, TipoParteEnvolvida tipoParteEnvolvida);

    ParteEnvolvida buscarPorId(Long id);

    ParteEnvolvida excluir(Long id);

}
