
package com.api.apartamentos.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.apartamentos.models.ReservaModel;
import com.api.apartamentos.models.DisponibilidadModel;
import com.api.apartamentos.models.UnidadRentableModel;
import com.api.apartamentos.models.ClienteModel;

import com.api.apartamentos.repositories.IReservaRepository;
import com.api.apartamentos.repositories.IDisponibilidadRepository;
import com.api.apartamentos.repositories.IUnidadRentableRepository;
import com.api.apartamentos.repositories.IClienteRepository;

@Service
public class ReservaService extends BaseCrudService<ReservaModel> {

    private final IReservaRepository reservaRepository;
    private final IDisponibilidadRepository disponibilidadRepository;
    private final IUnidadRentableRepository unidadRepository;
    private final IClienteRepository clienteRepository;

    public ReservaService(
            IReservaRepository reservaRepository,
            IDisponibilidadRepository disponibilidadRepository,
            IUnidadRentableRepository unidadRepository,
            IClienteRepository clienteRepository) {

        super(reservaRepository);
        this.reservaRepository = reservaRepository;
        this.disponibilidadRepository = disponibilidadRepository;
        this.unidadRepository = unidadRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public ReservaModel save(ReservaModel reserva) {

        if (reserva.getIdReservacion() != null) {
            return actualizarReserva(reserva);
        }

        return crearReserva(reserva);
    }

    private ReservaModel crearReserva(ReservaModel reserva) {

        if (!"PENDIENTE".equals(reserva.getEstado())) {
            throw new IllegalArgumentException(
                    "Una reservacion nueva debe iniciar como PENDIENTE");
        }

        if (reserva.getUnidad() == null ||
                reserva.getUnidad().getIdUnidad() == null) {
            throw new IllegalArgumentException(
                    "La unidad es obligatoria");
        }

        if (reserva.getCliente() == null ||
                reserva.getCliente().getId() == null) {
            throw new IllegalArgumentException(
                    "El cliente es obligatorio");
        }

        if (reserva.getFechaEntrada() == null ||
                reserva.getFechaSalida() == null ||
                !reserva.getFechaSalida().isAfter(reserva.getFechaEntrada())) {
            throw new IllegalArgumentException(
                    "El rango de fechas no es valido");
        }

        Long unidadId = reserva.getUnidad().getIdUnidad();

        UnidadRentableModel unidad = unidadRepository.findById(unidadId)
                .orElseThrow(() ->
                        new IllegalArgumentException("La unidad no existe"));

        ClienteModel cliente = clienteRepository.findById(
                reserva.getCliente().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("El cliente no existe"));

        if (!"DISPONIBLE".equals(unidad.getEstado())) {
            throw new IllegalArgumentException(
                    "La unidad no esta disponible");
        }

        List<DisponibilidadModel> periodos =
                disponibilidadRepository.findByUnidad_IdUnidad(unidadId);

        DisponibilidadModel periodoAplicable = periodos.stream()
                .filter(d -> Boolean.TRUE.equals(d.getDisponible()))
                .filter(d -> !d.getFechaInicio().isAfter(reserva.getFechaEntrada()))
                .filter(d -> !d.getFechaFin().isBefore(reserva.getFechaSalida()))
                .max(Comparator.comparing(DisponibilidadModel::getFechaInicio))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La unidad no tiene disponibilidad en las fechas solicitadas"));

        boolean bloqueada = periodos.stream()
                .filter(d -> Boolean.FALSE.equals(d.getDisponible()))
                .anyMatch(d ->
                        d.getFechaInicio().isBefore(reserva.getFechaSalida()) &&
                        d.getFechaFin().isAfter(reserva.getFechaEntrada()));

        if (bloqueada) {
            throw new IllegalArgumentException(
                    "La unidad tiene fechas bloqueadas");
        }

        if (reservaRepository.existeSolapamiento(
                unidadId,
                null,
                reserva.getFechaEntrada(),
                reserva.getFechaSalida())) {
            throw new IllegalArgumentException(
                    "La unidad ya esta reservada en ese rango");
        }

        BigDecimal precio = periodoAplicable.getPrecioEspecial();

        if (precio == null) {
            precio = unidad.getPrecioNoche();
        }

        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException(
                    "La unidad no tiene un precio valido");
        }

        long noches = ChronoUnit.DAYS.between(
                reserva.getFechaEntrada().toLocalDate(),
                reserva.getFechaSalida().toLocalDate());

        noches = Math.max(1, noches);

        reserva.setImporteTotal(
                precio.multiply(BigDecimal.valueOf(noches)));

        reserva.setUnidad(unidad);
        reserva.setCliente(cliente);

        return reservaRepository.save(reserva);
    }

    private ReservaModel actualizarReserva(ReservaModel datos) {

        ReservaModel existente = reservaRepository.findById(
                datos.getIdReservacion())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La reservacion no existe"));

        String anterior = existente.getEstado();
        String nuevo = datos.getEstado();

        if (nuevo == null || nuevo.isBlank()) {
            throw new IllegalArgumentException(
                    "El estado es obligatorio");
        }

        if (!anterior.equals(nuevo)) {

            boolean permitido =
                    ("PENDIENTE".equals(anterior) &&
                     ("CONFIRMADA".equals(nuevo) ||
                      "CANCELADA".equals(nuevo)))

                    ||

                    ("CONFIRMADA".equals(anterior) &&
                     ("FINALIZADA".equals(nuevo) ||
                      "CANCELADA".equals(nuevo)));

            if (!permitido) {
                throw new IllegalArgumentException(
                        "Transicion de reservacion no permitida: "
                                + anterior + " -> " + nuevo);
            }

            if ("FINALIZADA".equals(nuevo) &&
                    existente.getFechaSalida().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException(
                        "La estancia todavia no ha terminado");
            }

            existente.setEstado(nuevo);
        }

        if (datos.getComentario() != null) {
            existente.setComentario(datos.getComentario());
        }

        return reservaRepository.save(existente);
    }
}

