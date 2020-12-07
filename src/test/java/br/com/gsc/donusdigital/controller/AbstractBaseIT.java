package br.com.gsc.donusdigital.controller;

import br.com.gsc.donusdigital.utils.Constante;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

/**
 * Classe abstrata base para todos os testes Integrados.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class AbstractBaseIT {

    @Autowired
    private CacheManager cacheManager;

    @BeforeAll
    public static void setUpAll() {
        RestAssured.port = 9999;
        RestAssured.baseURI = "http://localhost";
    }

    /**
     * Limpa cache
     */
    protected void clearCache() {
        this.cacheManager.getCache(Constante.CACHE_HISTORICO_CONTAS).clear();
        this.cacheManager.getCache(Constante.CACHE_CONTA).clear();
        this.cacheManager.getCache(Constante.CACHE_CONTA_CPF).clear();
        this.cacheManager.getCache(Constante.CACHE_CONTA_NUMERO_CPF).clear();
    }

}