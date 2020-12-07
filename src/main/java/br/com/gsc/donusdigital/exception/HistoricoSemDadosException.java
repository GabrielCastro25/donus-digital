package br.com.gsc.donusdigital.exception;

@SuppressWarnings("serial")
public class HistoricoSemDadosException extends RuntimeException {
    public HistoricoSemDadosException(final String message) {
        super(message);
    }
}
