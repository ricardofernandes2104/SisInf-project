package isel.sisinf.model;

import java.util.List;

@Entity
@Table(name = "mercado")
public class Mercado {

    @Id
    @Column(name = "mercado_id")
    private String mercadoId;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "nome_curto", nullable = false, length = 50)
    private String nomeCurto;

    @OneToMany(mappedBy = "mercado", fetch = FetchType.LAZY)
    private List<Instrumento> instrumentos;

    @OneToMany(mappedBy = "mercado", fetch = FetchType.LAZY)
    private List<ValorMercadoDiario> valoresDiarios;

    public Mercado() {}

    public Mercado(String mercadoId, String descricao, String nomeCurto) {
        this.mercadoId = mercadoId;
        this.descricao = descricao;
        this.nomeCurto = nomeCurto;
    }

    // Getters e Setters
    public String getMercadoId() { return mercadoId; }
    public void setMercadoId(String mercadoId) { this.mercadoId = mercadoId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getNomeCurto() { return nomeCurto; }
    public void setNomeCurto(String nomeCurto) { this.nomeCurto = nomeCurto; }

    public List<Instrumento> getInstrumentos() { return instrumentos; }
    public List<ValorMercadoDiario> getValoresDiarios() { return valoresDiarios; }
}
