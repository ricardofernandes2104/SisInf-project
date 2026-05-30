package isel.sisinf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "instrumento")
public class Instrumento {

    @Id
    @Column(name = "instrumento_id", length = 12)
    private String instrumentoId;

    @Column(name = "descricao", nullable = false, length = 256)
    private String descricao;

    @Column(name = "mercado", nullable = false, length = 20)
    private String mercadoId;

    public Instrumento() {}

    public Instrumento(String instrumentoId, String descricao, String mercadoId) {
        this.instrumentoId = instrumentoId;
        this.descricao = descricao;
        this.mercadoId = mercadoId;
    }

    public String getInstrumentoId() { return instrumentoId; }
    public void setInstrumentoId(String instrumentoId) { this.instrumentoId = instrumentoId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getMercadoId() { return mercadoId; }
    public void setMercadoId(String mercadoId) { this.mercadoId = mercadoId; }
}