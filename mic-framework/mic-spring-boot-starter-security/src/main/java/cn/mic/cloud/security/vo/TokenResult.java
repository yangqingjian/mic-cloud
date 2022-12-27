package cn.mic.cloud.security.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
public class TokenResult {

    /**
     * 返回的token
     */
    private String token;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

}
