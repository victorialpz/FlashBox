package es.uclm.FlashBox.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Repartidor;

public interface RepartidorDAO extends JpaRepository<Repartidor, Long> {
}