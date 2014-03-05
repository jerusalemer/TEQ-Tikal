package controllers;

import dao.CandidateDao;
import model.Candidate;
import model.Group;
import org.codehaus.jackson.node.ArrayNode;
import play.Logger;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import service.CandidateFactory;
import service.CsvExporter;
import service.MailSender;
import views.html.candidate_questionnarie;
import views.html.candidates;

import java.util.*;

/**
 * Created by Art on 2/5/14.
 */
public class CandidateController extends Controller {

    private static CandidateDao candidateDao;
    private static CsvExporter csvExporter;
    private static CandidateFactory candidateFactory;
    private static MailSender mailSender;

    public static Result getAll(){
        final Iterable<Candidate> candidateIterable = candidateDao.getAll();
        Collection<Candidate> allCandidates = new ArrayList<>();
        for (Candidate candidate : candidateIterable) {
            allCandidates.add(candidate);
        }
        return ok(candidates.render(allCandidates));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findCandidate(String email) {
        Candidate candidate = candidateDao.getByEmail(email);
        if (candidate == null) {
            return notFound();
        }

        return ok(candidate_questionnarie.render(candidate));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findCandidateByName(String name) {
        final Collection<Candidate> candidates = candidateDao.findByNameOrLastName(name);
        if (candidates.isEmpty()) {
            return notFound();
        }
        //transform into json
        ArrayNode result = (ArrayNode) Json.toJson(candidates);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findCandidateByGroup(String group) {
        final Collection<Candidate> candidates = candidateDao.findByGroup(Group.valueOf(group));
        if (candidates.isEmpty()) {
            return notFound();
        }
        //transform into json
        ArrayNode result = (ArrayNode) Json.toJson(candidates);
        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result findByRecruiter(String recruiter) {
        final Collection<Candidate> candidates = candidateDao.findByRecruiter(recruiter);
        if (candidates.isEmpty()) {
            return notFound();
        }
        //transform into json
        ArrayNode result = (ArrayNode) Json.toJson(candidates);
        return ok(result);
    }

    public static void setUp(CandidateDao candidateDao, CsvExporter csvExporter, CandidateFactory candidateFactory, MailSender mailSender){
        CandidateController.candidateDao = candidateDao;
        CandidateController.csvExporter = csvExporter;
        CandidateController.candidateFactory = candidateFactory;
        CandidateController.mailSender = mailSender;
    }

    public static Result registerCandidate() {
        try{
            createCandidate();
            return getAll();
        }catch (IllegalArgumentException ex){
            return badRequest(ex.getMessage());
        }
    }

    public static Result deleteCandidate(String candidateEmail){
        Candidate candidate = candidateDao.getByEmail(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        candidateDao.delete(candidate);

        return getAll();
    }

    private static void createCandidate() {
        Map<String,String[]> requestParams = request().body().asFormUrlEncoded();
        String fullName = requestParams.get("full_name")[0];
        String email = requestParams.get("email")[0];
        String recruiter = requestParams.get("recruiter")[0];
        String[] groupsStr = requestParams.get("group");

        String firstName = fullName.split(" ")[0];
        String lastName = fullName.split(" ")[1];

        Set<Group> groups = new HashSet<>();
        for (String group : groupsStr) {
            groups.add(Group.valueOf(group));
        }

        candidateFactory.createCandidate(firstName, lastName, email, recruiter, groups);

    }

    // Groups - comma separated questionnarie groups
    public static Result getQuestionnarie(String candidateEmail) {
        Candidate candidate = candidateDao.getByEmail(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        return ok(candidate_questionnarie.render(candidate));
    }

    public static Result sendEmail(String candidateEmail){
        Candidate candidate = candidateDao.getByEmail(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        try{
            mailSender.sendTeqMail(candidate);
            return ok("Email to: " + candidateEmail + " successfully sent");
        }catch (Exception e) {
            Logger.error("Error sending email", e);
            return internalServerError(e.getMessage());
        }
    }

    public static Result exportToCSV(String candidateEmail) {
        Candidate candidate = candidateDao.getByEmail(candidateEmail);
        if (candidate == null) {
            return notFound();
        }

        response().setContentType("text/csv");
        Controller.response().setHeader("Content-Disposition",
                "attachment;filename=" + candidate.getFirstName() + "_" + candidate.getLastName() + ".fods");

        String candidateExcel = csvExporter.toExcel(candidate);

        return ok(candidateExcel);

    }

}
