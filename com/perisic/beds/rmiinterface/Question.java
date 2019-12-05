package com.perisic.beds.rmiinterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.perisic.beds.rmiinterface.PublicEnum.Option;
import com.perisic.mongoclient.SurveyAnswerService;
import com.perisic.mongoclient.SurveyQuestionService;

public class Question implements Serializable {

	private static final long serialVersionUID = -7273230871957691871L;
	private String[] answers;
	private String answerType;
	private String questionText;
	private Hashtable<String, Integer> frequencies = new Hashtable<String, Integer>();

	// Integer>();
	public Question(String questionText,String answerType, String[] answers) {
		super();
		this.answers = answers;
		this.questionText = questionText;
		this.answerType = answerType;
	}

	public String getQuestionText() {
		return questionText;
	}
	
	public String getAnswerType() {
		return answerType;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getFrequency(String answer) {
		Integer n = frequencies.get(answer);
		if (n == null)
			return 0;
		else
			return n;
	}

	public void addAnswer(String answer) {
		int n = getFrequency(answer);
		frequencies.put(answer, n + 1);
		SurveyAnswerService aswr = new SurveyAnswerService();
		aswr.addToAnswerPane(answer, 1, 1);
	}

	public void addQuestionToSurvey(String questionDesc, String option) {

		List<String[]> optionList = new ArrayList<>();
		ArrayList<String> optionCodeList = new ArrayList<String>();
		ArrayList<String> questionList = new ArrayList<String>();
		PublicEnum opt_ = null;
		String enumString = null;
		switch (Option.valueOf(option)) {
		case RANGE:
			opt_ = PublicEnum.RANGE;
			enumString = "RANGE";
			break;
		case SINGLEOPT:
			opt_ = PublicEnum.SINGLEOPT;
			enumString = "SINGLEOPT";
			break;
		case JOB:
			opt_ = PublicEnum.JOB;
			enumString = "JOB";
			break;
		case EDULVL:
			opt_ = PublicEnum.EDULVL;
			enumString = "EDULVL";
			break;
		case USEROPT:
			opt_ = PublicEnum.USEROPT;
			enumString = "USEROPT";
			break;
		case CHOICE:
			opt_ = PublicEnum.CHOICE;
			enumString = "CHOICE";
			break;
		case CHOICE2:
			opt_ = PublicEnum.CHOICE2;
			enumString = "CHOICE2";
			break;
		case LOGICAL:
			opt_ = PublicEnum.LOGICAL;
			enumString = "LOGICAL";
			break;
		}

		if (opt_ != null && questionDesc != null) {
			questionList.add(questionDesc);
			optionList.add(opt_.getOption());
			optionCodeList.add(enumString);
		}
		try {
			SurveyQuestionService svy = new SurveyQuestionService();
			svy.addToQuestionPane(questionList, optionList,optionCodeList);

		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}

/**
 * Debug main for SurveyQuestionService Remove once the implementaion completed
 */
class QuestService {
	public static void main(String[] args) {
		
	}
}