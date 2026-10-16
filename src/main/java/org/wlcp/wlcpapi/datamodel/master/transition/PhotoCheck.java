package org.wlcp.wlcpapi.datamodel.master.transition;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Photo check transition settings for one scope. The player photographs an object, the phone
 * scores it with the Teachable Machine image model at modelUrl, and the game moves on when the
 * score for targetClass is at least threshold (0 to 1). An empty targetClass is the
 * "any other photo" branch. Stored like TimerDuration: one row per transition and scope.
 */
@Embeddable
public class PhotoCheck implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "MODEL_URL", length = 1024)
	private String modelUrl;

	@Column(name = "TARGET_CLASS")
	private String targetClass;

	@Column(name = "THRESHOLD")
	private Double threshold;

	public PhotoCheck() {

	}

	public PhotoCheck(String modelUrl, String targetClass, Double threshold) {
		super();
		this.modelUrl = modelUrl;
		this.targetClass = targetClass;
		this.threshold = threshold;
	}

	public String getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(String modelUrl) {
		this.modelUrl = modelUrl;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

}
