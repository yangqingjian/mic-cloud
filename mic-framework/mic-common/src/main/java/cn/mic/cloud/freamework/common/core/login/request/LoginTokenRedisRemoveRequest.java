package cn.mic.cloud.freamework.common.core.login.request;

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
public class LoginTokenRedisRemoveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

}
