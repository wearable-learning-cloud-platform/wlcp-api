package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TimerDuration implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	@Column(name = "DURATION")
	private Integer duration;
	
	public TimerDuration() {
		
	}

	public TimerDuration(Integer duration) {
		super();
		this.duration = duration;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
}
