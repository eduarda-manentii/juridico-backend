package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartesEnvolvidasRepository extends JpaRepository<ParteEnvolvida, Long> {

    List<ParteEnvolvida> findByTipoParteEnvolvidaAndDocumentoValor(
            TipoParteEnvolvida tipo,
            String valorDocumento);

}
