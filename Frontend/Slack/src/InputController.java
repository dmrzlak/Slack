import Controllers.DBSupport;
import Models.User;
import Models.Workspace;
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

    public static void main(String[] args){
        Gson gson = new Gson();
        User thisUser = null;
        Workspace cur = null;
      Scanner input = new Scanner(System.in);
      String userInput = "";
      do {
          userInput = input.nextLine();
          //By forcing commands to be in a format of COMMAND - ARGUMENT
          //We can easily manage the input and decide what is needed
          int substringBegin = userInput.indexOf('-');
          if(substringBegin == -1) substringBegin = 0;
          String command = userInput.substring(0, substringBegin).trim();
          switch (command){
              case CREATE_WORKSPACE:
                  DBSupport.HTTPResponse wResponse = Workspace.createWorkspace(userInput.substring(substringBegin + 1).trim());
                  if (wResponse.code > 300) {
                      System.out.println(wResponse.response);
                  } else {
                      System.out.println("Saved Workspace");
                      Workspace w = gson.fromJson(wResponse.response, Workspace.class);
                  }
                  break;
              case JOIN_WORKSPACE:
                  DBSupport.HTTPResponse joinWorkspace = Workspace.joinWorkspace(userInput.substring(substringBegin + 1).trim(), "dylan3");
                  if (joinWorkspace.code > 300) {
                      System.out.println(joinWorkspace.response);
                  } else {
                      System.out.println("Joining Workspace");
                      Workspace w = gson.fromJson(joinWorkspace.response, Workspace.class);
                      cur = w;
                  }
                  break;
              case CREATE_CHANNEL:
                  if (cur == null) {
                      System.out.println("User not in workspace");
                      break;
                  }
                  if (userInput.substring(substringBegin + 1).trim().slice().size() > 2) {
                      System.out.println("Too many arguments. Try: create channel - <workspace> \"name\" ");
                  }
                  DBSupport.HTTPResponse cResponse = Channel.createChannel(userInput.substring((substringBegin + 1), substring.indexOf(' ')),
                                                                                                userInput.substring((substringBegin + 1).trim().indexOf(' ') + 1));
                  if (cResponse.code > 300) {
                      System.out.println(cResponse.response);
                  } else {
                      System.out.println("Saved Channel");
                      Channel c = gson.fromJson(cResponse.response, Channel.class);
                  }
                  break;
              case VIEW_USERS:
                  DBSupport.HTTPResponse viewUsers = Workspace.getUsersInWorkspace(userInput.substring(substringBegin + 1).trim());
                  if(viewUsers.code > 300) {
                      System.out.println("There are no users in this workspace");
                  }
                  System.out.println(viewUser.response);
                  break;

              default:
                  System.out.println("Invalid Input. Please try again :(");
          }
      } while (input.hasNextLine());

  }


}