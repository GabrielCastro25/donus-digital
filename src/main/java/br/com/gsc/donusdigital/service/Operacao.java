package br.com.gsc.donusdigital.service;

import br.com.gsc.donusdigital.dto.OperacaoDTO;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.model.ContaHistorico;
import br.com.gsc.donusdigital.validation.impl.OperacaoValidatorImpl;

import java.math.BigDecimal;

public interface Operacao <T extends OperacaoDTO> {

    ContaHistoricoService getHistoricoService();
    ContaService getContaService();
    OperacaoValidatorImpl getValidator();

    void efetivar(T operacaoDTO);

    default void inserirHistorico(ContaHistorico historico) {
        getHistoricoService().inserirTransacao(historico);
    }

    default void atualizarSaldo(final Conta conta, BigDecimal valorTransacao, BigDecimal taxa, String descricao) {
        // calcula valor operacao
        final BigDecimal valorAdicao = (taxa == null) ? valorTransacao : valorTransacao.add(taxa);
        conta.addValor(valorAdicao);

        // realiza validacoes
        this.getValidator().validar(conta);

        // salva objetos
        this.inserirHistorico(new ContaHistorico(conta.getNumeroConta(), valorTransacao, taxa, descricao));
        this.getContaService().atualizarConta(conta);
    }
}
