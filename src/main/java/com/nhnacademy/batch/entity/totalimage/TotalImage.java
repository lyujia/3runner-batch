package com.nhnacademy.batch.entity.totalimage;

import com.nhnacademy.batch.entity.bookimage.BookImage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class TotalImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Getter
	@NotBlank
	@Column(unique = true)
	@Size(max = 40)
	private String url;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "totalImage", cascade = CascadeType.ALL)
	private BookImage bookImage;

	public TotalImage(String url) {
		this.url = url;
	}
}
