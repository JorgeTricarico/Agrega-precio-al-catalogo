package com.innovacion.conversor.dto;

import org.springframework.web.multipart.MultipartFile;

public class ConvertRequest {

    private MultipartFile excelFile;
    private MultipartFile pdfFile;
    private Double ganancia;
    private String newPdfName;

    public MultipartFile getExcelFile() {
        return excelFile;
    }

    public void setExcelFile(MultipartFile excelFile) {
        this.excelFile = excelFile;
    }

    public MultipartFile getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(MultipartFile pdfFile) {
        this.pdfFile = pdfFile;
    }

    public Double getGanancia() {
        return ganancia;
    }

    public void setGanancia(Double ganancia) {
        this.ganancia = ganancia;
    }

    public String getNewPdfName() {
        return newPdfName;
    }

    public void setNewPdfName(String newPdfName) {
        this.newPdfName = newPdfName;
    }
}
