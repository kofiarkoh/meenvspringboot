package com.softport.meenvspringboot.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.softport.meenvspringboot.messages.MessageRecipient;

import lombok.Data;

@Data
public class SendOneTimeMessageDTO {

    @Length(min = 1, max = 160)
    private String message;
    private String senderId;

    @NotNull

    @Size(min = 1, max = 5)

    private List<@Valid MessageRecipient> recipients;

}
