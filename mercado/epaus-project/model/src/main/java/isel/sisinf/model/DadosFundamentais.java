package isel.sisinf.model;

import java.math.BigDecimal;

@Entity
@Table(name = "dados_fundamentais")
public class DadosFundamentais {

    @Id
    @Column(name = "instrumento_isin", length = 12)
    private String instrumentoIsin;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "instrumento_isin")
    private Instrumento instrumento;

    @Column(name = "variacao_diaria", nullable = false, precision = 15, scale = 2)
    private BigDecimal variacaoDiaria;

    @Column(name = "valor_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal valorActual;

    @Column(name = "media_6_meses", nullable = false, precision = 15, scale = 2)
    private BigDecimal media6Meses;

    @Column(name = "variacao_6_meses", nullable = false, precision = 15, scale = 2)
    private BigDecimal variacao6Meses;

    @Column(name = "percentagem_variacao_diaria", nullable = false, precision = 7, scale = 2)
    private BigDecimal percentagemVariacaoDiaria;

    @Column(name = "percentagem_variacao_6_meses", nullable = false, precision = 7, scale = 2)
    private BigDecimal percentagemVariacao6Meses;

    public DadosFundamentais() {}

    public DadosFundamentais(Instrumento instrumento, BigDecimal variacaoDiaria, BigDecimal valorActual, 
                               BigDecimal media6Meses, BigDecimal variacao6Meses, 
                               BigDecimal percentagemVariacaoDiaria, BigDecimal percentagemVariacao6Meses) {
        this.instrumento = instrumento;
        this.variacaoDiaria = variacaoDiaria;
        this.valorActual = valorActual;
        this.media6Meses = media6Meses;
        this.variacao6Meses = variacao6Meses;
        this.percentagemVariacaoDiaria = percentagemVariacaoDiaria;
        this.percentagemVariacao6Meses = percentagemVariacao6Meses;
    }

    // Getters e Setters
    public String getInstrumentoIsin() { return instrumentoIsin; }
    public void setInstrumentoIsin(String instrumentoIsin) { this.instrumentoIsin = instrumentoIsin; }

    public Instrumento getInstrumento() { return instrumento; }
    public void setInstrumento(Instrumento instrumento) { this.instrumento = instrumento; }

    public BigDecimal getVariacaoDiaria() { return variacaoDiaria; }
    public void setVariacaoDiaria(BigDecimal variacaoDiaria) { this.variacaoDiaria = variacaoDiaria; }

    public BigDecimal getValorActual() { return valorActual; }
    public void setValorActual(BigDecimal valorActual) { this.valorActual = valorActual; }

    public BigDecimal getMedia6Meses() { return media6Meses; }
    public void setMedia6Meses(BigDecimal media6Meses) { this.media6Meses = media6Meses; }

    public BigDecimal getVariacao6Meses() { return variacao6Meses; }
    public void setVariacao6Meses(BigDecimal variacao6Meses) { this.variacao6Meses = variacao6Meses; }

    public BigDecimal getPercentagemVariacaoDiaria() { return percentagemVariacaoDiaria; }
    public void setPercentagemVariacaoDiaria(BigDecimal percentagemVariacaoDiaria) { this.percentagemVariacaoDiaria = percentagemVariacaoDiaria; }

    public BigDecimal getPercentagemVariacao6Meses() { return percentagemVariacao6Meses; }
    public void setPercentagemVariacao6Meses(BigDecimal percentagemVariacao6Meses) { this.percentagemVariacao6Meses = percentagemVariacao6Meses; }
}