package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AndamentosProcessuaisRepository extends JpaRepository<AndamentoProcessual, Long> {
}
