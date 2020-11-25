package org.wlcp.wlcpapi.datamodel.master.state;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VideoOutput {
	
	@Column(name = "URL")
	private String url;
	
	public VideoOutput() {
		super();
	}
	
	public VideoOutput(String url) {
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
