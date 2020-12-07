package br.com.gsc.donusdigital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OperacaoDTO {

    @Positive
    private BigDecimal valor;

    private Long numeroConta;
}
