package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "partes_envolvidas_documentos")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParteEnvolvidaDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "valor", nullable = false)
    private String valor;

}
