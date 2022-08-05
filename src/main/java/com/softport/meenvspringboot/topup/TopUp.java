package com.softport.meenvspringboot.topup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class TopUp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    @Column
    private long userId;

    @Column
    private float amount;

    @Column
    private long smsQuantity;

    @Column
    private String phoneNumber;

    @Column
    private String network;

    @Column
    private String transactionId;

    @Column
    private long otp;

    @Column
    private Date date;
}
