package br.com.gsc.donusdigital.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaHistoricoDTO {

    private Long id;

    private String descricao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAcao;

    @JsonIgnore
    private BigDecimal valor;

    @JsonIgnore
    private BigDecimal taxa;

    public BigDecimal getValorTransacao() {
        return (this.taxa != null) ? this.valor.add(this.taxa) : this.valor;
    }

}
