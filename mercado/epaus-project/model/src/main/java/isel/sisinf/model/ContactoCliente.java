package isel.sisinf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contacto_cliente")
public class ContactoCliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_nif")
    private Cliente cliente;

    @Column(name = "tipo", length = 20)
    private String tipo;

    @Column(name = "descricao", length = 50)
    private String descricao;

    @Column(name = "contacto", length = 254)
    private String contacto;

    public ContactoCliente() {}

    public ContactoCliente(Cliente cliente, String tipo, String descricao, String contacto) {
        this.cliente = cliente;
        this.tipo = tipo;
        this.descricao = descricao;
        this.contacto = contacto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
}