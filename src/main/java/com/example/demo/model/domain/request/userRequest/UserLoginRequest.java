package com.example.demo.model.domain.request.userRequest;

        import lombok.Data;

        import java.io.Serial;
        import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5725586937659214123L;
    private String email;
    private String userPassword;

}
