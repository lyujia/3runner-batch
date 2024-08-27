package com.nhnacademy.batch.entity.purchase;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.nhnacademy.batch.entity.enums.PurchaseStatus;
import com.nhnacademy.batch.entity.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
public class Purchase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull
	@Column(unique = true)
	private UUID orderNumber;

	@NotNull
	private PurchaseStatus status;

	@NotNull
	private int deliveryPrice;

	@NotNull
	private int totalPrice;

	@NotNull
	private ZonedDateTime createdAt;


	@Lob
	@Column(columnDefinition = "TEXT")
	@NotNull
	private String road;

	private String password;

	private ZonedDateTime shippingDate;
	private Boolean isPacking;

	@ManyToOne
	private Member member;



}
