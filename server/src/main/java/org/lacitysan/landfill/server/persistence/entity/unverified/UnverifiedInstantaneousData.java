package org.lacitysan.landfill.server.persistence.entity.unverified;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.lacitysan.landfill.server.config.constant.ApplicationProperty;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.IMENumber;
import org.lacitysan.landfill.server.persistence.entity.instantaneous.WarmspotData;
import org.lacitysan.landfill.server.persistence.entity.instrument.Instrument;
import org.lacitysan.landfill.server.persistence.enums.MonitoringPoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Alvin Quach
 */
@Entity
@Table(name=ApplicationProperty.DATABASE_NAME + ".dbo.UnverifiedInstantaneousData")
@JsonInclude(Include.NON_NULL)
public class UnverifiedInstantaneousData {
	
	@Id
	@Column(name="UnverifiedInstantaneousPK")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
		
	@Column(name="MonitoringPointString")
	@Enumerated(EnumType.STRING)
	private MonitoringPoint monitoringPoint;
		
	@ManyToOne
	@JoinColumn(name="InstrumentFK")
	private Instrument instrument;

	@NotNull
	private Integer methaneLevel;	
	
	@NotNull
	private Timestamp startTime;
	
	@NotNull
	private Timestamp endTime;
	
	@JsonIgnoreProperties({"unverifiedInstantaneousData", "monitoringPoints", "instantaneousData", "imeData", "imeRepairData"})
	@ManyToMany
	@JoinTable(name="test.dbo.UnverifiedInstantaneousDataXRefIMENumbers", joinColumns=@JoinColumn(name="UnverifiedInstantaneousFK"), inverseJoinColumns=@JoinColumn(name="IMENumberFK"))
	private Set<IMENumber> imeNumbers = new HashSet<>();
	
	@JsonIgnoreProperties({"unverifiedInstantaneousData", "instantaneousData"})
	@ManyToMany
	@JoinTable(name="test.dbo.UnverifiedInstantaneousDataXRefWarmspotData", joinColumns=@JoinColumn(name="UnverifiedInstantaneousFK"), inverseJoinColumns=@JoinColumn(name="WarmspotFK"))
	private Set<WarmspotData> warmspotData = new HashSet<>();
	
	@JsonIgnoreProperties({"unverifiedInstantaneousData"})
	@Cascade(CascadeType.ALL)
	@ManyToOne
	@JoinColumn(name="UnverifiedDataSetFK", nullable=false)
	private UnverifiedDataSet unverifiedDataSet;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MonitoringPoint getMonitoringPoint() {
		return monitoringPoint;
	}

	public void setMonitoringPoint(MonitoringPoint monitoringPoint) {
		this.monitoringPoint = monitoringPoint;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public Integer getMethaneLevel() {
		return methaneLevel;
	}

	public void setMethaneLevel(Integer methaneLevel) {
		this.methaneLevel = methaneLevel;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Set<IMENumber> getImeNumbers() {
		return imeNumbers;
	}

	public void setImeNumbers(Set<IMENumber> imeNumbers) {
		this.imeNumbers = imeNumbers;
	}

	public Set<WarmspotData> getWarmspotData() {
		return warmspotData;
	}

	public void setWarmspotData(Set<WarmspotData> warmspotData) {
		this.warmspotData = warmspotData;
	}

	public UnverifiedDataSet getUnverifiedDataSet() {
		return unverifiedDataSet;
	}

	public void setUnverifiedDataSet(UnverifiedDataSet unverifiedDataSet) {
		this.unverifiedDataSet = unverifiedDataSet;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
}
