package com.ahsan.scrap.model;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Data
@Entity
@Setter
@Getter
@Table(name = "order_request")
public class OrderRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String name;
	private String phone;
	private Float quantity;
	private String message;
	@Column(name = "request_date")
	private LocalDateTime requestDate;
	
	public String getRequestedTime() {
	    Duration duration = Duration.between(requestDate, LocalDateTime.now());

	    long hours = duration.toHours();
	    long minutes = duration.toMinutes();

	    if (hours > 0) {
	        return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
	    } else if (minutes > 0) {
	        return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
	    } else {
	        return "Just now";
	    }
	}
}