package es.uclm.FlashBox.business.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private Restaurante restaurante;

	private String direccionEntrega;
	private boolean pagado;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemMenu> itemsSeleccionados;

	@OneToOne(cascade = CascadeType.ALL)
	private ServicioEntrega servicioEntrega;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante restaurante) {
		this.restaurante = restaurante;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}

	public List<ItemMenu> getItemsSeleccionados() {
		return itemsSeleccionados;
	}

	public void setItemsSeleccionados(List<ItemMenu> itemsSeleccionados) {
		this.itemsSeleccionados = itemsSeleccionados;
	}

	public ServicioEntrega getServicioEntrega() {
		return servicioEntrega;
	}

	public void setServicioEntrega(ServicioEntrega servicioEntrega) {
		this.servicioEntrega = servicioEntrega;
	}

}
