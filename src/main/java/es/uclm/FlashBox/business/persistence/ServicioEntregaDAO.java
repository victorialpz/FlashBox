package es.uclm.FlashBox.business.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Repartidor;
import es.uclm.FlashBox.business.entity.ServicioEntrega;

public interface ServicioEntregaDAO extends JpaRepository<ServicioEntrega, Long> {
	List<ServicioEntrega> findByRepartidor(Repartidor repartidor);
}
