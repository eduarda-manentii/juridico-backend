package br.com.attus.gerenciamentoprocessos;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.model.*;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class Mocker {

    public Usuario gerarUsuario(Long id) {
        return Usuario.builder()
                .id(id)
                .nome("Fulano")
                .email("email@gmail.com")
                .senha("12345678")
                .build();
    }

    public ParteEnvolvida gerarParteEnvolvida(Long id) {
        ParteEnvolvidaDocumento documento = new ParteEnvolvidaDocumento();
        documento.setId(1L);
        documento.setValor("12345678900");
        documento.setTipoDocumento(TipoDocumento.CPF);

        return ParteEnvolvida.builder()
                .id(id)
                .nomeCompleto("Parte " + id)
                .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                .email("parte" + id + "@email.com")
                .telefone("4899999999" + id)
                .documento(documento)
                .build();
    }

    public ParteEnvolvidaDto gerarParteEnvolvidaDto(Long id) {
        return ParteEnvolvidaDto.builder()
                .id(id)
                .nomeCompleto("Parte " + id)
                .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                .email("parte" + id + "@email.com")
                .telefone("4899999999" + id)
                .build();
    }

    public AndamentoProcessual gerarAndamento(Long id) {
        return AndamentoProcessual.builder()
                .id(id)
                .descricao("Descrição do andamento " + id)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.of(2025, 8, 7))
                .build();
    }

    public AndamentoProcessualDto gerarAndamentoDto(Long id) {
        return AndamentoProcessualDto.builder()
                .id(id)
                .descricao("Descrição do andamento " + id)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.of(2025, 8, 7))
                .build();
    }

    public Processo gerarProcesso(Long id) {
        return Processo.builder()
                .id(id)
                .dataAbertura(LocalDate.of(2025, 8, 6))
                .descricaoCaso("Caso gerado " + id)
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(gerarAndamento(1L))
                .partesEnvolvidas(List.of(gerarParteEnvolvida(1L), gerarParteEnvolvida(2L)))
                .build();
    }

    public ProcessoDto gerarProcessoDto(Long id) {
        return ProcessoDto.builder()
                .id(id)
                .dataAbertura(LocalDate.of(2025, 8, 6))
                .descricaoCaso("Caso DTO " + id)
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(gerarAndamentoDto(1L))
                .partesEnvolvidas(List.of(gerarParteEnvolvidaDto(1L), gerarParteEnvolvidaDto(2L)))
                .build();
    }

    public AndamentoProcessualDto gerarAndamentoProcessualDto(Long id) {
        return gerarAndamentoDto(id);
    }

    public AndamentoProcessual gerarAndamentoProcessual(Long id) {
        return gerarAndamento(id);
    }

}
