package com.darglk.onlineshop.model;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

@Entity
@Table(name="shipping")
@Access(AccessType.FIELD)
public class Shipping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="shipping", nullable=false)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal price;
	
	public Shipping() {}
	
	public Shipping(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Shipping [name=" + name + ", price=" + price + "]";
	}
}
