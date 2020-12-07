package br.com.gsc.donusdigital.validation.impl;

import br.com.gsc.donusdigital.exception.CpfExistenteException;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.util.CenarioFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class AberturaContaValidatorImplTest {

    @Mock
    private ContaService contaService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private final AberturaContaValidatorImpl aberturaContaValidator = new AberturaContaValidatorImpl();

    @Test
    public void validar_enviandoDadosValidos_esperadoSucesso() {
        final var conta = CenarioFactory.getConta();
        given(contaService.getContaPorCpfCliente(conta.getCliente().getCpf())).willReturn(null);

        this.aberturaContaValidator.validar(conta);

        verify(this.contaService).getContaPorCpfCliente(conta.getCliente().getCpf());
    }

    @Test
    public void validar_enviandoCpfJaExistente_esperadoException() {
        final var conta = CenarioFactory.getConta();

        given(contaService.getContaPorCpfCliente(conta.getCliente().getCpf())).willReturn(CenarioFactory.getConta());

        Assertions.assertThatExceptionOfType(CpfExistenteException.class)
                .isThrownBy(() -> this.aberturaContaValidator.validar(conta));

        verify(this.contaService).getContaPorCpfCliente(conta.getCliente().getCpf());
    }
}
