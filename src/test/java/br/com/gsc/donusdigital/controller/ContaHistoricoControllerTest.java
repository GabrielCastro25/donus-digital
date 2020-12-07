package br.com.gsc.donusdigital.controller;

import br.com.gsc.donusdigital.dto.ErroDTO;
import br.com.gsc.donusdigital.util.RestResponsePage;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * Classe de testes integrados do endpoint de historico
 */
@Sql({"/db/dados-teste.sql",})
@Sql(scripts = {"/db/limpar-base.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ContaHistoricoControllerTest extends ControllerBaseIT {

    @Test
    public void obterHistoricoPorConta_enviandoParametrosValidos_esperadoSucesso() {
        // Given
        final var numeroConta = 2l;

        // When
        final var transacoes = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("v1/transacoes/contas/"+ numeroConta)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("application/json").extract()
                .body()
                .as(RestResponsePage.class);

        assertThat(transacoes).isNotNull();
        assertThat(transacoes.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void obterHistoricoPorConta_enviandoParametrosInvalidos_esperadoNotFound() {
        // Given
        final var numeroConta = 2l;
        final var dataInicio = LocalDate.now().minusDays(10);
        final var dataFim = LocalDate.now().minusDays(5);

        // When
        final var erroDTO = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("v1/transacoes/contas/"+ numeroConta+"?dataInicio="+dataInicio+"&dataFim="+dataFim)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType("application/json").extract()
                .body()
                .as(ErroDTO.class);

        assertThat(erroDTO).isNotNull();
        assertThat(erroDTO.getMensagem()).isEqualTo("Não existem transações para conta e período informados.");
    }

}
