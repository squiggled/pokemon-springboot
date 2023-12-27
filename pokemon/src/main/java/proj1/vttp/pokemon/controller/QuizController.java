package proj1.vttp.pokemon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import proj1.vttp.pokemon.model.Question;
import proj1.vttp.pokemon.service.QuizAPIService;
import proj1.vttp.pokemon.service.UserService;
import proj1.vttp.pokemon.utils.UserUtils;

@Controller
public class QuizController {
    @Autowired
    QuizAPIService quizAPIService;

    @Autowired
    UserService userService;

    @GetMapping("/quiz")
    public String quiz(Model model, HttpSession session) {
        Question question = quizAPIService.getQuestion();

        // if session==null, set a new qn
        Question currentQuestion = (Question) session.getAttribute(UserUtils.CURRENT_QUESTION);
        if (currentQuestion == null) {
            session.setAttribute(UserUtils.CURRENT_QUESTION, question);
            model.addAttribute("question", question);
            System.out.println(question.getQuestion());
            System.out.println(question.getAnswer());
        } else { // if session!=null, user has not solved the qn, so keep the current question
            session.setAttribute(UserUtils.CURRENT_QUESTION, currentQuestion);
            model.addAttribute("question", currentQuestion);
            System.out.println(currentQuestion.getQuestion());
            System.out.println(currentQuestion.getAnswer());
        }

        // initialise total score
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        Integer totalScore = userService.getScore(username);
        if (totalScore == null) {
            totalScore = 0;
        }

        //find points to next badge
        Integer pointsToNextBadge = userService.pointsToNextBadge(totalScore);
        String nextBadge = userService.nextBadge(totalScore);

        // initialise attempts
        Integer attempts = (Integer) session.getAttribute(UserUtils.QUESTION_ATTEMPTS);
        if (attempts == null) {
            attempts = 0;
        }

        session.setAttribute(UserUtils.USER_SCORE, totalScore);
        session.setAttribute(UserUtils.QUESTION_ATTEMPTS, attempts);
        model.addAttribute("pointsToNextBadge", pointsToNextBadge);
        model.addAttribute("nextBadge", nextBadge);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("attempts", attempts);
        
        return "quiz";
    }

    @PostMapping("/quiz")
    public String verifyAns(@RequestBody MultiValueMap<String, String> form, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String answer = form.getFirst("answer");

        Question currentQuestion = (Question) session.getAttribute(UserUtils.CURRENT_QUESTION);
        Integer attempts = (Integer) session.getAttribute(UserUtils.QUESTION_ATTEMPTS);
        Integer totalScore = (Integer) session.getAttribute(UserUtils.USER_SCORE);

        if (attempts == 0)
            session.setAttribute(UserUtils.QUESTION_ATTEMPTS, 0);

        int roundScore = 0;
        // if answer is correct
        if (answer.toLowerCase().equals(currentQuestion.getAnswer().toLowerCase())) {

            switch (attempts) {
                case 0:
                    roundScore = 5;
                    break;
                case 1:
                    roundScore = 3;
                    break;
                default:
                    roundScore = 1;
            }
            totalScore += roundScore;

            // save scores to redis
            String username = (String) session.getAttribute(UserUtils.USER_SESSION);
            userService.saveScore(totalScore, username);

            //reset question attempts
            session.setAttribute(UserUtils.QUESTION_ATTEMPTS, 0);

            // also set scores in current session
            session.setAttribute(UserUtils.USER_SCORE, totalScore);
            session.setAttribute(UserUtils.CURRENT_QUESTION, null); // reset the qn
            redirectAttributes.addFlashAttribute("message", "Correct answer! You earned " + roundScore + " points");
            return "redirect:/quiz";
            
        } else {
            attempts++;
            session.setAttribute(UserUtils.QUESTION_ATTEMPTS, attempts);
            String message = "Wrong answer. Try again";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/quiz";
        }
    }

    @PostMapping("/quiz/skip")
    public String skipQuestion(HttpSession session, RedirectAttributes redirectAttributes){
        //reset attempts 
        session.setAttribute(UserUtils.QUESTION_ATTEMPTS, 0);

        //deduct from score
        Integer totalScore = (Integer) session.getAttribute(UserUtils.USER_SCORE);
        totalScore-=5;

        //save new score to redis
        String username = (String) session.getAttribute(UserUtils.USER_SESSION);
        userService.saveScore(totalScore, username);

        //set score in current session
        session.setAttribute(UserUtils.USER_SCORE, totalScore);
        session.setAttribute(UserUtils.CURRENT_QUESTION, null); // reset the qn
        redirectAttributes.addFlashAttribute("message", "Question skipped - 5 points deducted");

        return "redirect:/quiz";
    }
}
