package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.DuplicidadeDocumentoException;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasDocumentosRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParteEnvolvidaServiceImpl implements ParteEnvolvidaService {

    private final PartesEnvolvidasRepository partesEnvolvidasRepository;

    public ParteEnvolvidaServiceImpl(PartesEnvolvidasRepository partesEnvolvidasRepository, PartesEnvolvidasDocumentosRepository partesEnvolvidasDocumentosRepository) {
        this.partesEnvolvidasRepository = partesEnvolvidasRepository;
    }

    @Override
    public ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida) {
        String valorDoc = parteEnvolvida.getDocumento().getValor().replaceAll("\\D", "");
        if (parteEnvolvida.getDocumento().getTipoDocumento() == TipoDocumento.CPF) {
            if (partesEnvolvidasRepository.existsByTipoParteEnvolvidaAndDocumento_ValorAndNomeCompleto(
                    parteEnvolvida.getTipoParteEnvolvida(),
                    valorDoc,
                    parteEnvolvida.getNomeCompleto())) {
                throw new DuplicidadeDocumentoException("Parte envolvida já existente.");
            }
            if (partesEnvolvidasRepository.existsByTipoDocumentoAndValorAndNomeDiferente(
                    parteEnvolvida.getTipoParteEnvolvida(),
                    valorDoc,
                    parteEnvolvida.getNomeCompleto())) {
                throw new DuplicidadeDocumentoException("CPF já está associado a outro nome para o mesmo tipo de parte.");
            }
        }
        return partesEnvolvidasRepository.save(parteEnvolvida);
    }

    @Override
    public ParteEnvolvida buscarPorId(Long id) {
       Optional<ParteEnvolvida> parteEnvolvidaEncontrada = partesEnvolvidasRepository.findById(id);
       if(parteEnvolvidaEncontrada.isPresent()) {
           return parteEnvolvidaEncontrada.get();
       } else {
          throw new NullPointerException("Não foi encontrada parte envolvida para o id informado.");
       }
    }

    @Override
    public void excluir(Long id) {
        this.partesEnvolvidasRepository.deleteById(id);
    }

}
