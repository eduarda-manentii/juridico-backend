package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ProcessosRepository extends JpaRepository<Processo, Long> {

    @Query("""
    SELECT DISTINCT p FROM Processo p
    JOIN p.partesEnvolvidas parte
    LEFT JOIN parte.documento doc
    WHERE (:status IS NULL OR p.status = :status)
      AND p.dataAbertura = :dataAbertura
      AND (:documento IS NULL OR doc.valor LIKE %:documento%)
    """)
    Page<Processo> buscarPorFiltrosComData(
            @Param("status") StatusProcesso status,
            @Param("dataAbertura") LocalDate dataAbertura,
            @Param("documento") String documento,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT p FROM Processo p
    JOIN p.partesEnvolvidas parte
    LEFT JOIN parte.documento doc
    WHERE (:status IS NULL OR p.status = :status)
      AND (:documento IS NULL OR doc.valor LIKE %:documento%)
    """)
    Page<Processo> buscarPorFiltrosSemData(
            @Param("status") StatusProcesso status,
            @Param("documento") String documento,
            Pageable pageable
    );

    boolean existsByAndamentoProcessualId(Long andamentoProcessualId);

    boolean existsByPartesEnvolvidasId(Long parteEnvolvidaId);

}
