package edu.comillas.icai.gitt.pat.spring.practcarritos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class LineaCarrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLineaCarrito;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonIgnore
    private Carrito carrito;

    @NotNull
    @Column(nullable = false)
    private Long idArticulo;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private double precioUnitario;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private int numUnidades;

    @Min(0)
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private double costeLinea;

    public LineaCarrito() {
    }

    public LineaCarrito(Carrito carrito, Long idArticulo, double precioUnitario, int numUnidades) {
        this.carrito = carrito;
        this.idArticulo = idArticulo;
        this.precioUnitario = precioUnitario;
        this.numUnidades = numUnidades;
        this.costeLinea = precioUnitario * numUnidades;
    }

    public Long getIdLineaCarrito() {
        return idLineaCarrito;
    }

    public void setIdLineaCarrito(Long idLineaCarrito) {
        this.idLineaCarrito = idLineaCarrito;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Long getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Long idArticulo) {
        this.idArticulo = idArticulo;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.costeLinea = this.precioUnitario * this.numUnidades;
    }

    public int getNumUnidades() {
        return numUnidades;
    }

    public void setNumUnidades(int numUnidades) {
        this.numUnidades = numUnidades;
        this.costeLinea = this.precioUnitario * this.numUnidades;
    }

    public double getCosteLinea() {
        return costeLinea;
    }
}