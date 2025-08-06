package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.DuplicidadeDocumentoException;
import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasDocumentosRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParteEnvolvidaServiceImpl implements ParteEnvolvidaService {

    private final PartesEnvolvidasRepository partesEnvolvidasRepository;
    private final PartesEnvolvidasDocumentosRepository partesEnvolvidasDocumentosRepository;
    private final ProcessosRepository processosRepository;

    public ParteEnvolvidaServiceImpl(PartesEnvolvidasRepository partesEnvolvidasRepository, PartesEnvolvidasDocumentosRepository partesEnvolvidasDocumentosRepository, PartesEnvolvidasDocumentosRepository partesEnvolvidasDocumentosRepository1, ProcessosRepository processosRepository) {
        this.partesEnvolvidasRepository = partesEnvolvidasRepository;
        this.partesEnvolvidasDocumentosRepository = partesEnvolvidasDocumentosRepository1;
        this.processosRepository = processosRepository;
    }

    @Override
    public ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida) {
        normalizarCampos(parteEnvolvida);
        validarDocumento(parteEnvolvida);
        if (parteEnvolvida.getId() != null) {
            ParteEnvolvida existente = partesEnvolvidasRepository.findById(parteEnvolvida.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Parte envolvida não encontrada"));
            if (!existente.getDocumento().getValor().equals(parteEnvolvida.getDocumento().getValor())) {
                verificarDuplicidade(parteEnvolvida);
            }
        } else {
            verificarDuplicidade(parteEnvolvida);
        }
        return partesEnvolvidasRepository.save(parteEnvolvida);
    }


    @Override
    public ParteEnvolvida buscarPorId(Long id) {
        return partesEnvolvidasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parte envolvida não encontrada para o ID informado."));
    }

    @Override
    public void excluir(Long id) {
        boolean existeEmProcesso = processosRepository.existsByPartesEnvolvidas_Id(id);
        if (existeEmProcesso) {
            throw new EntidadeEmUsoException("Não é possível excluir. Esta parte está envolvida com um processo.");
        }
        partesEnvolvidasRepository.deleteById(id);
    }

    @Override
    public List<ParteEnvolvida> listarPorIds(List<Long> ids) {
        return partesEnvolvidasRepository.findAllById(ids);
    }

    public Page<ParteEnvolvida> listarTodos(Pageable pageable) {
        return partesEnvolvidasRepository.findAll(pageable);
    }

    private void normalizarCampos(ParteEnvolvida parteEnvolvida) {
        String telefoneLimpo = parteEnvolvida.getTelefone().replaceAll("\\D", "");
        parteEnvolvida.setTelefone(telefoneLimpo);
        String valorDoc = parteEnvolvida.getDocumento().getValor().replaceAll("\\D", "");
        parteEnvolvida.getDocumento().setValor(valorDoc);
    }

    private void validarDocumento(ParteEnvolvida parteEnvolvida) {
        ParteEnvolvidaDocumento doc = parteEnvolvida.getDocumento();
        String valor = doc.getValor();
        TipoDocumento tipo = doc.getTipoDocumento();

        if (tipo == TipoDocumento.CPF && valor.length() != 11) {
            throw new IllegalArgumentException("CPF inválido: deve conter 11 dígitos.");
        }

        if (tipo == TipoDocumento.CNPJ && valor.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido: deve conter 14 dígitos.");
        }

        if (doc.getId() != null) {
            ParteEnvolvidaDocumento existente = partesEnvolvidasDocumentosRepository.findById(doc.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));
            parteEnvolvida.setDocumento(existente);
        }
    }

    private void verificarDuplicidade(ParteEnvolvida parteEnvolvida) {
        String valorDoc = parteEnvolvida.getDocumento().getValor();
        List<ParteEnvolvida> existentes = partesEnvolvidasRepository.findByTipoParteEnvolvidaAndDocumentoValor(
                parteEnvolvida.getTipoParteEnvolvida(),
                valorDoc);
        for (ParteEnvolvida existente : existentes) {
            if (parteEnvolvida.getId() == null || !existente.getId().equals(parteEnvolvida.getId())) {
                throw new DuplicidadeDocumentoException("Documento já está associado a outro registro.");
            }
        }
    }

}
