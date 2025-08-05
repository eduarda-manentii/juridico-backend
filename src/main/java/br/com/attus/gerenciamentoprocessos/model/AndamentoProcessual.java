package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Table(name = "andamento_processual")
@Entity
public class AndamentoProcessual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "tipo_andamento_processual", nullable = false)
    private TipoAndamentoProcessual tipoAndamentoProcessual;

    @Column(name = "data_registro", nullable = false)
    private LocalDate dataRegistro;

    @Column(name = "descricao", nullable = false, length = 200)
    private String descricao;

}
