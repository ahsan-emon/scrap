package com.ahsan.scrap.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
@Table(name = "product")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String name;
    @Column(name = "short_name")
	private String shortName;
    @Column(name = "unit_price")
    private Float unitPrice;
	private Float quantity;
	@Column(name = "update_date")
	private LocalDateTime updateDate;
	
	public String getLastUpdatedTime() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(updateDate, now);

        long years = ChronoUnit.YEARS.between(updateDate, now);
        if (years > 0) {
            return "Last updated " + years + (years == 1 ? " year ago" : " years ago");
        }

        long months = ChronoUnit.MONTHS.between(updateDate, now);
        if (months > 0) {
            return "Last updated " + months + (months == 1 ? " month ago" : " months ago");
        }

        long weeks = ChronoUnit.WEEKS.between(updateDate, now);
        if (weeks > 0) {
            return "Last updated " + weeks + (weeks == 1 ? " week ago" : " weeks ago");
        }

        long days = ChronoUnit.DAYS.between(updateDate, now);
        if (days > 0) {
            return "Last updated " + days + (days == 1 ? " day ago" : " days ago");
        }

        long hours = duration.toHours();
        if (hours > 0) {
            return "Last updated " + hours + (hours == 1 ? " hour ago" : " hours ago");
        }

        long minutes = duration.toMinutes();
        if (minutes > 0) {
            return "Last updated " + minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }

        return "Last updated just now";
    }
}
