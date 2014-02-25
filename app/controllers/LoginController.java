package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.candidates;
import views.html.login;

import java.util.Map;

/**
 * User: Artiom
 * Date: 24/11/13
 */
public class LoginController extends Controller {

    public static Result getLoginPage(){
        return ok(login.render());
    }

    public static Result doLogin(){

        Map<String, String[]> values = request().body().asFormUrlEncoded();

        String username = values.get("username")[0];
        String password = values.get("password")[0];

        if(!"root".equalsIgnoreCase(username)
                || !"123".equals(password)){
            return unauthorized("Wrong password");
        }
        session("username", username);
        return redirect("/candidates");
    }

}
