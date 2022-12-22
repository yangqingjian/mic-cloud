package cn.mic.cloud.freamework.common.vos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yorking
 * @date 2020/06/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 职位Id
     */
    private String depPosId;



}
