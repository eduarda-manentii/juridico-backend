package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessosRepository extends JpaRepository<Processo, Long> {
}
