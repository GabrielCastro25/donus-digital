package br.com.gsc.donusdigital.service;

import br.com.gsc.donusdigital.dto.ClienteDTO;
import br.com.gsc.donusdigital.dto.ContaDTO;
import br.com.gsc.donusdigital.dto.OperacaoDTO;
import br.com.gsc.donusdigital.exception.ContaInexistenteException;
import br.com.gsc.donusdigital.model.Cliente;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.repository.ContaRepository;
import br.com.gsc.donusdigital.utils.Constante;
import br.com.gsc.donusdigital.validation.impl.AberturaContaValidatorImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private AberturaContaValidatorImpl aberturaContaValidator;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @CacheEvict(value = {Constante.CACHE_CONTA, Constante.CACHE_CONTA_CPF}, allEntries = true)
    public ContaDTO criarConta(final ClienteDTO clienteRequest) {
        final var conta = new Conta(modelMapper.map(clienteRequest, Cliente.class));
        this.aberturaContaValidator.validar(conta);
        this.contaRepository.save(conta);
        return cast(conta);
    }

    public ContaDTO getConta(final Long numeroConta) {
        final var conta = this.getByNumeroConta(numeroConta);
        return cast(conta);
    }

    @Cacheable(Constante.CACHE_CONTA)
    public Conta getByNumeroConta(final Long numeroConta) {
        return this.contaRepository.findById(numeroConta).orElseThrow(() ->
                new ContaInexistenteException(this.messageSource.getMessage("conta.inexistente.message",
                        null, Locale.getDefault())));
    }

    public void atualizarConta(final Conta conta) {
        this.contaRepository.save(conta);
    }

    @Cacheable(Constante.CACHE_CONTA_CPF)
    public Conta getContaPorCpfCliente(String cpf) {
        return this.contaRepository.getPorCpfCliente(cpf);
    }

    @Cacheable(Constante.CACHE_CONTA_NUMERO_CPF)
    public Conta getContaPorNumeroContaECpf(Long numeroConta, String cpf) {
        return this.contaRepository.getPorNumeroContaECpfCliente(numeroConta, cpf).orElseThrow(() ->
                new ContaInexistenteException(this.messageSource.getMessage("conta.destino.inexistente.message",
                        null, Locale.getDefault())));
    }

    private ContaDTO cast(Conta objetc) {
        return modelMapper.map(objetc, ContaDTO.class);
    }

    public void processarAcao(Operacao operacao, Long numeroConta, OperacaoDTO request) {
        request.setNumeroConta(numeroConta);
        operacao.efetivar(request);
    }
}
