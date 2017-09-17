package com.darglk.onlineshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="total", nullable=false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal total;
	
	@Column(name="shipping", nullable=false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal shipping;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable=false)
	private OrderStatus status;

	@OneToMany(mappedBy="order")
	@OrderBy("created_at")
	private List<OrderDetails> orderDetails;
	
	public List<OrderDetails> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getShipping() {
		return shipping;
	}

	public void setShipping(BigDecimal shipping) {
		this.shipping = shipping;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Order(BigDecimal total, BigDecimal shipping, Date createdAt, User user, OrderStatus status) {
		this.orderDetails = new ArrayList<OrderDetails>();
		this.total = total;
		this.shipping = shipping;
		this.createdAt = createdAt;
		this.user = user;
		this.status = status;
	}
	
	public Order() {
		this.orderDetails = new ArrayList<OrderDetails>();
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", total=" + total + ", shipping=" + shipping + ", createdAt=" + createdAt
				+ ", user=" + user + ", status=" + status + "]";
	}
}
