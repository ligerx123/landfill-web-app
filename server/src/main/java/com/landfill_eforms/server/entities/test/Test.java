package com.landfill_eforms.server.entities.test;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Alvin Quach
 */
@Entity
@Table(name="test.dbo.Test")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class Test {

	@Id
	@Column(name="TestPK")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private Integer value;
	
	@ManyToMany(fetch = FetchType.EAGER)
	//@JsonManagedReference
	//@LazyCollection(LazyCollectionOption.FALSE)
	@JoinTable(name="test.dbo.SleepTest", joinColumns=@JoinColumn(name="TestFK"), inverseJoinColumns=@JoinColumn(name="SleepFK"))
	private Set<Sleep> sleeps;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Set<Sleep> getSleeps() {
		return sleeps;
	}

	public void setSleeps(Set<Sleep> sleeps) {
		this.sleeps = sleeps;
	}
	
}