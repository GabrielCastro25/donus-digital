package br.com.gsc.donusdigital.service.impl;

import br.com.gsc.donusdigital.dto.OperacaoDTO;
import br.com.gsc.donusdigital.service.ContaHistoricoService;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.service.CustoOperacao;
import br.com.gsc.donusdigital.service.Operacao;
import br.com.gsc.donusdigital.validation.impl.OperacaoValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SaqueOperacaoImpl implements Operacao<OperacaoDTO>, CustoOperacao {

    private static final BigDecimal PORCENTAGEM_DESCONTO_SAQUE = new BigDecimal(1);
    private static final String DESCRICAO_HISTORICO = "Saque";

    @Autowired
    private ContaHistoricoService contaHistoricoService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private OperacaoValidatorImpl operacaoValidator;

    @Override
    public void efetivar(OperacaoDTO operacaoDTO) {
        final var conta = this.contaService.getByNumeroConta(operacaoDTO.getNumeroConta());
        final var taxa = this.calcularPorcentagem(operacaoDTO.getValor(), PORCENTAGEM_DESCONTO_SAQUE);
        this.atualizarSaldo(conta, operacaoDTO.getValor().negate(), taxa.negate(), DESCRICAO_HISTORICO);
    }

    @Override
    public ContaHistoricoService getHistoricoService() {
        return this.contaHistoricoService;
    }

    @Override
    public ContaService getContaService() {
        return this.contaService;
    }

    @Override
    public OperacaoValidatorImpl getValidator() {
        return this.operacaoValidator;
    }

}
