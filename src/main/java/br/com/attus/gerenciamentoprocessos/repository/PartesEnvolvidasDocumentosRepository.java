package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartesEnvolvidasDocumentosRepository extends JpaRepository<ParteEnvolvidaDocumento, Long> {

  }
