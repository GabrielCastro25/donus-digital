package br.com.gsc.donusdigital.controller;

import br.com.gsc.donusdigital.dto.ClienteDTO;
import br.com.gsc.donusdigital.dto.ContaDTO;
import br.com.gsc.donusdigital.dto.ErroDTO;
import br.com.gsc.donusdigital.util.CenarioFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * Classe de testes integrados do endpoint de contas
 */
@Sql({"/db/dados-teste.sql",})
@Sql(scripts = {"/db/limpar-base.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ContaControllerIT extends ControllerBaseIT {

    @Test
    public void abrirConta_enviandoDadosInvalidos_esperadoBusinessException() {
        // Given
        final var clienteDTO = new ClienteDTO();

        // When
        final var erro = given()
                .contentType("application/json")
                .body(clienteDTO)
                .when()
                .post("api/v1/contas")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erro).isNotNull();
        assertThat(erro.getMensagens()).isNotNull();
    }

    @Test
    public void abrirConta_enviandoCpfExistente_esperadoBusinessException() {
        // Given
        final var clienteDTO = CenarioFactory.getClienteDTOExistente();

        // When
        final var erro = given()
                .contentType("application/json")
                .body(clienteDTO)
                .when()
                .post("api/v1/contas")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erro).isNotNull();
        assertThat(erro.getMensagem()).isEqualTo("O CPF informado já esta associado a conta 1. Por gentileza informe outro CPF.");
    }

    @Test
    public void abrirConta_enviandoDadosValidos_esperadoSucesso() {
        // Given
        final var clienteDTO = CenarioFactory.getClienteDTOValido();

        // When
        final var contaDTO = given()
                .contentType("application/json")
                .body(clienteDTO)
                .when()
                .post("api/v1/contas")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType("application/json").extract()
                .body()
                .as(ContaDTO.class);

        assertThat(contaDTO).isNotNull();
        assertThat(contaDTO.getNumeroConta()).isNotNull();
        assertThat(contaDTO.getCliente().getCpf()).isEqualTo(clienteDTO.getCpf());
        assertThat(contaDTO.getCliente().getNome()).isEqualTo(clienteDTO.getNome());
    }

    @Test
    public void getConta_enviandoNumeroContaExistente_esperadoSucesso() {
        // Given
        final var numeroConta = 1l;

        // When
        final ContaDTO contaDTO = obterConta(numeroConta);

        assertThat(contaDTO).isNotNull();
        assertThat(contaDTO.getNumeroConta()).isEqualTo(numeroConta);
    }

    private ContaDTO obterConta(long numeroConta) {
        final var contaDTO = given()
                .contentType("application/json")
                .when()
                .get("api/v1/contas/"+ numeroConta)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json").extract()
                .body()
                .as(ContaDTO.class);
        return contaDTO;
    }

    @Test
    public void getConta_enviandoNumeroContaInexistente_esperadoNotFound() {
        // Given
        final var numeroConta = 999l;

        // When
        final var erroDTO = given()
                .contentType("application/json")
                .when()
                .get("api/v1/contas/"+numeroConta)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erroDTO).isNotNull();
        assertThat(erroDTO.getMensagem()).isEqualTo("Conta informada não existe.");
    }

    @Test
    public void depositar_enviandoDadosValidos_esperadoNotFound() {
        // Given
        final var numeroConta = 1l;
        final var valorDeposito = new BigDecimal(100);
        final var saldoEsperado = new BigDecimal(100.50);
        final var operacao = CenarioFactory.getOperacaoDTO(valorDeposito);

        // When
        given()
                .contentType("application/json")
                .body(operacao)
                .when()
                .patch("api/v1/contas/"+numeroConta+"/depositos")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        final var contaAtualizada = this.obterConta(numeroConta);
        assertThat(contaAtualizada.getSaldo().setScale(2)).isEqualTo(saldoEsperado.setScale(2));
    }

    @Test
    public void sacar_saldoSemConta_esperadoBadRequest() {
        // Given
        final var numeroConta = 1l;
        final var valorSaque = new BigDecimal(600);
        final var operacao = CenarioFactory.getOperacaoDTO(valorSaque);

        // When
        final var erroDTO = given()
                .contentType("application/json")
                .body(operacao)
                .when()
                .patch("api/v1/contas/"+numeroConta+"/saques")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erroDTO).isNotNull();
        assertThat(erroDTO.getMensagem()).isEqualTo("Não é possível realizar a operação pois a conta não possui saldo suficiente.");
    }

    @Test
    public void sacar_enviandoDadosValidos_esperadoSucesso() {
        // Given
        final var numeroConta = 2l;
        final var valorSaque = new BigDecimal(200);
        final var saldoEsperado = new BigDecimal(298.00);
        final var operacao = CenarioFactory.getOperacaoDTO(valorSaque);

        // When
        given()
                .contentType("application/json")
                .body(operacao)
                .when()
                .patch("api/v1/contas/"+numeroConta+"/saques")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        final var contaAtualizada = this.obterConta(numeroConta);
        assertThat(contaAtualizada.getSaldo().setScale(2)).isEqualTo(saldoEsperado.setScale(2));
    }

    @Test
    public void transferir_enviandoDadosValidos_esperadoSucesso() {
        // Given
        final var numeroConta = 2l;
        final var saldoEsperado = new BigDecimal(400.00);
        final var transferencia = CenarioFactory.getTransferenciaDTOValido();

        // When
        given()
                .contentType("application/json")
                .body(transferencia)
                .when()
                .patch("api/v1/contas/"+numeroConta+"/transferencias")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        final var contaAtualizada = this.obterConta(numeroConta);
        assertThat(contaAtualizada.getSaldo().setScale(2)).isEqualTo(saldoEsperado.setScale(2));

        final var contaDestino = this.obterConta(transferencia.getContaDestino());
        assertThat(contaDestino.getSaldo().setScale(2)).isEqualTo(transferencia.getValor().setScale(2));
    }

    @Test
    public void transferir_enviandoDadosInvalidos_esperadoNotFound() {
        // Given
        final var numeroConta = 2l;
        final var saldoEsperado = new BigDecimal(400.00);
        final var transferencia = CenarioFactory.getTransferencia();

        // When
        final var erroDTO = given()
                .contentType("application/json")
                .body(transferencia)
                .when()
                .patch("api/v1/contas/"+numeroConta+"/transferencias")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erroDTO).isNotNull();
        assertThat(erroDTO.getMensagem()).isEqualTo("Conta informada para destino da transferência não existe e/ou não pertence ao CPF informado.");
    }

}
