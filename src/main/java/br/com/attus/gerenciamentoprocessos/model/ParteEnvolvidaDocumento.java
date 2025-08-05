package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "parte_envolvida_documento")
@Entity
public class ParteEnvolvidaDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "valor", nullable = false)
    private String valor;

}
