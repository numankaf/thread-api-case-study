package com.threadserver.entity;

import com.threadserver.enums.ThreadType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity()
@Table(name = "thread")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class Thread extends BaseAuditEntity{

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ThreadType type;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}
