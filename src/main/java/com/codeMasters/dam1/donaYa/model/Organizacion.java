package com.codeMasters.dam1.donaYa.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una organización de trabajo social que gestiona y solicita
 * donaciones de ropa a través de la plataforma.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "organizaciones")
@DiscriminatorValue("ORGANIZACION")
@PrimaryKeyJoinColumn(name = "id")
public class Organizacion extends Usuario {

    @Column(name = "nombre_org", nullable = false, length = 200)
    private String nombreOrg;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "zona_cobertura", length = 100)
    private String zonaCobertura;

    @OneToMany(mappedBy = "organizacion", fetch = FetchType.LAZY)
    private List<Donacion> donacionesSolicitadas = new ArrayList<>();

    public Organizacion(String nombre, String email, String telefono,
                        String direccion, String nombreOrg,
                        String descripcion, String zonaCobertura) {
        setNombre(nombre);
        setEmail(email);
        setTelefono(telefono);
        setDireccion(direccion);
        this.nombreOrg    = nombreOrg;
        this.descripcion  = descripcion;
        this.zonaCobertura = zonaCobertura;
    }

    @Override
    public String getTipoUsuario() {
        return "ORGANIZACION";
    }
}

