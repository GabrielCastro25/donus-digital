package br.com.gsc.donusdigital.exception;

@SuppressWarnings("serial")
public class CpfExistenteException extends RuntimeException {
    public CpfExistenteException(final String message) {
        super(message);
    }
}
