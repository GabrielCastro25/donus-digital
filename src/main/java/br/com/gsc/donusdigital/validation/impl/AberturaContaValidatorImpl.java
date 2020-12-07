package br.com.gsc.donusdigital.validation.impl;

import br.com.gsc.donusdigital.exception.CpfExistenteException;
import br.com.gsc.donusdigital.model.Conta;
import br.com.gsc.donusdigital.service.ContaService;
import br.com.gsc.donusdigital.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AberturaContaValidatorImpl implements Validator<Conta> {

    @Autowired
    private ContaService contaService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void validar(Conta objeto) {
        final var conta =this.contaService.getContaPorCpfCliente(objeto.getCliente().getCpf());
        if (conta != null) {
            throw new CpfExistenteException(this.messageSource.getMessage("cpf.existente.message",
                    new Object[]{conta.getNumeroConta()}, Locale.getDefault()));
        }
    }

}
