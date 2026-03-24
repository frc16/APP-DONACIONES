package com.codeMasters.dam1.donaYa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa una donación de ropa registrada en el inventario.
 * Es la entidad central del sistema: puede estar DISPONIBLE,
 * RESERVADA (asignada a una org.) o ENTREGADA.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "donaciones")
public class Donacion {

    // ── Enumeraciones internas ────────────────────────────────────────────────

    public enum EstadoRopa {
        NUEVA, BUEN_ESTADO, REGULAR
    }

    public enum EstadoDonacion {
        DISPONIBLE, RESERVADA, ENTREGADA
    }

    // ── Campos ────────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donante_id", nullable = false)
    private Donante donante;

    @NotBlank(message = "El tipo de ropa es obligatorio")
    @Column(name = "tipo_ropa", nullable = false, length = 100)
    private String tipoRopa;

    @Column(length = 20)
    private String talla;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private int cantidad = 1;

    @NotNull(message = "El estado de la ropa es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ropa", nullable = false, length = 50)
    private EstadoRopa estadoRopa;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_donacion", nullable = false, length = 30)
    private EstadoDonacion estadoDonacion = EstadoDonacion.DISPONIBLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacion_id")
    private Organizacion organizacion;   // null hasta que una org. la solicite

    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // ── Lifecycle hooks ───────────────────────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.fechaPublicacion = LocalDateTime.now();
        this.estadoDonacion   = EstadoDonacion.DISPONIBLE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ── Constructor de conveniencia ───────────────────────────────────────────

    public Donacion(Donante donante, String tipoRopa, String talla,
                    int cantidad, EstadoRopa estadoRopa, String descripcion) {
        this.donante    = donante;
        this.tipoRopa   = tipoRopa;
        this.talla      = talla;
        this.cantidad   = cantidad;
        this.estadoRopa = estadoRopa;
        this.descripcion = descripcion;
    }

    /** Marca la donación como reservada y la asigna a una organización. */
    public void reservar(Organizacion org) {
        this.organizacion   = org;
        this.estadoDonacion = EstadoDonacion.RESERVADA;
    }

    /** Marca la donación como entregada definitivamente. */
    public void entregar() {
        this.estadoDonacion = EstadoDonacion.ENTREGADA;
    }
}
