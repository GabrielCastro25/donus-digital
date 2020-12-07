package br.com.gsc.donusdigital.handler;

import br.com.gsc.donusdigital.dto.ErroDTO;
import br.com.gsc.donusdigital.exception.ContaInexistenteException;
import br.com.gsc.donusdigital.exception.CpfExistenteException;
import br.com.gsc.donusdigital.exception.HistoricoSemDadosException;
import br.com.gsc.donusdigital.exception.SaldoInsuficienteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class DonusExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private static final String PATTERN_ERRO = "Exception do tipo %s handled";

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleGeneralException(final Exception exception, final WebRequest request) {
        final HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErroDTO internalServerErrorResponse = ErroDTO.builder()
            .mensagem(this.messageSource.getMessage("exception.general.error.message", null, Locale.getDefault()))
            .status(responseStatus.value())
            .build();
        log.error(String.format(PATTERN_ERRO, exception.getClass()), exception);
        return handleExceptionInternal(exception, internalServerErrorResponse, new HttpHeaders(),
            responseStatus, request);
    }

    @ExceptionHandler({ContaInexistenteException.class, HistoricoSemDadosException.class})
    protected ResponseEntity<Object> handleExceptionsNotFound(final RuntimeException exception,
        final WebRequest request) {
        final HttpStatus responseStatus = HttpStatus.NOT_FOUND;
        final ErroDTO internalServerErrorResponse = ErroDTO.builder()
                .mensagem(exception.getMessage())
                .status(responseStatus.value())
                .build();
        log.error(String.format(PATTERN_ERRO, exception.getClass()));
        return handleExceptionInternal(exception, internalServerErrorResponse, new HttpHeaders(), responseStatus, request);
    }

    @ExceptionHandler({SaldoInsuficienteException.class, CpfExistenteException.class})
    protected ResponseEntity<Object> handleExpectionsBadRequest(final RuntimeException exception,
                                                                     final WebRequest request) {
        final HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        final ErroDTO internalServerErrorResponse = ErroDTO.builder()
                .mensagem(exception.getMessage())
                .status(responseStatus.value())
                .build();
        log.error(String.format(PATTERN_ERRO, exception.getClass()));
        return handleExceptionInternal(exception, internalServerErrorResponse, new HttpHeaders(), responseStatus, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
                                                                  final HttpStatus status, final WebRequest request) {
        final List<String> errorList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField().concat(": ").concat(fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        final HttpStatus responseStatus = HttpStatus.BAD_REQUEST;
        final ErroDTO internalServerErrorResponse = ErroDTO.builder()
                .mensagens(errorList)
                .status(responseStatus.value())
                .build();
        log.error(String.format(PATTERN_ERRO, ex.getClass()));
        return handleExceptionInternal(ex, internalServerErrorResponse, headers, responseStatus, request);
    }

}
