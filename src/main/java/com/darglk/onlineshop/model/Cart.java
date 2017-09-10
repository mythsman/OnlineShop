package com.darglk.onlineshop.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="cart")
@Access(AccessType.FIELD)
public class Cart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="cart", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<LineItem> lineItems;
	
	public Cart() {
		lineItems = new TreeSet<LineItem>();
	}

	public Cart(Long id, Set<LineItem> lineItems) {
		this.id = id;
		this.lineItems = lineItems;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<LineItem> lineItems) {
		this.lineItems = lineItems;
	}
}
