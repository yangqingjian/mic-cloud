package cn.mic.cloud.freamework.common.core.login.request;

import cn.mic.cloud.freamework.common.core.login.LoginAuthUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginTokenRedisStoreRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

    private Integer expireSeconds = 3600;

    private LoginAuthUser loginUser;

}
