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

    /* a value that can be attached to this otp to load different entity when verification is successfull*/
    @Column
    private String firstIdentifier;

    /* another value that can be attached to this otp to load different entity when verification is successfull*/

    @Column
    private String secondIdentifier;

}
