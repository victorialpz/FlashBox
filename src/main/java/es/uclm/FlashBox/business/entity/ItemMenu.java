package es.uclm.FlashBox.business.entity;

import es.uclm.FlashBox.business.enums.TipoItemMenu;
import jakarta.persistence.*;

@Entity
public class ItemMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	private Double precio;

	@Enumerated(EnumType.STRING)
	private TipoItemMenu tipo;

	@ManyToOne
	private CartaMenu cartaMenu;
	
	private int stock;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public TipoItemMenu getTipo() {
		return tipo;
	}

	public void setTipo(TipoItemMenu tipo) {
		this.tipo = tipo;
	}

	public CartaMenu getCartaMenu() {
		return cartaMenu;
	}

	public void setCartaMenu(CartaMenu cartaMenu) {
		this.cartaMenu = cartaMenu;
	}
	public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}
