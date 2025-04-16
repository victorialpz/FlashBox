package es.uclm.FlashBox.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.Pedido;

public interface PedidoDAO extends JpaRepository<Pedido, Long> {

}
