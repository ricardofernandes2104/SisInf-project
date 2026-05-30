package isel.sisinf.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PosicaoId implements Serializable {

    @Column(name = "portefolio")
    private Long portefolioId;

    @Column(name = "instrumento_isin", length = 12)
    private String instrumentoIsin;

    public PosicaoId() {}

    public PosicaoId(Long portefolioId, String instrumentoIsin) {
        this.portefolioId = portefolioId;
        this.instrumentoIsin = instrumentoIsin;
    }

    public Long getPortefolioId() { return portefolioId; }
    public String getInstrumentoIsin() { return instrumentoIsin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PosicaoId posicaoId = (PosicaoId) o;
        return Objects.equals(portefolioId, posicaoId.portefolioId) &&
                Objects.equals(instrumentoIsin, posicaoId.instrumentoIsin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(portefolioId, instrumentoIsin);
    }
}