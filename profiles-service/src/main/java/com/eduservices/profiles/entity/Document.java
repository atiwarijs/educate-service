package com.eduservices.profiles.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.eduservices.profiles.util.Encrypt;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String docId;
    @Convert(converter = Encrypt.class)
    @Column(name = "doc_name", nullable = false)
    private String docName;
    @Convert(converter = Encrypt.class)
    @Column(name = "doc_num", nullable = true)
    private String docNumber;
    @Convert(converter = Encrypt.class)
    @Column(name = "doc_type", nullable = false)
    private String docType;
    @Column(name = "ref_id", nullable = true)
    private String refId;
    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private String docData;
    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public Document(String docName, String docType, String docData, String refId) {
        this.docName = docName;
        this.docType = docType;
        this.docData = docData;
        this.refId = refId;
    }


}
