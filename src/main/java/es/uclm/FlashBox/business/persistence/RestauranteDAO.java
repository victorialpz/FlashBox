package es.uclm.FlashBox.business.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Restaurante;

public interface RestauranteDAO extends JpaRepository<Restaurante, Long> {
	List<Restaurante> findByTipo(String tipo);

	List<Restaurante> findByNombreContainingIgnoreCaseAndTipoContainingIgnoreCase(String nombre, String tipo);

}
