package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName : TestVo
 * Created with IDEA
 * @author:CarlLing
 * @CreateDate : 2019-01-12 1:05
 * @Description :
 */
@Getter
@Setter
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull(message = "id����Ϊ��")
    @Max(value = 10, message = "id ���ܴ��� 10")
    @Min(value = 0, message = "id ���ٴ��ڵ���0")
    private Integer id;

    @NotEmpty
    private List<String> str;
}
