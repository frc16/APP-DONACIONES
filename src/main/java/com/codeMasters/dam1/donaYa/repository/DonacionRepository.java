package com.codeMasters.dam1.donaYa.repository;

import com.codeMasters.dam1.donaYa.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio JPA para el inventario de donaciones.
 * Spring Data genera la implementación automáticamente.
 */
public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    List<Donacion> findByEstadoDonacion(Donacion.EstadoDonacion estado);

    List<Donacion> findByDonanteId(Long donanteId);

    List<Donacion> findByOrganizacionId(Long organizacionId);

    @Query("SELECT d FROM Donacion d WHERE d.tipoRopa LIKE %:tipo% AND d.estadoDonacion = 'DISPONIBLE'")
    List<Donacion> buscarDisponiblesPorTipo(@Param("tipo") String tipo);

    long countByEstadoDonacion(Donacion.EstadoDonacion estado);
}
