package br.com.gsc.donusdigital.service;

import br.com.gsc.donusdigital.dto.ContaHistoricoDTO;
import br.com.gsc.donusdigital.exception.HistoricoSemDadosException;
import br.com.gsc.donusdigital.model.ContaHistorico;
import br.com.gsc.donusdigital.repository.ContaHistoricoRepository;
import br.com.gsc.donusdigital.utils.Constante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ContaHistoricoService {

    @Autowired
    private ContaHistoricoRepository contaHistoricoRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ModelMapper modelMapper;

    @CacheEvict(value = Constante.CACHE_HISTORICO_CONTAS, allEntries = true)
    public void inserirTransacao(ContaHistorico contaHistorico) {
        this.contaHistoricoRepository.save(contaHistorico);
    }

    @Cacheable(Constante.CACHE_HISTORICO_CONTAS)
    public Page<ContaHistoricoDTO> obterHistoricoTransacoes(final Long numeroConta, final LocalDate dataInicio,
                                                            final LocalDate dataFim, Pageable pageable) {
        Page<ContaHistorico> historicoPage = this.contaHistoricoRepository.pesquisarHistoricosPorNumeroContaEPeriodo(numeroConta,
                dataInicio, dataFim, pageable);

        if (historicoPage.isEmpty()) {
            throw new HistoricoSemDadosException(this.messageSource.getMessage("historico.sem.dados.message",
                    null, Locale.getDefault()));
        }

        return new PageImpl<>(
                historicoPage.getContent().stream()
                        .map(item -> modelMapper.map(item, ContaHistoricoDTO.class)).collect(Collectors.toList()),
                pageable, historicoPage.getTotalElements());
    }
}
