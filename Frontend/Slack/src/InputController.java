
import Controllers.DBSupport;
import Models.User;
import Models.Workspace;
import com.google.gson.Gson;

import java.util.Scanner;

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
    private static String workspaceName;

    public static void main(String[] args){
        Gson gson = new Gson();
        User thisUser = null;
      Scanner input = new Scanner(System.in);
      String userInput = "";

      System.out.println("Please enter username:\n");
      User current = new User("", "");
      System.out.println("Please enter your password!\n");
      String password = input.nextLine();

      do {
          userInput = input.nextLine();
          //By forcing commands to be in a format of COMMAND - ARGUMENT
          //We can easily manage the input and decide what is needed
          int substringBegin = userInput.indexOf('-');
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
                  DBSupport.HTTPResponse joinWorkspace = Workspace.joinWorkspace(userInput.substring(substringBegin + 1).trim(), thisUser.getName());
                  if (joinWorkspace.code > 300) {
                      System.out.println(joinWorkspace.response);
                  } else {
                      System.out.println("Joining Workspace");
                      Workspace w = gson.fromJson(joinWorkspace.response, Workspace.class);
                  }
                  break;
              case SEND_DM:

                  break;

                  default:
                  System.out.println("Invalid Input please try again :(");
          }
      } while (input.hasNextLine());

  }


}