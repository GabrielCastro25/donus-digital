package br.com.gsc.donusdigital.controller;

import br.com.gsc.donusdigital.dto.ClienteDTO;
import br.com.gsc.donusdigital.dto.ContaDTO;
import br.com.gsc.donusdigital.dto.OperacaoDTO;
import br.com.gsc.donusdigital.dto.TransferenciaDTO;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.service.impl.DepositoOperacaoImpl;
import br.com.gsc.donusdigital.service.impl.SaqueOperacaoImpl;
import br.com.gsc.donusdigital.service.impl.TransferenciaOperacaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private DepositoOperacaoImpl depositoOperacao;

    @Autowired
    private SaqueOperacaoImpl saqueOperacao;

    @Autowired
    private TransferenciaOperacaoImpl transferenciaOperacao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDTO criarConta(@Valid @RequestBody final ClienteDTO clienteRequest) {
        return this.contaService.criarConta(clienteRequest);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContaDTO getConta(@PathVariable("id") final Long numeroConta) {
        return this.contaService.getConta(numeroConta);
    }

    @PatchMapping("{id}/depositos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void depositar(@PathVariable("id") final Long numeroConta, @Valid @RequestBody final OperacaoDTO operacaoDTO) {
        this.contaService.processarAcao(this.depositoOperacao, numeroConta, operacaoDTO);
    }

    @PatchMapping("{id}/saques")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sacar(@PathVariable("id") final Long numeroConta, @Valid @RequestBody final OperacaoDTO operacaoDTO) {
        this.contaService.processarAcao(this.saqueOperacao, numeroConta, operacaoDTO);
    }

    @PatchMapping("{id}/transferencias")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sacar(@PathVariable("id") final Long numeroConta, @Valid @RequestBody final TransferenciaDTO transferenciaDTO) {
        this.contaService.processarAcao(this.transferenciaOperacao, numeroConta, transferenciaDTO);
    }

}
