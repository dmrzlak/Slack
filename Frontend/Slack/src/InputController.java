import Controllers.DBSupport;
import Models.Message;
import Models.Channel;
import Models.User;
import Models.Workspace;

import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;


/**
 * This will be the main controller for the application.
 *
 * It will take the initial input for User Input
 *  and then pass it along to other classes to handle the actual functionality
 * @Author Dylan Mrzlak
 *      Original Framework and Use handle for CREATE_WORKSPACE and JOIN_WORKSPACE
 */
public class InputController {
    private static final String CREATE_WORKSPACE = "create workspace";
    private static final String JOIN_WORKSPACE = "join";
    private static final String CREATE_CHANNEL = "create channel";
    private static final String VIEW_USERS = "view users";
    private static final String SEND = "send";
    private static final String SEND_DM = "send to";
    private static final String ADD_USER = "create user";
    private static final String LOGIN = "login";
    private static final String PIN_MESSAGE = "pin message";
    private static final String LOG_MESSAGES = "log messages";
    private static final String VIEW_MENTIONS = "view mentions";
    private static final String SIGN_IN = "sign in";
    private static final String HELP = "help";
    private static Gson gson = new Gson();
    private static User thisUser = null;
    private static Workspace cur = null;
    private static Channel curChannel = null;

    public static void main(String[] args){
      //If this line get mad, check your dependencies, may have dropped
     
      Scanner input = new Scanner(System.in);
      String userInput = "";
      printInstructions();

      do {
          userInput = input.nextLine();
          //By forcing commands to be in a format of COMMAND - ARGUMENT
          //We can easily manage the input and decide what is needed
          int substringBegin = userInput.indexOf('-');
          if(substringBegin == -1) substringBegin = userInput.length();
          String command = "";
          String[] userArgs = {};
          if(userInput.length() == substringBegin){
              command = userInput;
          }
          else{
              command = userInput.substring(0, substringBegin).trim();
              userArgs = userInput.substring(substringBegin + 1).trim().split(" ");
          }


          switch (command){
              case HELP:
                  printHelp();
                  break; 
              case SIGN_IN: 
                  SignIn(userArgs);
                  break;
              case ADD_USER:
                  AddUser(userArgs);
                  break;
              case CREATE_WORKSPACE:
                  CreateWorkspace(userArgs);
                  break;
              case JOIN_WORKSPACE:
                  JoinWorkspace(userArgs);
                  break;
              case CREATE_CHANNEL:
                  CreateChannel(userArgs);
                  break;
              case VIEW_USERS:
                  ViewUsers(userArgs);
                  break;
              case PIN_MESSAGE:
                  PinMessage(userArgs);
                  break;
              case SEND_DM:
                  SendDM(userArgs);
                  break;
              case SEND:
                  SendMessage(userArgs);
                  break;
              case LOG_MESSAGES:
                  LogMessage(userArgs);
                  break;
              case VIEW_MENTIONS:
                  ViewMentions(userArgs);
                  break;
              default:
                  System.out.println("Invalid Input please try again :(");
                  break;
          }
      } while (input.hasNextLine());

  }

    private static void ViewMentions(String[] userArgs) {
    }

    private static void LogMessage(String[] userArgs) {
    }

    private static void SignIn(String[] userArgs) {
    }

    private static void AddUser(String[] userArgs) {
        if(userArgs.length != 2) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse uResponse = User.createUser(userArgs[0], userArgs[1]);
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        }
        else {
            System.out.println("Saved User");
            User u = gson.fromJson(uResponse.response, User.class);
            thisUser = u;
        }
    }

    private static void CreateWorkspace(String[] userArgs) {
        if(userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        System.out.println("Creating Workspace...");
        DBSupport.HTTPResponse wResponse = Workspace.createWorkspace(userArgs[0]);
        if (wResponse.code > 300) {
            System.out.println(wResponse.response);
        }
        else {
            System.out.println("Saved Workspace");
            Workspace w = gson.fromJson(wResponse.response, Workspace.class);
            cur = w;
            System.out.println("Joining Workspace");
            JoinWorkspace(new String[]{ w.getName() } );
            System.out.println("Creating Default Channel");
            CreateChannel(new String[]{w.getName(), "Welcome"});
        }
    }

    private static void JoinWorkspace(String[] userArgs) {
        if(thisUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        System.out.println("Joining Workspace");
        DBSupport.HTTPResponse joinWorkspace = Workspace.joinWorkspace(userArgs[0], thisUser.getName());
        if (joinWorkspace.code > 300) {
            System.out.println(joinWorkspace.response);
        }
        else {
            Workspace w = gson.fromJson(joinWorkspace.response, Workspace.class);
            cur = w;
            System.out.println("Joined Workspace " + w.getName() + " and set it to your current workspace"); 
        }
    }

    private static void CreateChannel(String[] userArgs) {
        if (cur == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (userArgs.length != 2) {
            System.out.println("Wrong Number of arguments. Try: create channel - <workspace> <name> ");
            return;
        }
        DBSupport.HTTPResponse cResponse = Channel.createChannel(userArgs[0], userArgs[1]);
        if (cResponse.code > 300) {
            System.out.println(cResponse.response);
        }
        else {
            System.out.println("Saved Channel");
            Channel c = gson.fromJson(cResponse.response, Channel.class);
            curChannel = c;
        }
    }

    private static void ViewUsers(String[] userArgs) {
        if (cur == null) {
            System.out.println("User not in workspace");
            return;
        }
        DBSupport.HTTPResponse viewUsers = Workspace.getUsersInWorkspace(cur.getName());
        if(viewUsers.code > 300) {
            System.out.println("There are no users in this workspace");
        }
        else {
            String[] userList = gson.fromJson(viewUsers.response, String[].class);
            System.out.println("\nUsers in workspace: " + cur.getName());
            for (int i = 0; i < userList.length; i++) {
                System.out.println("\t" + userList[i]);
            }
            System.out.println("\n");
        }
    }

    private static void PinMessage(String[] userArgs) {
        if(thisUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse pinMessage = Workspace.pinMessage(userArgs[0]);
        if (pinMessage.code > 300) {
            System.out.println(pinMessage.response);
        }
        else {
            System.out.println("Pinned message");
            Message m = gson.fromJson(pinMessage.response, Message.class);
            System.out.println("Message Pinned: \n\t" +"[" + m.getwId() + "." + m.getcID() + "." + m.getId() + "]" + m.getContent());
        }
    }

    private static void SendMessage(String[] userArgs){
        if(thisUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (cur == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        if(userArgs.length < 1){
            System.out.println("Invalid number of arguments");
            return;
        }
        String message = "";
        for(int i = 0; i < userArgs.length; i++){
            message += userArgs[i];
        }
        DBSupport.HTTPResponse sendMessage = Message.sendMessage(thisUser.getName(), cur.getName(), curChannel.getName(), message);
        if (sendMessage.code > 300) {
            System.out.println(sendMessage.response);
        }
        else {
            System.out.println("Joining Workspace");
            Message m = gson.fromJson(sendMessage.response, Message.class);
            System.out.println("Message Sent: \n\t" + m.getContent());
        }
    }

    private static void SendDM(String[] userArgs){
        if(userArgs.length < 2){
            System.out.println("Invalid number of arguments");
            return;
        }
        String directMessage = "";
        for(int i = 1; i < userArgs.length; i++){
            directMessage += userArgs[i];
        }
        DBSupport.HTTPResponse dm = Message.sendDirectMessage(thisUser.getName(), userArgs[0], directMessage);
        if (dm.code > 300) {
            System.out.println(dm.response);
        }
        else {
            System.out.println("Joining Workspace");
            Message m = gson.fromJson(dm.response, Message.class);
            System.out.println("Message Sent: \n\t" + m.getContent());
        }
    }
    
    private static void printInstructions() {
        System.out.println("Welcome to Slack# (patent pending), our cheeky, user un-friendly, clone of Slack\n" +
                "\t\tTo run this god forsaken app, type in a command and its arguments.\n" +
                "\t\tIf you dont know the commands or need a refresher. I suggest you git gud skrub\n\n\n\n" +
                "\t\t(Or enter \"help\", I'm not your mommy lol)");
    }

//    private static final String CREATE_WORKSPACE = "create workspace";
//    private static final String JOIN_WORKSPACE = "join";
//    private static final String CREATE_CHANNEL = "create channel";
//    private static final String VIEW_USERS = "view users";
//    private static final String SEND = "send";
//    private static final String SEND_DM = "send to";
//    private static final String ADD_USER = "create user";
//    private static final String LOGIN = "login";
//    private static final String PIN_MESSAGE = "pin message";
    private static void printHelp(){
        System.out.println("Commands are sent in the order COMMAND - ARGUMENTS\n" +
                "using ' ' to separate arguments\n\n" +
                "create user: create user - <name> <password>\n" +
                "sign in: sign in - <username> <password>\n" +
                "create workspace: create workspace - <name of workspace>\n" +
                "join workspace: join - <name of workspace>\n" +
                "create channel: create channel - <workspace name> <channel name>\n"+
                "view users: view users\n" +
                "send to group: send - <message>\n" +
                "direct message: send to - <user> <message>\n" +
                "pin message: pin message - <message>\n" +
                "log messages: log messages\n" +
                "view mentions: view mentions\n");
    }

}
