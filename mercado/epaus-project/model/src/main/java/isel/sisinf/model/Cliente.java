package isel.sisinf.model;

import jakarta.persistence.*;
import org.eclipse.persistence.annotations.OptimisticLocking;
import org.eclipse.persistence.annotations.OptimisticLockingType;
import java.util.List;

@Entity
@Table(name = "cliente")
@OptimisticLocking(type = OptimisticLockingType.ALL_COLUMNS)
public class Cliente {

    @Id
    @Column(name = "nif", length = 20)
    private String nif;

    @Column(name = "cartao_cidadao", unique = true, nullable = false, length = 20)
    private String cartaoCidadao;

    @Column(name = "nome", nullable = false, length = 256)
    private String nome;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Portefolio> portefolios;

    public Cliente() {}

    public Cliente(String nif, String cartaoCidadao, String nome) {
        this.nif = nif;
        this.cartaoCidadao = cartaoCidadao;
        this.nome = nome;
    }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getCartaoCidadao() { return cartaoCidadao; }
    public void setCartaoCidadao(String cartaoCidadao) { this.cartaoCidadao = cartaoCidadao; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Portefolio> getPortefolios() { return portefolios; }
    public void setPortefolios(List<Portefolio> portefolios) { this.portefolios = portefolios; }
}