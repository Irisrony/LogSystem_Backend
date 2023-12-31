package com.cvte.logsystem.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UploadEntity {
    private int id;
    @NotBlank
    @Length(min = 8,max = 8)
    private String appid;
    @NotBlank
    @Length(max = 68)
    private String userid;
}
