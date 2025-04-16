package es.uclm.FlashBox.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.CartaMenu;

public interface CartaMenuDAO extends JpaRepository<CartaMenu, Long>  {

}
