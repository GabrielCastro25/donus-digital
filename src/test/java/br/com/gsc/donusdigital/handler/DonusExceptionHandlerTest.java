package br.com.gsc.donusdigital.handler;

import br.com.gsc.donusdigital.dto.ErroDTO;
import br.com.gsc.donusdigital.exception.ContaInexistenteException;
import br.com.gsc.donusdigital.exception.CpfExistenteException;
import br.com.gsc.donusdigital.exception.HistoricoSemDadosException;
import br.com.gsc.donusdigital.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class DonusExceptionHandlerTest {

    @Autowired
    private DonusExceptionHandler exceptionHandler;

    @Test
    public void test_handleGeneralException() {
        // Given
        final Exception ex = new Exception("General Exception Test Message");
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleGeneralException(ex, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagem()).isEqualTo("Erro - Não foi possível completar a operação.");
        assertThat(erroDTO.getStatus()).isEqualTo(500);
    }

    @Test
    public void test_handleContaInexistenteException() {
        // Given
        final String mensagem = "Teste erro";
        final ContaInexistenteException ex = new ContaInexistenteException(mensagem);
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleExceptionsNotFound(ex, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagem()).isEqualTo(mensagem);
        assertThat(erroDTO.getStatus()).isEqualTo(404);
    }

    @Test
    public void test_handleHistoricoSemDadosException() {
        // Given
        final String mensagem = "Teste erro";
        final HistoricoSemDadosException ex = new HistoricoSemDadosException(mensagem);
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleExceptionsNotFound(ex, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagem()).isEqualTo(mensagem);
        assertThat(erroDTO.getStatus()).isEqualTo(404);
    }

    @Test
    public void test_handleSaldoInsuficienteException() {
        // Given
        final String mensagem = "Teste erro";
        final SaldoInsuficienteException ex = new SaldoInsuficienteException(mensagem);
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleExpectionsBadRequest(ex, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagem()).isEqualTo(mensagem);
        assertThat(erroDTO.getStatus()).isEqualTo(400);
    }

    @Test
    public void test_handleCpfExistenteException() {
        // Given
        final String mensagem = "Teste erro";
        final CpfExistenteException ex = new CpfExistenteException(mensagem);
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleExpectionsBadRequest(ex, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagem()).isEqualTo(mensagem);
        assertThat(erroDTO.getStatus()).isEqualTo(400);
    }

    @Test
    public void test_handleMethodArgumentNotValid() {
        // Given
        final MethodArgumentNotValidException mockMethodArgument = Mockito.mock(MethodArgumentNotValidException.class);
        final BindingResult mockBindingResult = Mockito.mock(BindingResult.class);
        given(mockMethodArgument.getBindingResult()).willReturn(mockBindingResult);
        given(mockBindingResult.getFieldErrors()).willReturn(List.of(new FieldError("teste", "teste","teste")));
        final HttpStatus httpStatus = Mockito.mock(HttpStatus.class);
        final ServletWebRequest mockRequest = Mockito.mock(ServletWebRequest.class);

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json");

        // When
        final ResponseEntity<Object> response = this.exceptionHandler.handleMethodArgumentNotValid(mockMethodArgument, headers,
                httpStatus, mockRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody() instanceof ErroDTO).isTrue();
        final ErroDTO erroDTO = (ErroDTO) response.getBody();
        assertThat(erroDTO.getMensagens()).isNotEmpty();
        assertThat(erroDTO.getStatus()).isEqualTo(400);
    }

}
