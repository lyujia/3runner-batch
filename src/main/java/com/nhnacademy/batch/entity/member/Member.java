package com.nhnacademy.batch.entity.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.nhnacademy.batch.entity.purchase.Purchase;
import com.nhnacademy.batch.entity.member.enums.Grade;
import com.nhnacademy.batch.entity.member.enums.Status;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 6, max = 255)
	private String password;

	@NotNull
	private Long point;

	@NotNull
	@Size(min = 1, max = 255)
	private String name;

	private int age;

	@NotNull
	@Size(min = 1, max = 11)
	private String phone;

	@NotNull
	@Column(unique = true)
	private String email;

	private ZonedDateTime birthday;

	@NotNull
	private Grade grade;

	@NotNull
	private Status status;

	private ZonedDateTime lastLoginDate;

	@NotNull
	private ZonedDateTime createdAt;

	private ZonedDateTime modifiedAt;
	private ZonedDateTime deletedAt;


	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Purchase> purchaseList = new ArrayList<>();

}
