package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: SingleButtonPress
 *
 */
@Embeddable
public class SingleButtonPress implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "BUTTON_1")
	private Boolean button1;
	
	@Column(name = "BUTTON_1_LABEL", length = 20)
	private String button1Label;
	
	@Column(name = "BUTTON_2")
	private Boolean button2;
	
	@Column(name = "BUTTON_2_LABEL", length = 20)
	private String button2Label;
	
	@Column(name = "BUTTON_3")
	private Boolean button3;
	
	@Column(name = "BUTTON_3_LABEL", length = 20)
	private String button3Label;
	
	@Column(name = "BUTTON_4")
	private Boolean button4;
	
	@Column(name = "BUTTON_4_LABEL", length = 20)
	private String button4Label;

	public SingleButtonPress() {
		super();
	}

	public SingleButtonPress(Boolean button1, String button1Label, Boolean button2, String button2Label,
			Boolean button3, String button3Label, Boolean button4, String button4Label) {
		super();
		this.button1 = button1;
		this.button1Label = button1Label;
		this.button2 = button2;
		this.button2Label = button2Label;
		this.button3 = button3;
		this.button3Label = button3Label;
		this.button4 = button4;
		this.button4Label = button4Label;
	}
	
	public Boolean getButton1() {
		return button1;
	}

	public void setButton1(Boolean button1) {
		this.button1 = button1;
	}

	public String getButton1Label() {
		return button1Label;
	}

	public void setButton1Label(String button1Label) {
		this.button1Label = button1Label;
	}

	public Boolean getButton2() {
		return button2;
	}

	public void setButton2(Boolean button2) {
		this.button2 = button2;
	}

	public String getButton2Label() {
		return button2Label;
	}

	public void setButton2Label(String button2Label) {
		this.button2Label = button2Label;
	}

	public Boolean getButton3() {
		return button3;
	}

	public void setButton3(Boolean button3) {
		this.button3 = button3;
	}

	public String getButton3Label() {
		return button3Label;
	}

	public void setButton3Label(String button3Label) {
		this.button3Label = button3Label;
	}

	public Boolean getButton4() {
		return button4;
	}

	public void setButton4(Boolean button4) {
		this.button4 = button4;
	}

	public String getButton4Label() {
		return button4Label;
	}

	public void setButton4Label(String button4Label) {
		this.button4Label = button4Label;
	}
	
   
}
