package br.com.gsc.donusdigital.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@Entity
@Table(name="tb_conta")
public class Conta extends Model {

    @Id
    @Column(name = "nr_conta")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long numeroConta;

    private Integer digito;

    private BigDecimal saldo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nr_conta")
    private Cliente cliente;

    public Conta(Cliente cliente) {
        this.cliente = cliente;
        this.digito = 1;
        this.saldo = BigDecimal.ZERO;
    }

    public void addValor(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }

}
