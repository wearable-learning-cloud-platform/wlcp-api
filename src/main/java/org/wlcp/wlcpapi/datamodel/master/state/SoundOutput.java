package org.wlcp.wlcpapi.datamodel.master.state;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SoundOutput {
	
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
