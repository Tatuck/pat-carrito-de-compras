package edu.comillas.icai.gitt.pat.spring.practcarritos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import org.hibernate.annotations.Formula;

import java.util.List;

@Entity
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @NotNull
    @Column(nullable = false, unique = true)
    private Long idUsuario;

    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String emailUsuario;

    @Formula("(SELECT COALESCE(SUM(l.coste_linea), 0) FROM linea_carrito l WHERE l.carrito_id = id_carrito)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double totalPrecio;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<LineaCarrito> lineas;

    public Carrito() {
    }

    public Carrito(Long idUsuario, String emailUsuario) {
        this.idUsuario = idUsuario;
        this.emailUsuario = emailUsuario;
        this.totalPrecio = 0;
    }

    public Long getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Long idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public double getTotalPrecio() {
        return totalPrecio;
    }

    public List<LineaCarrito> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaCarrito> lineas) {
        this.lineas = lineas;
    }
}
