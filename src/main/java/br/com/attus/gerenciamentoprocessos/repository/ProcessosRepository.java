package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProcessosRepository extends JpaRepository<Processo, Long> {

    @Query("""
    SELECT DISTINCT p FROM Processo p
    JOIN p.parteEnvolvida parte
    JOIN parte.documento doc
    WHERE (:status IS NULL OR p.status = :status)
      AND (:dataAbertura IS NULL OR p.dataAbertura = :dataAbertura)
      AND (:documento IS NULL OR doc.valor = :documento)
    """)
    List<Processo> buscarPorFiltros(
            @Param("status") StatusProcesso status,
            @Param("dataAbertura") LocalDate dataAbertura,
            @Param("documento") String documento
    );

}
