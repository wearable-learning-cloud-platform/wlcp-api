package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.wlcp.wlcpapi.datamodel.master.Game;
import org.wlcp.wlcpapi.datamodel.master.connection.Connection;

/**
 * Entity implementation class for Entity: OutputState
 *
 */
@Entity
@Table(name = "OUTPUT_STATE")
@PrimaryKeyJoinColumn(referencedColumnName = "STATE_ID")
public class OutputState extends State implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ElementCollection()
    @CollectionTable(name = "DISPLAY_TEXT_MAP")
    @MapKeyColumn(name = "SCOPE")
    @Column(name = "DISPLAY_TEXT", length = 2048)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Map<String, String> displayText = new HashMap<String, String>();
	
	@ElementCollection()
    @CollectionTable(name = "PICTURE_OUTPUT")
    @MapKeyColumn(name = "SCOPE")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Map<String, PictureOutput> pictureOutputs = new HashMap<String, PictureOutput>();

	public OutputState() {
		super();
		setStateType(StateType.OUTPUT_STATE);
	}
	
	public OutputState(String stateId, Game game, StateType stateType, Float positionX, Float positionY, List<Connection> inputConnections, List<Connection> outputConnections, String description, Map<String, String> displayText, Map<String, PictureOutput> pictureOutputs) {
		super(stateId, game, stateType, positionX, positionY, inputConnections, outputConnections);
		this.description = description;
		this.displayText = displayText;
		this.pictureOutputs = pictureOutputs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getDisplayText() {
		return displayText;
	}

	public void setDisplayText(Map<String, String> displayText) {
		this.displayText = displayText;
	}

	public Map<String, PictureOutput> getPictureOutputs() {
		return pictureOutputs;
	}

	public void setPictureOutputs(Map<String, PictureOutput> pictureOutputs) {
		this.pictureOutputs = pictureOutputs;
	}

}
