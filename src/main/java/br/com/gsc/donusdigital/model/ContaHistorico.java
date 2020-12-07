package br.com.gsc.donusdigital.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@Entity
@Table(name="tb_conta_historico")
public class ContaHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nr_conta")
    private Long numeroConta;

    private BigDecimal valor;

    private BigDecimal taxa;

    private String descricao;

    @Column(name = "dt_acao", nullable = false)
    @CreationTimestamp
    private LocalDateTime dataAcao;

    public ContaHistorico(Long numeroConta, BigDecimal valor, String descricao) {
        this.numeroConta = numeroConta;
        this.valor = valor;
        this.descricao = descricao;
    }

    public ContaHistorico(Long numeroConta, BigDecimal valor, BigDecimal taxa,String descricao) {
        this(numeroConta, valor, descricao);
        this.taxa = taxa;
    }
}
