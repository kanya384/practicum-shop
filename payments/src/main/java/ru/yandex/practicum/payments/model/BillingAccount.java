package ru.yandex.practicum.payments.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "billing_account")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillingAccount {
    @Id
    private Long id;
    private Integer money;
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate modifiedAt;
}
