package com.eduservices.campusmgmt.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    private String docId;
    private String docName;
    private String docNumber;
    private String docType;
    private String refId;
    private String docData;

    public DocumentDto(String docName, String docNumber, String docType, String refId, String docData) {
        this.docName = docName;
        this.docNumber = docNumber;
        this.docType = docType;
        this.refId = refId;
        this.docData = docData;
    }
    
    public String getDocName() {
        return docName;
    }
    
    public void setRefId(String refId) {
        this.refId = refId;
    }
}
