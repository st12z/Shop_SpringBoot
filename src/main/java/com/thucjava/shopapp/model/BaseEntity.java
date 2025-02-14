package com.thucjava.shopapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="create_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name="created_by")
    @CreatedBy
    private String createdBy;

    @Column(name="modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Column(name="modified_by")
    @LastModifiedBy
    @Temporal(TemporalType.TIMESTAMP)
    private String modifiedBy;


}
