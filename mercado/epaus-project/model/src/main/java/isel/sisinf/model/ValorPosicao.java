package isel.sisinf.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "valor_posicao")
public class ValorPosicao {

    @EmbeddedId
    private PosicaoId id;

    @Column(name = "quantidade", precision = 15, scale = 4)
    private BigDecimal quantidade;

    @Column(name = "valor_actual", precision = 15, scale = 2)
    private BigDecimal valorActual;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    public ValorPosicao() {}

    public PosicaoId getId() { return id; }
    public BigDecimal getQuantidade() { return quantidade; }
    public BigDecimal getValorActual() { return valorActual; }
    public BigDecimal getValorTotal() { return valorTotal; }
}