package br.com.gsc.donusdigital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransferenciaDTO extends OperacaoDTO {

    @NotNull
    private Long contaDestino;

    @NotBlank
    @CPF
    private String cpfTitular;
}
