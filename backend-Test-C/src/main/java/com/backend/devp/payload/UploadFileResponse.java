package com.backend.devp.payload;

public class UploadFileResponse {
	
	private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private String aufgabeblattNum;
    private long size;
    
    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size ,String aufgabeblattNum) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.aufgabeblattNum = aufgabeblattNum;
    }

	public String getAufgabeblattNum() {
		return aufgabeblattNum;
	}

	public void setAufgabeblattNum(String aufgabeblattNum) {
		this.aufgabeblattNum = aufgabeblattNum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}


}
