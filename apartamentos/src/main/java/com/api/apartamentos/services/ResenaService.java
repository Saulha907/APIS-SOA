
package com.api.apartamentos.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.apartamentos.models.ResenaModel;
import com.api.apartamentos.models.ReservaModel;
import com.api.apartamentos.repositories.IResenaRepository;
import com.api.apartamentos.repositories.IReservaRepository;

@Service
public class ResenaService extends BaseCrudService<ResenaModel> {

    private final IResenaRepository resenaRepository;
    private final IReservaRepository reservaRepository;

    public ResenaService(
        IResenaRepository resenaRepository,
        IReservaRepository reservaRepository
    ) {
        super(resenaRepository);
        this.resenaRepository = resenaRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<ResenaModel> findByReserva(Long id) {
        return resenaRepository.findByReserva_IdReservacion(id);
    }

    @Override
    @Transactional
    public ResenaModel save(ResenaModel resena) {

        if (resena.getReserva() == null ||
            resena.getReserva().getIdReservacion() == null) {
            throw new IllegalArgumentException("La reservacion es obligatoria");
        }

        if (resena.getCliente() == null ||
            resena.getCliente().getId() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (resena.getCalificacionGeneral() < 1 ||
            resena.getCalificacionGeneral() > 5) {
            throw new IllegalArgumentException(
                "La calificacion general debe estar entre 1 y 5"
            );
        }

        if (resena.getCalificacionComunicacion() < 1 ||
            resena.getCalificacionComunicacion() > 5) {
            throw new IllegalArgumentException(
                "La calificacion de comunicacion debe estar entre 1 y 5"
            );
        }

        ReservaModel reserva = reservaRepository.findById(
            resena.getReserva().getIdReservacion()
        ).orElseThrow(() ->
            new IllegalArgumentException("La reservacion no existe")
        );

        if (!reserva.getCliente().getId().equals(
            resena.getCliente().getId()
        )) {
            throw new IllegalArgumentException(
                "El cliente no pertenece a esta reservacion"
            );
        }

        if (!"FINALIZADA".equals(reserva.getEstado())) {
            throw new IllegalArgumentException(
                "Solo se pueden calificar reservaciones finalizadas"
            );
        }

        if (reserva.getFechaSalida() == null ||
            reserva.getFechaSalida().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                "La estancia todavia no ha terminado"
            );
        }

        if (resena.getIdResena() == null) {

            if (resenaRepository.existsByReserva_IdReservacion(
                reserva.getIdReservacion()
            )) {
                throw new IllegalArgumentException(
                    "Esta reservacion ya tiene una resena"
                );
            }

            resena.setReserva(reserva);
            resena.setCliente(reserva.getCliente());

            return resenaRepository.save(resena);
        }

        ResenaModel existente = resenaRepository.findById(
            resena.getIdResena()
        ).orElseThrow(() ->
            new IllegalArgumentException("La resena no existe")
        );

        if (!existente.getReserva().getIdReservacion().equals(
            reserva.getIdReservacion()
        )) {
            throw new IllegalArgumentException(
                "No se puede cambiar la reservacion de una resena"
            );
        }

        existente.setCalificacionGeneral(
            resena.getCalificacionGeneral()
        );
        existente.setCalificacionComunicacion(
            resena.getCalificacionComunicacion()
        );
        existente.setComentario(resena.getComentario());

        return resenaRepository.save(existente);
    }
}
