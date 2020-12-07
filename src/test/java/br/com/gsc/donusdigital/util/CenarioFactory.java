package br.com.gsc.donusdigital.util;

import br.com.gsc.donusdigital.dto.ClienteDTO;
import br.com.gsc.donusdigital.dto.ContaDTO;
import br.com.gsc.donusdigital.dto.OperacaoDTO;
import br.com.gsc.donusdigital.dto.TransferenciaDTO;
import br.com.gsc.donusdigital.model.Cliente;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.model.ContaHistorico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CenarioFactory {

    public static ContaHistorico getHistorico() {
        return ContaHistorico.builder().dataAcao(LocalDateTime.MAX).descricao("TESTE").numeroConta(5646l)
                .taxa(BigDecimal.ONE).valor(BigDecimal.ONE).build();
    }

    public static Page<ContaHistorico> getPaginacaoHistorico() {
        return new PageImpl<ContaHistorico>(List.of(getHistorico()));
    }

    public static Conta getConta() {
        return Conta.builder().dataCriacao(LocalDateTime.MAX).dataUltimaAtualizacao(LocalDateTime.MAX)
                .numeroConta(5646l).digito(1).saldo(BigDecimal.ZERO).cliente(getCliente()).build();
    }

    public static Cliente getCliente() {
        return Cliente.builder().cpf("145.544.545.54").nome("TESTE").id(321l).build();
    }

    public static ClienteDTO getClienteDTOValido() {
        return ClienteDTO.builder().cpf("131.066.922-86").nome("CLIENTE TESTE").build();
    }

    public static ContaDTO getContaDTO() {
        return ContaDTO.builder().numeroConta(5646l).digito(1).saldo(BigDecimal.ZERO).cliente(getClienteDTOValido())
                .build();
    }

    public static OperacaoDTO getOperacaoDTO(final BigDecimal valor) {
        return OperacaoDTO.builder().valor(valor).numeroConta(65l).build();
    }

    public static Conta getContaComSaldoInvalido() {
        return Conta.builder().dataCriacao(LocalDateTime.MAX).dataUltimaAtualizacao(LocalDateTime.MAX)
                .numeroConta(5646l).digito(1).saldo(BigDecimal.ONE.negate()).cliente(getCliente()).build();
    }

    public static TransferenciaDTO getTransferencia() {
        return TransferenciaDTO.builder().valor(new BigDecimal(100)).numeroConta(65l)
                .contaDestino(564l).cpfTitular("052.145.581-20").build();
    }

}
