package br.com.gsc.donusdigital.service;

import br.com.gsc.donusdigital.dto.ContaHistoricoDTO;
import br.com.gsc.donusdigital.exception.HistoricoSemDadosException;
import br.com.gsc.donusdigital.repository.ContaHistoricoRepository;
import br.com.gsc.donusdigital.util.CenarioFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContaHistoricoServiceTest {

    @Mock
    private ContaHistoricoRepository contaHistoricoRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private final ContaHistoricoService service = new ContaHistoricoService();

    @Test
    public void inserirTransacao_enviandoObjetoValido_esperadoSucesso() {
        final var objeto = CenarioFactory.getHistorico();
        this.service.inserirTransacao(objeto);
        verify(this.contaHistoricoRepository).save(objeto);
    }

    @Test
    public void obterHistoricoTransacoes_enviandoFiltrosValidos_esperadoSucesso() {
        final var numeroConta = 156l;
        final var dataInicio = LocalDate.now();
        final var dataFim = LocalDate.now();
        final var pageable = Pageable.unpaged();
        given(this.contaHistoricoRepository.pesquisarHistoricosPorNumeroContaEPeriodo(numeroConta, dataInicio, dataFim,
                pageable)).willReturn(CenarioFactory.getPaginacaoHistorico());

        Page<ContaHistoricoDTO> transacoes = this.service.obterHistoricoTransacoes(numeroConta, dataInicio, dataFim, pageable);
        assertThat(transacoes).isNotNull();
        assertThat(transacoes.getTotalElements()).isEqualTo(CenarioFactory.getPaginacaoHistorico().getTotalElements());
        verify(this.contaHistoricoRepository).pesquisarHistoricosPorNumeroContaEPeriodo(numeroConta, dataInicio, dataFim,
                pageable);
    }

    @Test
    public void obterHistoricoTransacoes_enviandoFiltrosInvalidos_esperadoException() {
        final var mensagemEsperada = "Não existem transações para conta e período informados.";
        final var numeroConta = 156l;
        final var dataInicio = LocalDate.now();
        final var dataFim = LocalDate.now();
        final var pageable = Pageable.unpaged();

        given(this.contaHistoricoRepository.pesquisarHistoricosPorNumeroContaEPeriodo(numeroConta, dataInicio, dataFim,
                pageable)).willReturn(Page.empty());

        given(this.messageSource.getMessage("historico.sem.dados.message", null,Locale.getDefault()))
                .willReturn(mensagemEsperada);
        Assertions.assertThatExceptionOfType(HistoricoSemDadosException.class)
                .isThrownBy(() -> this.service.obterHistoricoTransacoes(numeroConta, dataInicio, dataFim, pageable))
                .withMessageContaining(mensagemEsperada);
    }

}
