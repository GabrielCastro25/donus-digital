package br.com.gsc.donusdigital.validation.impl;

import br.com.gsc.donusdigital.exception.SaldoInsuficienteException;
import br.com.gsc.donusdigital.util.CenarioFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
public class OperacaoValidatorImplTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private final OperacaoValidatorImpl operacaoValidator = new OperacaoValidatorImpl();

    @Test
    public void validar_enviandoDadosValidos_esperadoSucesso() {
        this.operacaoValidator.validar(CenarioFactory.getConta());
    }

    @Test
    public void validar_enviandoSaldoNegativo_esperadoException() {
        Assertions.assertThatExceptionOfType(SaldoInsuficienteException.class)
                .isThrownBy(() -> this.operacaoValidator.validar(CenarioFactory.getContaComSaldoInvalido()));

    }
}
