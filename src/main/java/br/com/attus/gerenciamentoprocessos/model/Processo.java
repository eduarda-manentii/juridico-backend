package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Table(name = "processo")
@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "descricao_caso", nullable = false, length = 200)
    private String descricaoCaso;

    @OneToMany(mappedBy = "processo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParteEnvolvida> parteEnvolvida;

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
