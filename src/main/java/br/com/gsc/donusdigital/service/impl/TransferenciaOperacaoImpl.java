package br.com.gsc.donusdigital.service.impl;

import br.com.gsc.donusdigital.dto.TransferenciaDTO;
import br.com.gsc.donusdigital.service.ContaHistoricoService;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.service.Operacao;
import br.com.gsc.donusdigital.validation.impl.OperacaoValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaOperacaoImpl implements Operacao<TransferenciaDTO> {

    private static final String DESCRICAO_HISTORICO_ORIGEM = "Transferência para conta %s";
    private static final String DESCRICAO_HISTORICO_RECEBIDO = "Transferência recebida da conta %s";

    @Autowired
    private ContaHistoricoService contaHistoricoService;

    @Autowired
    private ContaService contaService;

    @Autowired
    private OperacaoValidatorImpl operacaoValidator;

    @Override
    public void efetivar(TransferenciaDTO operacaoDTO) {
        // obtem contas
        final var contaOrigem = this.contaService.getByNumeroConta(operacaoDTO.getNumeroConta());
        final var contaDestino = this.contaService.getContaPorNumeroContaECpf(operacaoDTO.getContaDestino(),
                operacaoDTO.getCpfTitular());

        // atualiza valores
        this.atualizarSaldo(contaOrigem, operacaoDTO.getValor().negate(), null,
                String.format(DESCRICAO_HISTORICO_ORIGEM, operacaoDTO.getContaDestino()));
        this.atualizarSaldo(contaDestino, operacaoDTO.getValor(), null,
                String.format(DESCRICAO_HISTORICO_RECEBIDO, operacaoDTO.getNumeroConta()));
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
