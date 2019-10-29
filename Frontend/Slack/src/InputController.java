import Models.User;
import Models.Workspace;

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

      Scanner input = new Scanner(System.in);
      String userInput = "";

      System.out.println("Please enter username:\n");
      User current = new User();
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
                  workspaceName = userInput.substring(substringBegin + 1).trim()
                  Workspace.createWorkspace(workspaceName);
                  break;
              case JOIN_WORKSPACE:
                  Workspace.j
                  break;
              case SEND:

                  break;
              case SEND_DM:

                  break;

                  default:
                  System.out.println("Invalid Input please try again :(");
          }
      } while (input.hasNextLine());

  }


}