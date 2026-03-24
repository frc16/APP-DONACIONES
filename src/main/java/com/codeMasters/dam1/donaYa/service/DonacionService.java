package com.codeMasters.dam1.donaYa.service;

import com.codeMasters.dam1.donaYa.exception.DonacionNoDisponibleException;
import com.codeMasters.dam1.donaYa.exception.RecursoNoEncontradoException;
import com.codeMasters.dam1.donaYa.model.Donacion;
import com.codeMasters.dam1.donaYa.model.Donante;
import com.codeMasters.dam1.donaYa.model.Organizacion;
import com.codeMasters.dam1.donaYa.repository.DonacionRepository;
import com.codeMasters.dam1.donaYa.repository.DonanteRepository;
import com.codeMasters.dam1.donaYa.repository.OrganizacionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonacionService {

    private final DonacionRepository donacionRepo;
    private final DonanteRepository donanteRepo;
    private final OrganizacionRepository orgRepo;
    private final DonacionRepositoryImpl  donacionCustomRepo;

    // ── Crear ─────────────────────────────────────────────────────────────────

    @Transactional
    public Donacion crear(Long donanteId, String tipoRopa, String talla,
                          int cantidad, Donacion.EstadoRopa estadoRopa,
                          String descripcion) {

        Donante donante = donanteRepo.findById(donanteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Donante no encontrado: " + donanteId));

        Donacion donacion = new Donacion(donante, tipoRopa, talla, cantidad, estadoRopa, descripcion);
        Donacion guardada = donacionRepo.save(donacion);

        donante.incrementarTotalDonaciones();
        donanteRepo.save(donante);

        return guardada;
    }

    // ── Leer ──────────────────────────────────────────────────────────────────

    public List<Donacion> listarTodas() {
        return donacionRepo.findAll();
    }

    public List<Donacion> listarDisponibles() {
        return donacionRepo.findByEstadoDonacion(EstadoDonacion.DISPONIBLE);
    }

    public List<Donacion> filtrar(String tipoRopa, String talla) {
        return donacionCustomRepo.filtrarDisponibles(tipoRopa, talla);
    }

    public Donacion buscarPorId(Long id) {
        return donacionRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Donación no encontrada: " + id));
    }

    public List<Donacion> listarPorDonante(Long donanteId) {
        return donacionRepo.findByDonanteId(donanteId);
    }

    public List<Donacion> listarPorOrganizacion(Long orgId) {
        return donacionRepo.findByOrganizacionId(orgId);
    }

    // ── Actualizar ────────────────────────────────────────────────────────────

    @Transactional
    public Donacion actualizar(Long id, String tipoRopa, String talla,
                               int cantidad, Donacion.EstadoRopa estadoRopa,
                               String descripcion) {
        Donacion d = buscarPorId(id);
        if (d.getEstadoDonacion() == EstadoDonacion.ENTREGADA) {
            throw new DonacionNoDisponibleException(id);
        }
        d.setTipoRopa(tipoRopa);
        d.setTalla(talla);
        d.setCantidad(cantidad);
        d.setEstadoRopa(estadoRopa);
        d.setDescripcion(descripcion);
        return donacionRepo.save(d);
    }

    // ── Solicitar (organización reserva una donación) ─────────────────────────

    @Transactional
    public Donacion solicitar(Long donacionId, Long orgId) {
        Donacion donacion = buscarPorId(donacionId);
        if (donacion.getEstadoDonacion() != EstadoDonacion.DISPONIBLE) {
            throw new DonacionNoDisponibleException(donacionId);
        }
        Organizacion org = orgRepo.findById(orgId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Organización no encontrada: " + orgId));

        donacion.reservar(org);
        return donacionRepo.save(donacion);
    }

    // ── Marcar como entregada ─────────────────────────────────────────────────

    @Transactional
    public Donacion marcarEntregada(Long id) {
        Donacion donacion = buscarPorId(id);
        donacion.entregar();
        return donacionRepo.save(donacion);
    }

    // ── Eliminar ──────────────────────────────────────────────────────────────

    @Transactional
    public void eliminar(Long id) {
        Donacion d = buscarPorId(id);
        donacionRepo.delete(d);
    }

    // ── Estadísticas ──────────────────────────────────────────────────────────

    public long totalDisponibles()  { return donacionRepo.countByEstadoDonacion(Donacion.EstadoDonacion.DISPONIBLE); }
    public long totalReservadas()   { return donacionRepo.countByEstadoDonacion(Donacion.EstadoDonacion.RESERVADA); }
    public long totalEntregadas()   { return donacionRepo.countByEstadoDonacion(Donacion.EstadoDonacion.ENTREGADA); }
}