package com.kwizzie.restservices;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.kwizzie.dao.PrivateQuizRoomDAO;
import com.kwizzie.model.AudioQuestion;
import com.kwizzie.model.MCQAnswerType;
import com.kwizzie.model.PictureQuestion;
import com.kwizzie.model.PrivateQuizRoom;
import com.kwizzie.model.QRAnswerType;
import com.kwizzie.model.QRQuestion;
import com.kwizzie.model.Question;
import com.kwizzie.model.QuestionLocation;
import com.kwizzie.model.TextAnswerType;
import com.kwizzie.model.TextQuestion;
import com.kwizzie.model.VideoQuestion;

import flexjson.JSONSerializer;

@Path("/quizRoom/private")
public class PrivateQuizRoomResource {

	@Autowired
	PrivateQuizRoomDAO quizRoomDAO;
	
	@Autowired
	JSONSerializer serializer;
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String enterQuizRoom(@QueryParam("roomId") String roomID , @QueryParam("key") String securityKey){
		PrivateQuizRoom quizRoom = quizRoomDAO.getQuizRoom(roomID, securityKey);
		if(quizRoom != null){
			return serializer.deepSerialize(quizRoom);
		} else {
			return "1";
		}	
	    
	}
	
	@GET
	@Path("/add")
	public String addQuizRoom(@QueryParam("roomId")String roomID,@QueryParam("key")String securityKey){
		List<Question> questions = new ArrayList<>();
		ArrayList<String> optionList = new ArrayList<String>();
		optionList.add("disney");
		optionList.add("dreamworks");
		QuestionLocation location = new QuestionLocation(15.390353333333334, 73.87886499999999,1000.0, "BITS Goa", "http://localhost:8080/images/faqsImage.png");
		Question ques = new PictureQuestion("http://img4.wikia.nocookie.net/__cb20130222060253/disney/images/7/7d/2013disneyprincess.jpg", location,
		"Identify The picture",new MCQAnswerType(optionList, 0),false);

		Question ques2 = new TextQuestion("contin___s", null,"Fill in the missing characters",new TextAnswerType("uou"),false);
		ArrayList<String> options3 = new ArrayList<String>();
		options3.add("sonu nigam");
		options3.add("arijit singh");
		options3.add("mika singh");
		options3.add("honney singh");

		Question ques3 = new AudioQuestion("http://www.largesound.com/ashborytour/sound/brobob.mp3", location, "Identify the singer", new MCQAnswerType(options3, 2), false);
		Question ques4 = new VideoQuestion("http://info.sonicretro.org/images/5/54/Tiger_Sonic_3D_Blast_Ad.flv", location, "Identify the actor in video", new TextAnswerType("Ranbeer Kapoor"), false);
		Question ques5 = new QRQuestion(location, "scan the qr code", new QRAnswerType("testqr"), false);
		questions=new ArrayList<Question>();
		questions.add(ques5);
		questions.add(ques);
		questions.add(ques2);
		questions.add(ques3);
		questions.add(ques4);
		try{
		//TODO: change roomName and description
		quizRoomDAO.save(new PrivateQuizRoom(questions, securityKey, roomID, roomID, roomID));
		return "1";
		} catch(Exception e){
			return "0";
		}
	}
	
	@GET
	@Path("/delete")
	public String deleteQuizRoom(@QueryParam("roomId")String roomID,@QueryParam("key")String securityKey){
		PrivateQuizRoom quizRoom = quizRoomDAO.getQuizRoom(roomID, securityKey);
		quizRoomDAO.delete(quizRoom);
		return "1";
	}
}
