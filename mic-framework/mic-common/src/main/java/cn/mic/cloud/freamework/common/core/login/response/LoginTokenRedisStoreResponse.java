package cn.mic.cloud.freamework.common.core.login.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
public class LoginTokenRedisStoreResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;


}
