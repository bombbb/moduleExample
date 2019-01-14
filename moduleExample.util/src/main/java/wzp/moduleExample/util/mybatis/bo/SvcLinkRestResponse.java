package wzp.moduleExample.util.mybatis.bo;

import java.util.Date;

public class SvcLinkRestResponse {
	private String svcId;
	private String app;

	private String url;
	private Boolean timeout;
	private Boolean exception;
	private String exceptionMessage;
	private String ip;
	private String clientIp;
	private String linkId;
	private Long span;
	private Date timestamp;

	public String getSvcId() {
		return svcId;
	}

	public void setSvcId(String svcId) {
		this.svcId = svcId;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getTimeout() {
		return timeout;
	}

	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}

	public Boolean getException() {
		return exception;
	}

	public void setException(Boolean exception) {
		this.exception = exception;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Long getSpan() {
		return span;
	}

	public void setSpan(Long span) {
		this.span = span;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
