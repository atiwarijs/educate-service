package com.eduservices.profile.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "created_date", updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedBy
	@Column(name = "updated_by")
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "updated_date")
	private LocalDateTime lastModifiedDate;
	
	@Column(name = "active", nullable = false)
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
}
