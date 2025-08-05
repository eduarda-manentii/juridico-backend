package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Table(name = "partes_envolvidas")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParteEnvolvida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 50)
    private String nomeCompleto;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoParteEnvolvida tipoParteEnvolvida;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_documento")
    private ParteEnvolvidaDocumento documento;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @ManyToMany(mappedBy = "parteEnvolvida")
    private List<Processo> processos;

}
