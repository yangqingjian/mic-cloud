package cn.mic.cloud.freamework.common.core.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * @author : YangQingJian
 * @date : 2022/12/28
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SimpleAuthority implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权限
     */
    private String authority;

}
