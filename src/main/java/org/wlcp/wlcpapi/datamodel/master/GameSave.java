package org.wlcp.wlcpapi.datamodel.master;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.wlcp.wlcpapi.datamodel.enums.SaveType;

@Entity
@Table(name = "GAME_SAVE")
public class GameSave {

	@Id
	@Column(name = "GAME_SAVE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private int gameSaveId;
	
	@Column(name = "TIME_STAMP")
	@CreationTimestamp
	private Timestamp timeStamp;
	
	@Column(name = "MASTER_GAME_ID")
	private String masterGameId;
	
	@Column(name = "REFERENCE_GAME_ID")
	private String referenceGameId;

	@Column(name = "TYPE")
	@Enumerated(EnumType.ORDINAL)
	private SaveType type;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	public GameSave() {
		
	}

	public GameSave(String masterGameId, String referenceGameId, SaveType type, String description) {
		super();
		this.masterGameId = masterGameId;
		this.referenceGameId = referenceGameId;
		this.type = type;
		this.description = description;
	}

	public int getGameSaveId() {
		return gameSaveId;
	}

	public void setGameSaveId(int gameSaveId) {
		this.gameSaveId = gameSaveId;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMasterGameId() {
		return masterGameId;
	}

	public void setMasterGameId(String masterGameId) {
		this.masterGameId = masterGameId;
	}

	public String getReferenceGameId() {
		return referenceGameId;
	}

	public void setReferenceGameId(String referenceGameId) {
		this.referenceGameId = referenceGameId;
	}

	public SaveType getType() {
		return type;
	}

	public void setType(SaveType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
