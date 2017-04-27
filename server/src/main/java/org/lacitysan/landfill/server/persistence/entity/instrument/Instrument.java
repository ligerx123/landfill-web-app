package org.lacitysan.landfill.server.persistence.entity.instrument;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.lacitysan.landfill.server.persistence.entity.system.Trackable;
import org.lacitysan.landfill.server.persistence.entity.user.User;
import org.lacitysan.landfill.server.persistence.enums.instrument.InstrumentStatus;
import org.lacitysan.landfill.server.persistence.enums.location.Site;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Alvin Quach
 */
@Entity
@Table(name="dbo.Instruments")
@JsonInclude(Include.NON_NULL)
public class Instrument implements Trackable {
	
	@Id
	@Column(name="InstrumentPK")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String serialNumber;
	
	@JsonIgnoreProperties(value="instruments", allowSetters=true)
	@NotNull
	@ManyToOne
	@JoinColumn(name="InstrumentTypeFK")
	private InstrumentType instrumentType;
	
	@NotNull
	@Column(name="InstrumentStatusString")
	@Enumerated(EnumType.STRING)
	private InstrumentStatus instrumentStatus;
	
	private Timestamp serviceDueDate;
	
	private Timestamp lastServiceDate;
	
	private Timestamp purchaseDate;
	
	@Column(name="SiteString")
	@Enumerated(EnumType.STRING)
	private Site site;
	
	private String inventoryNumber;
	
	private String description;
	
	@JsonIgnoreProperties(value={"userGroups", "enabled"}, allowSetters=true)
	@ManyToOne
	@JoinColumn(name="CreatedByFK")
	private User createdBy;
	
	private Timestamp createdDate;
	
	@JsonIgnoreProperties(value={"userGroups", "enabled"}, allowSetters=true)
	@ManyToOne
	@JoinColumn(name="ModifiedByFK")
	private User modifiedBy;
	
	private Timestamp modifiedDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public InstrumentType getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(InstrumentType instrumentType) {
		this.instrumentType = instrumentType;
	}

	public InstrumentStatus getInstrumentStatus() {
		return instrumentStatus;
	}

	public void setInstrumentStatus(InstrumentStatus instrumentStatus) {
		this.instrumentStatus = instrumentStatus;
	}

	public Timestamp getServiceDueDate() {
		return serviceDueDate;
	}

	public void setServiceDueDate(Timestamp serviceDueDate) {
		this.serviceDueDate = serviceDueDate;
	}

	public Timestamp getLastServiceDate() {
		return lastServiceDate;
	}

	public void setLastServiceDate(Timestamp lastServiceDate) {
		this.lastServiceDate = lastServiceDate;
	}

	public Timestamp getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Timestamp purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public User getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	@Override
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public User getModifiedBy() {
		return modifiedBy;
	}

	@Override
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	@Override
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
}
