
import Controllers.DBSupport;
import Models.Message;
import Models.User;
import Models.Workspace;

import java.nio.channels.Channel;
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;


/**
 * This will be the main controller for the application.
 *
 * It will take the initial input for User Input
 *  and then pass it along to other classes to handle the actual functionality
 * @Author Dylan Mrzlak
 *      Original Framework and Use handle for CREATE_WORkSPACE and JOIN_WORKSPACE
 */
public class InputController {
    private static final String CREATE_WORKSPACE = "create workspace";
    private static final String JOIN_WORKSPACE = "join";
    private static final String SEND = "send";
    private static final String SEND_DM = "send to";
    private static final String ADD_USER = "create user";
    private static final String LOGIN = "login";
    private static final String PIN_MESSAGE = "pin message";

    public static void main(String[] args){
      //If this line get mad, check your dependencies, may have dropped
        Gson gson = new Gson();
      User thisUser = null;
      Workspace cur = null;
      Scanner input = new Scanner(System.in);
      String userInput = "";
      Random rand = new Random();
      Channel curChannel;
      int messageNumber;

      /*
      * We want to handle all forms of input via commands. THat is everything is in the switch case.
      * Eventtually, logan or I will get HELP commands in here. Probably just listing the commands and arguments
      * Possisble allowing "HELP - COMMAND" which will explain what the command does and the arguments/preconditions
      *
      *
      *
      *
      * 
      System.out.println("Please enter username:\n");
      User current = new User("", "");
      System.out.println("Please enter your password!\n");
      String password = input.nextLine();
       */

      do {
          userInput = input.nextLine();
          //By forcing commands to be in a format of COMMAND - ARGUMENT
          //We can easily manage the input and decide what is needed
          int substringBegin = userInput.indexOf('-');
          if(substringBegin == -1) substringBegin = 0;
          String command = userInput.substring(0, substringBegin).trim();
          String[] userArgs = userInput.substring(substringBegin + 1).trim().split(" ");
          switch (command){
              case ADD_USER:
                  if(userArgs.length != 2) {
                      System.out.println("Invalid Number or Arguments");
                      break;
                  }
                  DBSupport.HTTPResponse uResponse = User.createUser(userArgs[0], userArgs[1]);
                  if (uResponse.code > 300) {
                      System.out.println(uResponse.response);
                  } else {
                      System.out.println("Saved User");
                      User u = gson.fromJson(uResponse.response, User.class);
                      thisUser = u;
                  }
                  break;
              case CREATE_WORKSPACE:
                  if(userArgs.length != 1) {
                      System.out.println("Invalid Number or Arguments");
                      break;
                  }
                  DBSupport.HTTPResponse wResponse = Workspace.createWorkspace(userArgs[0]);
                  if (wResponse.code > 300) {
                      System.out.println(wResponse.response);
                  } else {
                     System.out.println("Saved Workspace");
                     Workspace w = gson.fromJson(wResponse.response, Workspace.class);
		            cur = w;
                  }
                  break;
              case JOIN_WORKSPACE:
                  if(thisUser == null) {
                      System.out.println("You need to create a user or sign in to continue");
                      break;
                  }
                  if(userArgs.length != 1) {
                      System.out.println("Invalid Number or Arguments");
                      break;
                  }
                  DBSupport.HTTPResponse joinWorkspace = Workspace.joinWorkspace(userArgs[0], thisUser.getName());
                  if (joinWorkspace.code > 300) {
                      System.out.println(joinWorkspace.response);
                  } else {
                      System.out.println("Joining Workspace");
                      Workspace w = gson.fromJson(joinWorkspace.response, Workspace.class);
                      cur = w;
                  }
                  break;
              case PIN_MESSAGE:
                  if(thisUser == null) {
                      System.out.println("You need to create a user or sign in to continue");
                      break;
                  }
                  if(userArgs.length != 1) {
                      System.out.println("Invalid Number or Arguments");
                      break;
                  }
                  DBSupport.HTTPResponse pinMessage = Workspace.pinMessage(userArgs[0]);
                  if (pinMessage.code > 300) {
                      System.out.println(pinMessage.response);
                  }
                  else {
                      System.out.println("Pinned message");
                      Message m = gson.fromJson(pinMessage.response, Message.class);
                  }
                  break;
              case SEND_DM:
                  if(userArgs.length < 2){
                      System.out.println("Invalid number of arguments");
                  }
                  String directMessage = "";
                  for(int i = 1; i < userArgs.length; i++){
                      directMessage += userArgs[i];
                  }
                  DBSupport.HTTPResponse dm = Message.sendDirectMessage(thisUser.getName(), userArgs[0], directMessage);
                  if (dm.code > 300) {
                      System.out.println(dm.response);
                  } else {
                      System.out.println("Joining Workspace");
                      Message m = gson.fromJson(dm.response, Message.class);
                      System.out.println("Message Sent: \n\t" + m.getContent());
                  }
                  break;
              case SEND:
                  if(userArgs.length < 1){
                      System.out.println("Invalid number of arguments");
                  }
                  String message = "";
                  for(int i = 0; i < userArgs.length; i++){
                      message += userArgs[i];
                  }
                  DBSupport.HTTPResponse sendMessage = Message.sendMessage(thisUser.getName(), cur.getName(), curChannel.getName(), message);
                  if (sendMessage.code > 300) {
                      System.out.println(sendMessage.response);
                  } else {
                      System.out.println("Joining Workspace");
                      Message m = gson.fromJson(sendMessage.response, Message.class);
                      System.out.println("Message Sent: \n\t" + m.getContent());
                  }
                  break;
                  default:
                  System.out.println("Invalid Input please try again :(");
                  break;
          }
      } while (input.hasNextLine());

  }


}