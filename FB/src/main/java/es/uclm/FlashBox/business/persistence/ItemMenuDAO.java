package es.uclm.FlashBox.business.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.FlashBox.business.entity.ItemMenu;

public interface ItemMenuDAO extends JpaRepository<ItemMenu, Long>{

}
