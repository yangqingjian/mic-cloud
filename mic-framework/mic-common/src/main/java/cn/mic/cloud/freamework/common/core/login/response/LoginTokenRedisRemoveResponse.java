package cn.mic.cloud.freamework.common.core.login.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
public class LoginTokenRedisRemoveResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean flag;


}
