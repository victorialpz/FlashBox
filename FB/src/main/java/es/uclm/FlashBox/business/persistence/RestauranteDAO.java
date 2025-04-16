package es.uclm.FlashBox.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Restaurante;

public interface RestauranteDAO extends JpaRepository<Restaurante, Long> {

}
