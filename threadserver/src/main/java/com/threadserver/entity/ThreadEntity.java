package com.threadserver.entity;

import com.threadserver.enums.ThreadType;
import jakarta.persistence.*;
import lombok.*;

@Entity()
@Table(name = "thread")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThreadEntity extends BaseAuditEntity{

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ThreadType type;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;
}
