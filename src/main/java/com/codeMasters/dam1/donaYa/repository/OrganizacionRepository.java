package com.codeMasters.dam1.donaYa.repository;

import com.codeMasters.dam1.donaYa.model.Organizacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizacionRepository extends JpaRepository<Organizacion, Long> {
    Optional<Organizacion> findByEmail(String email);
    List<Organizacion> findByZonaCobertura(String zona);
    boolean existsByEmail(String email);
}


//CLASE SUPLENTE INTERFAZ CUSTOM (SE PUEDE IMPLEMENTAR O NO).
/*
* import com.donaciones.model.Donacion;
import java.util.List;

/**
 * Interfaz para consultas personalizadas que no cubre Spring Data directamente.

public interface DonacionRepositoryCustom {

    // Devuelve donaciones disponibles filtradas opcionalmente por tipo y talla.
    List<Donacion> filtrarDisponibles(String tipoRopa, String talla);

    /** Estadísticas resumidas del inventario.
    long contarPorEstado(String estado);
}
*/

//implementacion del metodo

/*import com.donaciones.model.Donacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * Implementación de consultas personalizadas con JPQL dinámico.

@Repository
public class DonacionRepositoryImpl implements DonacionRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Donacion> filtrarDisponibles(String tipoRopa, String talla) {
        StringBuilder jpql = new StringBuilder(
                "SELECT d FROM Donacion d WHERE d.estadoDonacion = 'DISPONIBLE'");

        if (tipoRopa != null && !tipoRopa.isBlank()) {
            jpql.append(" AND LOWER(d.tipoRopa) LIKE LOWER(:tipo)");
        }
        if (talla != null && !talla.isBlank()) {
            jpql.append(" AND LOWER(d.talla) = LOWER(:talla)");
        }
        jpql.append(" ORDER BY d.fechaPublicacion DESC");

        TypedQuery<Donacion> query = em.createQuery(jpql.toString(), Donacion.class);

        if (tipoRopa != null && !tipoRopa.isBlank()) {
            query.setParameter("tipo", "%" + tipoRopa + "%");
        }
        if (talla != null && !talla.isBlank()) {
            query.setParameter("talla", talla);
        }

        return query.getResultList();
    }

    @Override
    public long contarPorEstado(String estado) {
        return em.createQuery(
                        "SELECT COUNT(d) FROM Donacion d WHERE d.estadoDonacion = :estado", Long.class)
                .setParameter("estado", estado)
                .getSingleResult();
    }
}
 */