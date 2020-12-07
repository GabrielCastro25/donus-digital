package br.com.gsc.donusdigital.repository;

import br.com.gsc.donusdigital.model.ContaHistorico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ContaHistoricoRepository extends CrudRepository<ContaHistorico, Long> {

    @Query(value = "select hist from ContaHistorico hist where hist.numeroConta = :nrConta AND TRUNC(hist.dataAcao) BETWEEN :dtInicio AND :dtFim ORDER BY hist.dataAcao DESC")
    Page<ContaHistorico> pesquisarHistoricosPorNumeroContaEPeriodo(@Param("nrConta") Long numeroConta, @Param("dtInicio") LocalDate dataInicio,
                                                           @Param("dtFim") LocalDate dataFim, Pageable pageable);
}
