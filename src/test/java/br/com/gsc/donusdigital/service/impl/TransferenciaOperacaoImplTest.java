package br.com.gsc.donusdigital.service.impl;

import br.com.gsc.donusdigital.exception.ContaInexistenteException;
import br.com.gsc.donusdigital.service.ContaHistoricoService;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.util.CenarioFactory;
import br.com.gsc.donusdigital.validation.impl.OperacaoValidatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TransferenciaOperacaoImplTest {

    @Mock
    private ContaHistoricoService contaHistoricoService;

    @Mock
    private ContaService contaService;

    @Mock
    private OperacaoValidatorImpl operacaoValidator;

    @InjectMocks
    private final TransferenciaOperacaoImpl transferenciaOperacao = new TransferenciaOperacaoImpl();

    @Test
    public void efetivar_enviandoDadosValidos_esperadoSucesso() {
        final var operacao = CenarioFactory.getTransferencia();
        given(contaService.getByNumeroConta(operacao.getNumeroConta())).willReturn(CenarioFactory.getConta());
        given(contaService.getContaPorNumeroContaECpf(operacao.getContaDestino(), operacao.getCpfTitular()))
                .willReturn(CenarioFactory.getConta());

        this.transferenciaOperacao.efetivar(operacao);

        verify(this.contaService).getByNumeroConta(operacao.getNumeroConta());
        verify(this.contaService).getContaPorNumeroContaECpf(operacao.getContaDestino(), operacao.getCpfTitular());
        verify(this.contaHistoricoService, times(2)).inserirTransacao(any());
        verify(this.contaService, times(2)).atualizarConta(any());
        verify(this.operacaoValidator, times(2)).validar(any());
    }

    @Test
    public void efetivar_enviandoContaDestinoECpfInvalido_esperadoException() {
        final var operacao = CenarioFactory.getTransferencia();
        given(contaService.getByNumeroConta(operacao.getNumeroConta())).willReturn(CenarioFactory.getConta());
        given(contaService.getContaPorNumeroContaECpf(operacao.getContaDestino(), operacao.getCpfTitular()))
                .willThrow(new ContaInexistenteException("teste"));

        Assertions.assertThatExceptionOfType(ContaInexistenteException.class)
                .isThrownBy(() -> this.transferenciaOperacao.efetivar(operacao));

        verify(this.contaService).getByNumeroConta(operacao.getNumeroConta());
        verify(this.contaService).getContaPorNumeroContaECpf(operacao.getContaDestino(), operacao.getCpfTitular());
        verify(this.contaHistoricoService, never()).inserirTransacao(any());
        verify(this.contaService, never()).atualizarConta(any());
        verify(this.operacaoValidator, never()).validar(any());
    }
}
