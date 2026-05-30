package isel.sisinf.model;

import java.util.List;

@Entity
@Table(name = "instrumento")
public class Instrumento {

    @Id
    @Column(name = "instrumento_id", length = 12)
    private String instrumentoId;

    @Column(name = "descricao", nullable = false, length = 256)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mercado", nullable = false)
    private Mercado mercado;

    @OneToOne(mappedBy = "instrumento", fetch = FetchType.LAZY)
    private DadosFundamentais dadosFundamentais;

    @OneToMany(mappedBy = "instrumento", fetch = FetchType.LAZY)
    private List<ValorInstrumentoDiario> valoresDiarios;

    @OneToMany(mappedBy = "instrumento", fetch = FetchType.LAZY)
    private List<Posicao> posicoes;

    public Instrumento() {}

    public Instrumento(String instrumentoId, String descricao, Mercado mercado) {
        this.instrumentoId = instrumentoId;
        this.descricao = descricao;
        this.mercado = mercado;
    }

    // Getters e Setters
    public String getInstrumentoId() { return instrumentoId; }
    public void setInstrumentoId(String instrumentoId) { this.instrumentoId = instrumentoId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Mercado getMercado() { return mercado; }
    public void setMercado(Mercado mercado) { this.mercado = mercado; }

    public DadosFundamentais getDadosFundamentais() { return dadosFundamentais; }
    public void setDadosFundamentais(DadosFundamentais dadosFundamentais) { this.dadosFundamentais = dadosFundamentais; }

    public List<ValorInstrumentoDiario> getValoresDiarios() { return valoresDiarios; }
    public void setValoresDiarios(List<ValorInstrumentoDiario> valoresDiarios) { this.valoresDiarios = valoresDiarios; }

    public List<Posicao> getPosicoes() { return posicoes; }
    public void setPosicoes(List<Posicao> posicoes) { this.posicoes = posicoes; }
}
