package br.com.gsc.donusdigital.service;

import br.com.gsc.donusdigital.exception.ContaInexistenteException;
import br.com.gsc.donusdigital.model.Cliente;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.repository.ContaRepository;
import br.com.gsc.donusdigital.service.impl.DepositoOperacaoImpl;
import br.com.gsc.donusdigital.util.CenarioFactory;
import br.com.gsc.donusdigital.validation.impl.AberturaContaValidatorImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AberturaContaValidatorImpl aberturaContaValidator;

    @Mock
    private DepositoOperacaoImpl depositoOperacao;

    @InjectMocks
    private final ContaService service = new ContaService();

    @Test
    public void criarConta_enviandoObjetoValido_esperadoSucesso() {
        final var clienteRequest = CenarioFactory.getClienteDTOValido();
        given(modelMapper.map(clienteRequest, Cliente.class)).willReturn(CenarioFactory.getCliente());

        this.service.criarConta(clienteRequest);

        verify(this.contaRepository).save(any(Conta.class));
        verify(this.modelMapper).map(clienteRequest, Cliente.class);
        verify(this.aberturaContaValidator).validar(any(Conta.class));
    }

    @Test
    public void getByNumeroConta_enviandoNumeroValido_esperadoSucesso() {
        final var numeroConta = 654l;
        given(contaRepository.findById(numeroConta)).willReturn(Optional.of(CenarioFactory.getConta()));

        final var conta = this.service.getByNumeroConta(numeroConta);

        assertThat(conta).isNotNull();
        verify(this.contaRepository).findById(numeroConta);
    }

    @Test
    public void getByNumeroConta_enviandoNumeroInvalido_esperadoException() {
        final var numeroConta = 654l;
        final var mensagemEsperada = "Conta informada não existe..";
        given(contaRepository.findById(numeroConta)).willReturn(Optional.empty());

        given(this.messageSource.getMessage("conta.inexistente.message", null, Locale.getDefault()))
                .willReturn(mensagemEsperada);

        Assertions.assertThatExceptionOfType(ContaInexistenteException.class)
                .isThrownBy(() -> this.service.getByNumeroConta(numeroConta))
                .withMessageContaining(mensagemEsperada);
    }

    @Test
    public void getContaPorNumeroContaECpf_enviandoDadosValidos_esperadoSucesso() {
        final var numeroConta = 654l;
        final var cpf = "5645645";
        given(contaRepository.getPorNumeroContaECpfCliente(numeroConta, cpf)).willReturn(Optional.of(CenarioFactory.getConta()));

        final var conta = this.service.getContaPorNumeroContaECpf(numeroConta, cpf);

        assertThat(conta).isNotNull();
        verify(this.contaRepository).getPorNumeroContaECpfCliente(numeroConta, cpf);
    }

    @Test
    public void getContaPorNumeroContaECpf_enviandoDadosInvalidos_esperadoException() {
        final var numeroConta = 654l;
        final var cpf = "5645645";
        final var mensagemEsperada = "Conta informada para destino da transferência não existe e/ou não pertence ao CPF informado.";
        given(contaRepository.getPorNumeroContaECpfCliente(numeroConta, cpf)).willReturn(Optional.empty());

        given(this.messageSource.getMessage("conta.destino.inexistente.message", null, Locale.getDefault()))
                .willReturn(mensagemEsperada);

        Assertions.assertThatExceptionOfType(ContaInexistenteException.class)
                .isThrownBy(() -> this.service.getContaPorNumeroContaECpf(numeroConta, cpf))
                .withMessageContaining(mensagemEsperada);
    }

    @Test
    public void getContaPorCpfCliente_enviandoDadosValidos_esperadoSucesso() {
        final var cpf = "5645645";
        given(contaRepository.getPorCpfCliente(cpf)).willReturn(CenarioFactory.getConta());

        final var conta = this.service.getContaPorCpfCliente(cpf);

        assertThat(conta).isNotNull();
        verify(this.contaRepository).getPorCpfCliente(cpf);
    }

    @Test
    public void atualizarConta_enviandoDadosValidos_esperadoSucesso() {
        final var conta = CenarioFactory.getConta();

        this.service.atualizarConta(conta);

        verify(this.contaRepository).save(conta);
    }

    @Test
    public void getConta_enviandoDadosValidos_esperadoSucesso() {
        final var numeroConta = 654l;
        given(contaRepository.findById(numeroConta)).willReturn(Optional.of(CenarioFactory.getConta()));
        given(modelMapper.map(any(), any())).willReturn(CenarioFactory.getContaDTO());

        final var contaDTO = this.service.getConta(numeroConta);

        assertThat(contaDTO).isNotNull();
        verify(this.contaRepository).findById(numeroConta);
        verify(this.modelMapper).map(any(), any());
    }

    @Test
    public void processarAcao_enviandoDadosValidos_esperadoSucesso() {
        final var operacao = CenarioFactory.getOperacaoDTO(BigDecimal.ONE);
        this.service.processarAcao(this.depositoOperacao, 156l, operacao);
        verify(this.depositoOperacao).efetivar(operacao);
    }
}
