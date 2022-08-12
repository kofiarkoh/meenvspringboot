package com.softport.meenvspringboot.OTP;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "user_id")
    private long id;

    @Column
    private String code;

    @Column
    private Date date;

    @Column
    private String firstIdentifier;

    @Column
    private String secondIdentifier;

}
