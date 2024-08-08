package com.ahsan.scrap.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Setter
@Getter
@Table(name = "sell")
public class Sell {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "sell_date")
	private LocalDateTime sellDate;
	@Column(name="number_of_items")
	private int numberOfItems;
	@Column(name = "sell_amount")
	private Float sellAmount;
	@Column(name = "sell_quantity")
	private Float sellQuantity;
	@Column(name = "sell_receipt")
	private String sellReceipt;
	
	@ManyToOne
	@JoinColumn(name = "user_dtls_id")
    private UserDtls userDtls;

    @OneToMany(mappedBy = "sell", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellItem> sellItems = new ArrayList<>();
}
