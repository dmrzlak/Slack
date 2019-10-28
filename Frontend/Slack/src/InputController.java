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

    public static void main(String[] args){

      Scanner input = new Scanner(System.in);
      String userInput = "";
      do {
          userInput = input.nextLine();
          //By forcing commands to be in a format of COMMAND - ARGUMENT
          //We can easily manage the input and decide what is needed
          int substringBegin = userInput.indexOf('-');
          String command = userInput.substring(0, substringBegin).trim();
          switch (command){
              case CREATE_WORKSPACE:
                  Workspace.createWorkspace(userInput.substring(substringBegin + 1).trim());
                  break;
              case JOIN_WORKSPACE:

                  break;
              default:
                  System.out.println("Invalid Input please try again :(");
          }
      } while (input.hasNextLine());

  }


}