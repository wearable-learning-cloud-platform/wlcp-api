package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Randoms implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "RANDOM_ENABLED")
	private Boolean randomEnabled;
	
	public Randoms() {
		super();
	}

	public Randoms(Boolean randomEnabled) {
		super();
		this.randomEnabled = randomEnabled;
	}

	public Boolean getRandomEnabled() {
		return randomEnabled;
	}

	public void setRandomEnabled(Boolean randomEnabled) {
		this.randomEnabled = randomEnabled;
	}
	
}
