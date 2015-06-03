package vivid.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by wujy on 15-6-3.
 */
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(
            {
                    AuthenticationException.class,
                    UnknownAccountException.class,
                    UnauthenticatedException.class,
                    IncorrectCredentialsException.class,
                    UnauthorizedException.class
            }
    )
    public void unauthorized() {

    }

}
