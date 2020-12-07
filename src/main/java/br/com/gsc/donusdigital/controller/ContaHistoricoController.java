package br.com.gsc.donusdigital.controller;

import br.com.gsc.donusdigital.dto.ContaHistoricoDTO;
import br.com.gsc.donusdigital.service.ContaHistoricoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("v1/transacoes")
public class ContaHistoricoController {

    @Autowired
    private ContaHistoricoService contaHistoricoService;

    public final static LocalDate DATA_INICIO_PADRAO = LocalDate.now().minusDays(7);
    private final static LocalDate DATA_FIM_PADRAO = LocalDate.now();

    @GetMapping("/contas/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ContaHistoricoDTO> obterHistoricoPorConta(@PathVariable("id") final Long numeroConta,
                                                          @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataInicio,
                                                          @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataFim,
                                                          final Pageable pageable) {
        final var inicio = (dataInicio == null) ? DATA_INICIO_PADRAO : dataInicio;
        final var fim = (dataFim == null) ? DATA_FIM_PADRAO : dataFim;
        return this.contaHistoricoService.obterHistoricoTransacoes(numeroConta, inicio, fim, pageable);
    }

}
