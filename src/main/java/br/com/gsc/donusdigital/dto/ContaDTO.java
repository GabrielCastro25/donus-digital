package br.com.gsc.donusdigital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaDTO {
    private Long numeroConta;
    private Integer digito;
    private BigDecimal saldo;
    private ClienteDTO cliente;
}
