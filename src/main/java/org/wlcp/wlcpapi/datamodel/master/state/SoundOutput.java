package org.wlcp.wlcpapi.datamodel.master.state;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SoundOutput implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "URL")
	private String url;
	
	public SoundOutput() {
		super();
	}

	public SoundOutput(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
