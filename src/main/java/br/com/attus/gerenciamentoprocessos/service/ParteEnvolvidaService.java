package br.com.attus.gerenciamentoprocessos.service;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface ParteEnvolvidaService {

    ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida);

    ParteEnvolvida buscarPorId(Long id);

    List<ParteEnvolvida> listarPorIds(List<Long> ids);

    void excluir(Long id);

}
