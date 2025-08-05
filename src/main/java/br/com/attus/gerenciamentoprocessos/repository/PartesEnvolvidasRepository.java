package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PartesEnvolvidasRepository extends JpaRepository<ParteEnvolvida, Long> {

    void updateTipoParteEnvolvidaById(Long id, TipoParteEnvolvida tipo);

}
