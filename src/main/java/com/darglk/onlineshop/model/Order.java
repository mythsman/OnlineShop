package com.darglk.onlineshop.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="shipping_id")
	private Shipping shipping;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status", nullable=false)
	private OrderStatus status;

	@OneToMany(mappedBy="order")
	private List<OrderDetails> orderDetails;
	
	@Transient
	private BigDecimal totalWithShipping;
	
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

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
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
	
	public BigDecimal getTotalWithShipping() {
		this.totalWithShipping = new BigDecimal(this.total.doubleValue() + this.shipping.getPrice().doubleValue());
		return this.totalWithShipping.setScale(2, RoundingMode.HALF_UP);
	}

	public Order(BigDecimal total, Shipping shipping, Date createdAt, User user, OrderStatus status) {
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
