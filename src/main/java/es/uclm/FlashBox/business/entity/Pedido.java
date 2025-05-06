package es.uclm.FlashBox.business.entity;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import es.uclm.FlashBox.business.enums.EstadoPedido;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

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
	
	@Enumerated(EnumType.STRING)
	private EstadoPedido estado = EstadoPedido.PENDIENTE_PAGO;


	@ManyToMany
	@JoinTable(
		name = "pedido_items_seleccionados",
		joinColumns = @JoinColumn(name = "pedido_id"),
		inverseJoinColumns = @JoinColumn(name = "items_seleccionados_id")
	)
	private Set<ItemMenu> itemsSeleccionados = new HashSet<>();


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

	public Set<ItemMenu> getItemsSeleccionados() {
		return itemsSeleccionados;
	}

	public void setItemsSeleccionados(Set<ItemMenu> itemsSeleccionados) {
		this.itemsSeleccionados = itemsSeleccionados;
	}

	public ServicioEntrega getServicioEntrega() {
		return servicioEntrega;
	}

	public void setServicioEntrega(ServicioEntrega servicioEntrega) {
		this.servicioEntrega = servicioEntrega;
	}

}
