package br.com.gsc.donusdigital.validation.impl;

import br.com.gsc.donusdigital.exception.SaldoInsuficienteException;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Locale;

@Service
public class OperacaoValidatorImpl implements Validator<Conta> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public void validar(Conta objeto) {
        if (objeto.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoInsuficienteException(this.messageSource.getMessage("saldo.insuficiente.message",
                    null, Locale.getDefault()));
        }
    }
}
