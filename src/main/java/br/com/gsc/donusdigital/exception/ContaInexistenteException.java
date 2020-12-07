package br.com.gsc.donusdigital.exception;

@SuppressWarnings("serial")
public class ContaInexistenteException extends RuntimeException {
    public ContaInexistenteException(final String message) {
        super(message);
    }
}
