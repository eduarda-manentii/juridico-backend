package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "processos")
@Entity
@AllArgsConstructor
@Builder
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "descricao_caso", nullable = false, length = 200)
    private String descricaoCaso;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "processos_partes_envolvidas",
            joinColumns = @JoinColumn(name = "processo_id"),
            inverseJoinColumns = @JoinColumn(name = "parte_envolvida_id")
    )
    private List<ParteEnvolvida> partesEnvolvidas;

    @JoinColumn(name = "id_andamento_processual")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AndamentoProcessual andamentoProcessual;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusProcesso status;

    public Processo() {
        this.status = StatusProcesso.ATIVO;
    }

}
