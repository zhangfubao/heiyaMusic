package com.heiya123.music.entity.kugou;import java.io.Serializable;public class KuGouMusicUrl implements Serializable{	private static final long serialVersionUID = 810566771L;	private String fileName;	private long fileSize;	private long fileHead;	private long status;	private long timeLength;	private String url;	private long q;	private long bitRate;	private String extName;	public String getFileName() {		return this.fileName;	}	public void setFileName(String fileName) {		this.fileName = fileName;	}	public long getFileSize() {		return this.fileSize;	}	public void setFileSize(long fileSize) {		this.fileSize = fileSize;	}	public long getFileHead() {		return this.fileHead;	}	public void setFileHead(long fileHead) {		this.fileHead = fileHead;	}	public long getStatus() {		return this.status;	}	public void setStatus(long status) {		this.status = status;	}	public long getTimeLength() {		return this.timeLength;	}	public void setTimeLength(long timeLength) {		this.timeLength = timeLength;	}	public String getUrl() {		return this.url;	}	public void setUrl(String url) {		this.url = url;	}	public long getQ() {		return this.q;	}	public void setQ(long q) {		this.q = q;	}	public long getBitRate() {		return this.bitRate;	}	public void setBitRate(long bitRate) {		this.bitRate = bitRate;	}	public String getExtName() {		return this.extName;	}	public void setExtName(String extName) {		this.extName = extName;	}}