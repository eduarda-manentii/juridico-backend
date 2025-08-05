package br.com.attus.gerenciamentoprocessos.repository;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PartesEnvolvidasRepository extends JpaRepository<ParteEnvolvida, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ParteEnvolvida p SET p.tipoParteEnvolvida = :tipo WHERE p.id = :id")
    void updateTipoParteEnvolvidaById(@Param("id") Long id,
                                      @Param("tipo") TipoParteEnvolvida tipo);

    boolean existsByTipoParteEnvolvidaAndDocumento_ValorAndNomeCompleto(
            TipoParteEnvolvida tipoParteEnvolvida, String valorDocumento, String nomeCompleto);

    boolean existsByTipoParteEnvolvidaAndDocumento_Valor(
            TipoParteEnvolvida tipoParteEnvolvida, String valorDocumento);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ParteEnvolvida p " +
            "WHERE p.tipoParteEnvolvida = :tipo AND p.documento.valor = :valor AND p.nomeCompleto <> :nome")
    boolean existsByTipoDocumentoAndValorAndNomeDiferente(@Param("tipo") TipoParteEnvolvida tipo,
                                                          @Param("valor") String valor,
                                                          @Param("nome") String nomeCompleto);

}
