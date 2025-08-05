package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ParteEnvolvidaService {

    ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida);

    ParteEnvolvida buscarPorId(Long id);

    void excluir(Long id);

}
