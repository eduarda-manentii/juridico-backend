package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Optional;

@Service
public class ParteEnvolvidaServiceImpl implements ParteEnvolvidaService {


    private final PartesEnvolvidasRepository partesEnvolvidasRepository;

    public ParteEnvolvidaServiceImpl(PartesEnvolvidasRepository partesEnvolvidasRepository) {
        this.partesEnvolvidasRepository = partesEnvolvidasRepository;
    }


    @Override
    public ParteEnvolvida salvar(ParteEnvolvida parteEnvolvida) {
        return partesEnvolvidasRepository.save(parteEnvolvida);
    }

    @Override
    public void atualizarTipoPorId(Long id, TipoParteEnvolvida tipoParteEnvolvida) {
        Optional<ParteEnvolvida> parteEnvolvidaEncontrada = partesEnvolvidasRepository.findById(id);
        if(parteEnvolvidaEncontrada.isPresent()) {
            this.partesEnvolvidasRepository.updateTipoParteEnvolvidaById(id, tipoParteEnvolvida);
        } else {
            throw new NullPointerException("Não foi encontrada parte envolvida para o id informado.");
        }
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
    public ParteEnvolvida excluir(Long id) {
        ParteEnvolvida parteEnvolvidaParaExcluir = buscarPorId(id);
        this.partesEnvolvidasRepository.deleteById(id);
        return parteEnvolvidaParaExcluir;
    }

}
