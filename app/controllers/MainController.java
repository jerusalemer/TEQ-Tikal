package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Created by tema on 1/21/14.
 */
public class MainController extends Controller {

    public static Result sendQuestionnaire() {
        return ok("send questionnaire");
    }

    public static Result registerCandidate() {
        return ok("register candidate");
    }
}
