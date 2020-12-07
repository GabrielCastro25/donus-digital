package br.com.gsc.donusdigital.service.impl;

import br.com.gsc.donusdigital.service.ContaHistoricoService;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.util.CenarioFactory;
import br.com.gsc.donusdigital.validation.impl.OperacaoValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SaqueOperacaoImplTest {

    @Mock
    private ContaHistoricoService contaHistoricoService;

    @Mock
    private ContaService contaService;

    @Mock
    private OperacaoValidatorImpl operacaoValidator;

    @InjectMocks
    private final SaqueOperacaoImpl saqueOperacao = new SaqueOperacaoImpl();

    @Test
    public void efetivar_enviandoDadosValidos_esperadoSucesso() {
        final var operacao = CenarioFactory.getOperacaoDTO(new BigDecimal(100));
        given(contaService.getByNumeroConta(operacao.getNumeroConta())).willReturn(CenarioFactory.getConta());

        this.saqueOperacao.efetivar(operacao);

        verify(this.contaService).getByNumeroConta(operacao.getNumeroConta());
        verify(this.contaHistoricoService).inserirTransacao(any());
        verify(this.contaService).atualizarConta(any());
        verify(this.operacaoValidator).validar(any());
    }
}
