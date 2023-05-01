package com.softport.meenvspringboot.OTP;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

import com.softport.meenvspringboot.user.User;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table
@RequiredArgsConstructor
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "user_id")
    private long id;

    @Column
    private String code;

    @Column
    private Date date;

    /*
     * a value that can be attached to this otp to load different entity when
     * verification is successfull
     */
    @Column
    private String firstIdentifier;

    /*
     * another value that can be attached to this otp to load different entity when
     * verification is successfull
     */

    @Column
    private String secondIdentifier;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public boolean isExpired() {

        Instant now = Instant.now();
        Duration timeElapsed = Duration.between(this.getDate().toInstant(), now);
        return timeElapsed.toMinutes() > 5;
    }

}
