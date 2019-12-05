package com.perisic.beds.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import com.perisic.beds.rmiinterface.Question;
import com.perisic.beds.rmiinterface.RemoteQuestions;
import com.perisic.beds.rmiinterface.UserRoles;
import com.perisic.mongoclient.SurveyQuestionService;

import java.util.ArrayList;
import java.util.List;

public class SurveyImplementation extends UnicastRemoteObject implements RemoteQuestions {
	
    private static final long serialVersionUID = -3763231206310491048L;
    Vector<Question> myQuestions = new Vector<Question>();
    // Vector<UserRoles> surveyUserRoles = new Vector<UserRoles>();
    UserRoles surveyUserRoles =  new UserRoles();

    @SuppressWarnings("unchecked")
	SurveyImplementation() throws RemoteException {
        super();
        // System.out.println("QuestionServerImplementation Created");
        

        SurveyQuestionService svy = new SurveyQuestionService();
        for (org.bson.Document a : svy.getBasicQuestions()) {
            List<String> quesOptions = new ArrayList<String>();
            quesOptions = (List<String>) a.get("Answer");
            String[] answers = new String[quesOptions.size()];
            answers = quesOptions.toArray(answers);
            myQuestions.add(new Question((String) a.get("Question"),(String) a.get("Answer_Type"), answers));
        }
    }

    @Override
    public Question getQuestion(int i) throws RemoteException {
        return myQuestions.elementAt(i);
    }

    @Override
    public int getNumberOfQuestions() throws RemoteException {
        return myQuestions.size();
    }

    @Override
	public void submitAnswer(int i, String answer) throws RemoteException {
		myQuestions.elementAt(i).addAnswer(answer);
    }
    
    @Override
    public Vector<Question> getData() {
    return myQuestions;
    }
    
    /**
     * Generate the standerd user login for survey users
     */
    @Override
    public boolean getSurveyAccess( String username, String pass){
        return surveyUserRoles.generateUserLogin(username, pass);
    }

}

