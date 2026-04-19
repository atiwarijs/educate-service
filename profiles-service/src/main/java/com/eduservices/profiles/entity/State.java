package com.eduservices.profiles.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "states")
public class State extends BaseEntity{

	@Column(name = "state_name", nullable = true)
	private String name;

	@OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
	private List<City> cities = new ArrayList<>();
}
