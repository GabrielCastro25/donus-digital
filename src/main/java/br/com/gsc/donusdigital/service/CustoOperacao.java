package br.com.gsc.donusdigital.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface CustoOperacao {

    default BigDecimal calcularPorcentagem(BigDecimal valor, BigDecimal porcentagemValor) {
        final var cemPorcento = new BigDecimal(100);
        return valor.multiply(porcentagemValor).divide(cemPorcento, RoundingMode.HALF_UP).setScale(2);
    }
}
