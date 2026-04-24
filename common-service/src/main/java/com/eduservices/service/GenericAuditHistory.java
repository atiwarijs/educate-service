package com.eduservices.service;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class GenericAuditHistory<T> {

    @Autowired
    private AuditReader auditReader;

    public List<?> getRevisions(Long id, boolean fetchChanges, T entity) {
        AuditQuery auditQuery = null;

        if(fetchChanges) {
            auditQuery = auditReader.createQuery()
                    .forRevisionsOfEntityWithChanges(entity.getClass(), true);
        }
        else {
            auditQuery = auditReader.createQuery()
                    .forRevisionsOfEntity(entity.getClass(), true);
        }
        auditQuery.add(AuditEntity.id().eq(id));
        return auditQuery.getResultList();
    }
}
