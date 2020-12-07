package br.com.gsc.donusdigital.exception;

@SuppressWarnings("serial")
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(final String message) {
        super(message);
    }
}
