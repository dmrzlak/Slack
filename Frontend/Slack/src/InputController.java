import Controllers.DBSupport;
import Models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;


/**
 * This will be the main controller for the application.
 * It will take the initial input for User Input
 * and then pass it along to other classes to handle the actual functionality
 *
 * @Author Dylan Mrzlak
 * Original Framework and Use handle for CREATE_WORKSPACE and JOIN_WORKSPACE
 */
public class InputController {
    private static final String CREATE_WORKSPACE = "create workspace";
    private static final String JOIN_WORKSPACE = "join workspace";
    private static final String CREATE_CHANNEL = "create channel";
    private static final String VIEW_USERS = "view users";
    private static final String CLEAR_USER = "delete user";
    private static final String SEND = "send";
    private static final String SEND_DM = "send to";
    private static final String ADD_USER = "create user";
    private static final String PIN_MESSAGE = "pin message";
    private static final String UNPIN_MESSAGE = "unpin message";
    private static final String LOG_MESSAGES = "log messages";
    private static final String VIEW_MENTIONS = "view mentions";
    private static final String GET_PINNED = "get pinned";
    private static final String CHANGE_ROLE = "change role";
    private static final String LOGIN = "login";
    private static final String HELP = "help";
    private static final String VIEW_FRIENDS = "view friends";
    private static final String ADD_FRIEND = "add friend";
    private static final String SWITCH_WORKSPACE = "switch workspace";
    private static final String SWITCH_CHANNEL = "switch channel";
    private static final String DELETE_FRIEND = "delete friend";
    private static final String SEARCH_WORKSPACE = "search workspace";
    private static final String SEARCH_USER = "search user";
    private static final String FAVORITE_MESSAGE = "favorite message";
    private static final String UNFAVORITE_MESSAGE = "unfavorite message";
    private static final String GET_FAVORITES = "veiw favorites";
    private static final String SEND_TEXTFILE = "send file";
    private static final String DOWNLOAD_TEXTFILE = "download file";
    private static final String SET_STATUS = "set status";
    private static final String DELETE_STATUS = "delete status";
    private static final String VIEW_STATUS = "view status";
    private static final String KICK_USER = "kick";
    private static final String UNKICK_USER = "unkick";
    private static final String CREATE_APPOINTMENT = "create appointment";
    private static final String VIEW_APPOINTMENTS = "view appointments";
    private static final String RESPOND_APPOINTMENT = "respond appointment";
    private static final String DELETE_APPOINTMENT = "delete appointment";

    //If this line get mad, check your dependencies, may have dropped
    private static Gson gson = new Gson();
    private static User curUser = null;
    private static Workspace curWorkspace = null;
    private static Channel curChannel = null;
    private static final String[] roles = new String[]{"USER", "MOD", "ADMIN"};
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String userInput = "";

        printInstructions();
        do {
            userInput = input.nextLine();
            //By forcing commands to be in a format of COMMAND - ARGUMENT
            //We can easily manage the input and decide what is needed
            int substringBegin = userInput.indexOf('-');
            //Now that we have commands without args, we need to be able to take commands without the delimitter set
            if (substringBegin == -1) substringBegin = userInput.length();
            String command = "";
            String[] userArgs = {};
            if (userInput.length() == substringBegin) {
                command = userInput;
            } else {
                command = userInput.substring(0, substringBegin).trim();
                userArgs = userInput.substring(substringBegin + 1).trim().split(" ");
            }
            //Have updated the switch to be more readable and move into methods, rather than holding all the logic in here.
            //We were simply expanding this too much that it was becoming hard to read. This is much more followable
            switch (command) {
                case HELP:
                    printHelp();
                    break;
                case LOGIN:
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
                case UNPIN_MESSAGE:
                    UnpinMessage(userArgs);
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
                case VIEW_FRIENDS:
                    ViewFriends(userArgs);
                    break;
                case ADD_FRIEND:
                    AddFriend(userArgs);
                    break;
                case SWITCH_WORKSPACE:
                    SwitchWorkspace(userArgs);
                    break;
                case SWITCH_CHANNEL:
                    SwitchChannel(userArgs);
                    break;
                case DELETE_FRIEND:
                    DeleteFriend(userArgs);
                case SEARCH_USER:
                    searchUser(userArgs);
                    break;
                case SEARCH_WORKSPACE:
                    searchWorkspace(userArgs);
                    break;
                case DOWNLOAD_TEXTFILE:
                    downloadTextfile(userArgs);
                    break;
                case SEND_TEXTFILE:
                    sendTextfile(userArgs);
                    break;
                case GET_PINNED:
                    GetPinned(userArgs);
                    break;
                case CHANGE_ROLE:
                    ChangeRole(userArgs);
                    break;
                case FAVORITE_MESSAGE:
                    favoriteMessage(userArgs);
                    break;
                case UNFAVORITE_MESSAGE:
                    unfavoriteMessage(userArgs);
                    break;
                case GET_FAVORITES:
                    getFavorites(userArgs);
                    break;
                case SET_STATUS:
                    SetStatus(userArgs);
                    break;
                case DELETE_STATUS:
                    DeleteStatus(userArgs);
                    break;
                case VIEW_STATUS:
                    ViewStatus(userArgs);
                    break;
                case KICK_USER:
                    KickUser(userArgs);
                    break;
                case UNKICK_USER:
                    UnkickUser(userArgs);
                    break;
                case CREATE_APPOINTMENT:
                    createAppointment();
                    break;
                case VIEW_APPOINTMENTS:
                    getAppointments(userArgs);
                    break;
                case DELETE_APPOINTMENT:
                    deleteAppointment(userArgs);
                    break;
                case RESPOND_APPOINTMENT:
                    respondAppointment(userArgs);
                    break;
                case CLEAR_USER:
                    clearUser();
                    break;
                default:
                    System.out.println("Invalid Input please try again :(");
                    break;
            }
        } while (input.hasNextLine());

    }
    private static void getFavorites(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        DBSupport.HTTPResponse response = Channel.veiwFavorites(curWorkspace.getId(),curChannel.getId(),curUser.getId());
        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            Message[] favorites = gson.fromJson(response.response, Message[].class);
            System.out.println("These are the your favorited messages:");
            for (Message favorite : favorites) {
                String printMention = "\t" + favorite.getContent();
                System.out.println(printMention);
            }
        }
    }
    private static void unfavoriteMessage(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        DBSupport.HTTPResponse uResponse = Channel.unFavoriteMessage(curUser.getId(),Integer.parseInt(userArgs[0]));
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        } else {
            System.out.println("Un-Favorited Message");
        }

    }

    private static void favoriteMessage(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        DBSupport.HTTPResponse uResponse = Channel.favoriteMessage(curUser.getId(),Integer.parseInt(userArgs[0]));
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        } else {
            System.out.println("Favorited Message");
        }

    }

    private static void sendTextfile(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        String filename = userArgs[0];
        if(filename.contains("/")){
            filename = filename.substring( filename.lastIndexOf('/') + 1);
        }
        else{
            filename = filename.substring( filename.lastIndexOf('\\') + 1);
        }

        String content = "";
        File toRead = new File(userArgs[0]);
        if(toRead.exists()){
            Scanner scan = null;
            try {
                scan = new Scanner(toRead);
                String temp;
                while (scan.hasNextLine()) {
                    temp = scan.nextLine();
                    content += temp + "\n";
                }
                scan.close();
                content = ReplaceSpecChars(content);
                filename= filename.substring(0, filename.length() - 4);
                DBSupport.HTTPResponse response = Textfile.sendText(filename, content);

                if (response.code >= 300) {
                    System.out.println(response.response);
                } else {
                    System.out.println("file sent!");
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("File not found, please try again");
        }
    }

    private static void downloadTextfile(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        String filename = userArgs[0];

        DBSupport.HTTPResponse response = Textfile.getText(filename);

        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            Textfile t = gson.fromJson(response.response, Textfile.class);
            String[] file = t.getContent().split("\n");
            WriteFile(file,"..\\..\\files\\", "\\" + t.getName());
        }
    }


    /**
     * Takes "no" arguments and will print the mentions for the current user in a channel.
     * Our mentions right now just search for a username, but can simply be expanded to be "@/USERNAME/"
     * @param userArgs
     * @author Dylan Mrzlak
     */
    private static void ViewMentions(String[] userArgs) {
        //View mentions does not need user args whatsoever, so we'll just ignore them. They are passed in for consistency
        //First we want to make sure that nothing is null (We want to be in a workspace and a channel, and then the user needs to be signed in
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        //Now that those are out of the way, we need to get the actual mentions
        // depending on what the server returns, we handle it accordingly. We either get a list and print it,
        //  or an error and print that
        DBSupport.HTTPResponse response = Channel.viewMentions(curUser.getName(), curWorkspace.getName(), curChannel.getName());
        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            Message[] mentions = gson.fromJson(response.response, Message[].class);
            System.out.println("These are the your mentions:");
            for (Message mention : mentions) {
                String printMention = "\t" + mention.getContent();
                System.out.println(printMention);
            }
        }

    }

    /**
     * Take the messages from a workspace, grouped by channel and order based on time. The write it to a file
     * @param userArgs
     * @author Dylan Mrzlak
     */
    private static void LogMessage(String[] userArgs) {
        //Logging does not need user args whatsoever, so we'll just ignore them. They are passed in for consistency
        //First we want to make sure that nothing is null (We want to be in a workspace and a channel, and then the user needs to be signed in
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        //Now that those are out of the way, we need to get the actual messages.
        // They will be grouped by channel in the backend,
        // but we do some lifting here as well to properly make the strings we need
        //Note like with all of our methods, we can get an error or the data we want so we have to deal with it properly
        System.out.println("Getting the messages for: " + curWorkspace.getName());
        DBSupport.HTTPResponse response = Message.getAllMessages(curWorkspace.getName());
        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            System.out.println("Retrieval for: " + curWorkspace.getName() + " successful");
            Message[] messages = gson.fromJson(response.response, Message[].class);
            String workspaceName = curWorkspace.getName();
            //There is a real possibility that this could take a long time (and it's , so I'm going to run it asynchronously maybe later)
            //For the log file, as of now, we'll put it into the out folder under a folder logs
            //and with the name:
            //      "LOG_<WORKSPACENAME>_<DATE>
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
            Date date = new Date();
            String fileName = "\\LOG_" + workspaceName + "_" + dateFormat.format(date);
            System.out.println("Formatting");
            //We want to format the data as we want, and then take the new list and write the file with it
            String[] linesToWrite = LogMessagesFormat(messages);
            System.out.println("Writing");
            //write said file
            WriteFile(linesToWrite, "..\\..\\logs\\", fileName);
        }
    }

    /**
     * searches for user
     * args name of user
     */
    private static void searchWorkspace(String[] userArgs){
            if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        String Wname;
        if(userArgs.length == 0){
            Wname = "-1";
        }else{
            Wname = userArgs[0];
        }
        System.out.println("Searching for workspace...");
        DBSupport.HTTPResponse response = Workspace.searchWorkspace(Wname);
        if (response.code >= 300) {
            System.out.println(response.response);
        }
        else {
            System.out.println("Workspaces like: " + Wname);
            Workspace[] workspacesFound = gson.fromJson(response.response, Workspace[].class);
            for(int i = 0; i < workspacesFound.length;i++) {
                System.out.println(workspacesFound[i].getName());
            }
        }
    }

    private static void searchUser(String[] userArgs){
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        String Uname;
        if(userArgs.length == 0){
            Uname = "-1";
        }else{
            Uname = userArgs[0];
        }
        System.out.println("Searching for User...");
        DBSupport.HTTPResponse response = User.searchUser(Uname);
        if (response.code >= 300) {
            System.out.println(response.response);
        }
        else {
            System.out.println("Users with name like: " + Uname);
            User[] userFound = gson.fromJson(response.response, User[].class);
            for(int i = 0; i < userFound.length;i++) {
                System.out.println("\t" + userFound[i].getName());
            }
        }
    }

    /**
     * Sign the user in (if the arguments are correct) and set them to the current user
     * @param userArgs
     * @author Logan Garrett
     */
    private static void SignIn(String[] userArgs) {
        if (userArgs.length != 2) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        //Either the user put in the right username and password, or they did not.
        //If they did not, tell them with the error
        //If they did, then the user is signed in and set the user to the user returned from the server
        DBSupport.HTTPResponse uResponse = User.signIn(userArgs[0], userArgs[1]);
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        } else {
            System.out.println("Login Successful");
            User u = gson.fromJson(uResponse.response, User.class);
            curUser = u;
            curChannel = null;
            curWorkspace = null;
        }
    }

    /**
     * Create user in the server, then set the user to that
     * @param userArgs
     */
    private static void AddUser(String[] userArgs) {
        if (userArgs.length != 2) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse uResponse = User.createUser(userArgs[0], userArgs[1]);
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        } else {
            System.out.println("Saved User");
            User u = gson.fromJson(uResponse.response, User.class);
            curUser = u;
        }
    }

    /**
     * Create a new workspace (if possible) and then set the user's current workspace to that
     * @param userArgs
     * @author dylan mrzlak
     */
    private static void CreateWorkspace(String[] userArgs) {
        if (userArgs.length != 1) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        System.out.println("Creating Workspace...");
        DBSupport.HTTPResponse wResponse = Workspace.createWorkspace(userArgs[0]);
        if (wResponse.code > 300) {
            System.out.println(wResponse.response);
        } else {
            System.out.println("Saved Workspace");
            Workspace w = gson.fromJson(wResponse.response, Workspace.class);
            curWorkspace = w;
            System.out.println("Joining Workspace");
            //Whenever a new workspace is created, the creator should automatically join it
            JoinWorkspace(new String[]{w.getName()});
            //We don't want to force a user to need to create a channel just to send messages, so now we automatically
            //make one, We can change the name whenever, but every new workspace gets the same name for its first channel
            System.out.println("Creating Default Channel");
            CreateChannel(new String[]{w.getName(), "Welcome"});
            ChangeRole(new String[]{"ADMIN", curUser.getName()});
        }
    }

    /**
     * Join a workspace (put data into the server that the user is in the workspace) and then make the workspace the current
     * @param userArgs
     * @author Dylan Mrzlak
     */
    private static void JoinWorkspace(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        System.out.println("Joining Workspace");
        DBSupport.HTTPResponse joinWorkspace = Workspace.joinWorkspace(userArgs[0], curUser.getName());
        if (joinWorkspace.code > 300) {
            System.out.println(joinWorkspace.response);
        } else {
            Workspace w = gson.fromJson(joinWorkspace.response, Workspace.class);
            curWorkspace = w;
            System.out.println("Joined Workspace " + w.getName() + " and set it to your current workspace");
        }
    }

    /**
     * Create a new channel (if possible) and then set the user's current channel to that
     * @param userArgs
     * @author dylan mrzlak
     */
    private static void CreateChannel(String[] userArgs) {
        if (curWorkspace == null) {
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
        } else {
            System.out.println("Saved Channel");
            Channel c = gson.fromJson(cResponse.response, Channel.class);
            curChannel = c;
        }
    }

    /**
     * Get all of the users in a workspace, this is not a focused search at all.
     * Simply a full list of users that are marked as having joined
     * @param userArgs
     * @author logan garrett
     */
    private static void ViewUsers(String[] userArgs) {
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        //We either get an error and want to consume it, or we get a list to print
        DBSupport.HTTPResponse viewUsers = Workspace.getUsersInWorkspace(curWorkspace.getName());
        if (viewUsers.code > 300) {
            System.out.println("There are no users in this workspace");
        } else {
            String[] userList = gson.fromJson(viewUsers.response, String[].class);
            System.out.println("\nUsers in workspace: " + curWorkspace.getName());
            for (int i = 0; i < userList.length; i++) {
                System.out.println("\t" + userList[i]);
            }
            System.out.println(userList.length + " Users in this workspace found. \n");
        }
    }

    private static void GetPinned(String[] userArgs) {
        if (curWorkspace == null) {
            System.out.println("You are not in a workspace\n");
            return;
        }
        if (curChannel == null) {
            System.out.println("You are not in a channel\n");
            return;
        }
        //get messages, then return them
        System.out.println("Getting the pinned messages for: " + curChannel.getName());
        DBSupport.HTTPResponse response = Message.getPinnedMessages(curWorkspace.getName(), curChannel.getName());
        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            System.out.println("Retrieval for: " + curChannel.getName() + " successful");
            Message[] messages = gson.fromJson(response.response, Message[].class);
            int i = 0;
            while (i < messages.length) {
                System.out.println("Message ID: " + messages[i].getId() +
                        ", Sender ID: " + messages[i].getSenderId() +
                        ", Message: " + messages[i].getContent());
                i++;
            }
            System.out.println(messages.length + " pins found. \n");
        }
    }

    /**
     * Mark a message as pinned (when marked as pinned a pin search will be able to get them)
     * A pinned message is technically important to the channel (but we're not enforcing that and leaving that to the users)
     * @param userArgs
     * @author Joe Hudson
     */
    private static void PinMessage(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse pinMessage = Workspace.pinMessage(userArgs[0]);
        if (pinMessage.code > 300) {
            System.out.println(pinMessage.response);
        } else {
            System.out.println("Pinned message");
            Message m = gson.fromJson(pinMessage.response, Message.class);
            System.out.println("Message Pinned: \n\t" + "[" + m.getwId() + "." + m.getcID() + "." + m.getId() + "]"
                    + m.getContent());
        }
    }

    private static void UnpinMessage(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse unpinMessage = Workspace.unpinMessage(userArgs[0]);
        if (unpinMessage.code > 300) {
            System.out.println(unpinMessage.response);
        } else {
            System.out.println("Pinned message");
            Message m = gson.fromJson(unpinMessage.response, Message.class);
            System.out.println("Message Unpinned: \n\t" + "[" + m.getwId() + "." + m.getcID() + "." + m.getId() + "]"
                    + m.getContent());
        }
    }

     private static void ChangeRole(String[] userArgs) {
         if (curUser == null) {
             System.out.println("You need to create a user or sign in to continue");
             return;
         }
         if (curWorkspace == null) {
             System.out.println("User not in workspace");
             return;
         }
         if (curChannel == null) {
             System.out.println("User not in Channel;");
             return;
         }
        if (userArgs.length != 2) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        //1 user, 2 moderator, 3 admin
        int role;
        String strRole;
        String toComp = userArgs[0].toUpperCase();
        switch(toComp){
            case "USER":
                role = 1;
                strRole = "User";
                break;
            case "MOD":
                role = 2;
                strRole = "Moderator";
                break;
            case "ADMIN":
                role = 3;
                strRole = "Admin";
                break;
            default:
                System.out.println("Invalid Role name input");
                return;
        }
        DBSupport.HTTPResponse changeRole = Workspace.changeRole(curWorkspace.getName(), userArgs[1], role);
        if (changeRole.code > 300) {
            System.out.println(changeRole.response);
        } else {
            System.out.println("Changed role of " + userArgs[1] + " to " + strRole);
        }
    }

    /**
     * Send a message to the channel. Takes the content and will put it into the server.
     * @param userArgs
     * @author thomas mcandrew
     */
    private static void SendMessage(String[] userArgs) {
        //null checks for the stuff that's required to send a message
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        if (curChannel == null) {
            System.out.println("User not in Channel;");
            return;
        }
        if (userArgs.length < 1) {
            System.out.println("Invalid number of arguments");
            return;
        }
        //Format the message in a way that the data can be sent fully, uncorrupted
        //using %20 to replace 'spaces' in the message
        //We don't use http bodies, so the url is not a fan of spaces
        String message = "";
        for (int i = 0; i < userArgs.length; i++) {
            message += userArgs[i] + " ";
        }
        message = message.trim();
        message = ReplaceSpecChars(message);
        //Send the message to the server, and acknowledge the search
        DBSupport.HTTPResponse sendMessage = Message.sendMessage(curUser.getName(), curWorkspace.getName(), curChannel.getName(), message);
        if (sendMessage.code > 300) {
            if(sendMessage.code == 403) {
                curWorkspace = null;
                curChannel = null;
            }

            System.out.println(sendMessage.response);
        } else {
            Message m = gson.fromJson(sendMessage.response, Message.class);
            System.out.println("Message Sent " + m.getId() + ": \n\t" + m.getContent());
        }
    }

    /**
     * Send a message to a user. Takes the content and will put it into the server.
     * DM's will be able to be seen by a user when a search for dm's is run
     * @param userArgs
     * @author thomas mcandrew
     */
    private static void SendDM(String[] userArgs) {
        if (userArgs.length < 2) {
            System.out.println("Invalid number of arguments");
            return;
        }
        //Format the message in a way that the data can be sent fully, uncorrupted
        //using %20 to replace 'spaces' in the message
        //We don't use http bodies, so the url is not a fan of spaces
        String directMessage = "";
        for (int i = 1; i < userArgs.length; i++) {
            directMessage += userArgs[i] + " ";
        }
        directMessage = directMessage.trim();
        directMessage = ReplaceSpecChars(directMessage);
        DBSupport.HTTPResponse dm = Message.sendDirectMessage(curUser.getName(), userArgs[0], directMessage);
        if (dm.code > 300) {
            System.out.println(dm.response);
        } else {
            Message m = gson.fromJson(dm.response, Message.class);

            System.out.println("Message Sent: \n\t" + m.getContent());
        }
    }

    private static void ViewFriends(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        DBSupport.HTTPResponse viewFriends = User.viewFriends(curUser.getId());
        if (viewFriends.code > 300) {
            System.out.println("You're a lonely one.");
        } else {
            String[] friendList = gson.fromJson(viewFriends.response, String[].class);
            System.out.println("\nFriend list: ");
            for (int i = 0; i < friendList.length; i++) {
                System.out.println("\t" + friendList[i]);
            }
            System.out.println(friendList.length + " friends found. \n");
        }
    }

    private static void AddFriend(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse addFriend = User.addFriend(curUser.getName(), userArgs[0]);
        if (addFriend.code > 300) {
            System.out.println(addFriend.response);
        } else {
            User u = gson.fromJson(addFriend.response, User.class);
            System.out.println("Added friend " + u.getName());
        }
    }

    private static void SwitchWorkspace(String[] userArgs) {
        if (curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (curWorkspace != null && curWorkspace.getName().equals(userArgs[0])) {
            System.out.println("You are already in this workspace!");
            return;
        }
        DBSupport.HTTPResponse switchWorkspace = Workspace.switchWorkspace(userArgs[0], curUser.getId());
        if (switchWorkspace.code > 300) {
            System.out.println(switchWorkspace.response);
        } else {
            //Find the workspace first
            DBSupport.HTTPResponse wResponse = Workspace.getWorkspaceByName(userArgs[0]);
            if (wResponse.code > 300) {
                System.out.println(wResponse.response);
            }
            Workspace w = gson.fromJson(wResponse.response, Workspace.class);
            curWorkspace = w;

            //Then set to default channel
            DBSupport.HTTPResponse cResponse = Channel.getChannelByName(userArgs[0], "Welcome");
            if (cResponse.code > 300) {
                System.out.println(cResponse.response);
            } else {
                Channel c = gson.fromJson(cResponse.response, Channel.class);
                curChannel = c;
                System.out.println("Changed workspace and entered default channel");
            }
        }
    }

    private static void SwitchChannel(String[] userArgs) {
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        DBSupport.HTTPResponse response = Channel.Switch(curWorkspace.getName(), userArgs[0], curUser.getId());
        if(response.code > 300) {
            System.out.println(response.response);
        } else {
            Channel c = gson.fromJson(response.response, Channel.class);
            curChannel = c;
            System.out.println("Changed Channel");
        }
    }

    private static void DeleteFriend(String[] userArgs) {
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse deleteFriend = User.deleteFriend(curUser.getName(), userArgs[0]);
        if (deleteFriend.code > 300) {
            System.out.println(deleteFriend.response);
        } else {
            User u = gson.fromJson(deleteFriend.response, User.class);
            System.out.println("Removed friend " + u.getName());
        }
    }
    
    private static void clearUser(){
        if(curUser == null){
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        DBSupport.HTTPResponse clear = User.clearUser(curUser.getName());
        System.out.println(clear.response);
    }

    public static void createAppointment() {
        if(curUser == null){
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        //Will be changing the userArgs for this one...
        System.out.println("Enter the name of the appointment");
        String name = input.nextLine().trim();
        System.out.println("Enter the description of the appointment");
        String description = input.nextLine().trim();
        System.out.println("Enter the date of the appointment in the form of YYYY-MM-DD hh:mm");
        String timeString = (input.nextLine() + ":00").trim();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date time = null;
        try {
            time = dateFormat.parse(timeString);
        } catch (ParseException e) {
            System.out.println("Invalid Date, Try Again");
        }


        name = ReplaceSpecChars(name);
        description = ReplaceSpecChars(description);
        timeString = ReplaceSpecChars(timeString);

        DBSupport.HTTPResponse aRes = Appointment.createAppointment(name, description, timeString, curUser.getId());
        if (aRes.code > 300) {
            System.out.println(aRes.response);
        } else {
            Appointment a = gson.fromJson(aRes.response, Appointment.class);
            System.out.println("Successfully made appointment " + a.getName());
            String userIn = "";
            while(userIn.length() < 1 || ( userIn.length() >= 1 && !(userIn.charAt(0) == 'n' || userIn.charAt(0) == 'y'))){
                System.out.println("Would you like to send invites? (Y/N)");
                userIn = input.nextLine().toLowerCase();
            }
            if(userIn.length() >= 1 && userIn.charAt(0) == 'y'){
                sendInvite(a);
            }
            else if (userIn.length() >= 1){
                System.out.println("No invitations will be sent.");
                return;
            }
        }
    }

    public static void sendInvite(Appointment a){
        String userIn = "";
        while(userIn.length() < 1 ){
            System.out.println("Write the usernames of the users you want to send this invite to in a comma delimited list");
            userIn = input.nextLine().toLowerCase();
        }
        ArrayList<String> userNameArrList = new ArrayList<>();
        //Turn user input into list
        userNameArrList.addAll(Arrays.asList(userIn.split(",")));
        //We want to get all of the user ids for the call.
        // Makes it a little easier for the request than a huge string of usernames
        ArrayList<Integer> userIds = new ArrayList<>();
        for (String username : userNameArrList) {
            username = username.trim();
            DBSupport.HTTPResponse idRes = User.getUserIdByName(username);
            if(idRes.code >= 300){
                continue;
            }
            else{
                userIds.add(Integer.parseInt(idRes.response));
            }
        }
        //If it's empty then none of those users exist
        if(userIds.isEmpty()){
            System.out.println("Failed to send any invites.");
            return;
        }
        //Else some users exist and we want to attempt to send invites to them
        DBSupport.HTTPResponse invRes = Appointment.sendInvite(a.getId(), userIds);
        //We got an error from the server.
        // Note the server doesn't send any error for this request,
        //So the only error would be a 500 error
        if(invRes.code >= 300){
            System.out.println(invRes.response);
            return;
        }
        else{
            //We want to signify which inivtes succeeded and which failed
            ArrayList<String> successfulInvites = new ArrayList<>();
            successfulInvites.addAll(Arrays.asList(gson.fromJson(invRes.response, String[].class)));
            //Remove successful invites and from the user given list and we are left with the successful invites
            String successes = "\n\t";
            for(String successfulName : successfulInvites){
                //Create the string of successes while removing successes from the users
                //this will save us another loop
                successes += successfulName + "\n\t";
                userNameArrList.remove(successfulName);
            }
            System.out.println("Invite successful to:" + successes);

            System.out.println("Invite unsuccessful to:" );
            for(String unsuccessfulName : userNameArrList){
                System.out.println("\n\t" + unsuccessfulName);
            }
        }
    }

    public static void getAppointments(String[] userArgs){
        if(curUser == null){
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(userArgs.length > 1){
            System.out.println("Invalid number of arguments");
            return;
        }
        boolean accepted = false;
        boolean pending = false;
        //if no args -- all appointments
        if(userArgs.length == 0){
            pending = true;
            accepted = true;
        }
        else if(userArgs[0].equalsIgnoreCase("pending"))
            pending = true;
        else if(userArgs[0].equalsIgnoreCase("accepted"))
            accepted = true;
        else{
            System.out.println("Could not find the expected arguments. Try: \n" +
                    VIEW_APPOINTMENTS + ", \n" + VIEW_APPOINTMENTS + " - accepted" +
                    ", \n or" + VIEW_APPOINTMENTS + " - pending");
            return;
        }

        DBSupport.HTTPResponse viewRes = Appointment.getAppointments(curUser.getName(), accepted, pending);
        if(viewRes.code >= 300){
            System.out.println(viewRes.response);
            return;
        }
        else{
            Appointment[] appts = gson.fromJson(viewRes.response, Appointment[].class);
            int i = 0;
            if(pending){
                System.out.println("Pending invites: ");
                for(; i < appts.length; i++){
                    if(!appts[i].isAccepted())
                    {
                        System.out.println("\n" + appts[i].toString());
                    }
                    else break;
                }
            }
            if(accepted){
                System.out.println("Accepted invites: ");
                for(; i < appts.length; i++){
                    if(appts[i].isAccepted()) {
                        System.out.println("\n" + appts[i].toString());
                    }
                    else break;
                }
            }
        }
    }

    public static void deleteAppointment(String[] userArgs){
        if(curUser == null){
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(userArgs.length != 1){
            System.out.println("Invalid number of arguments");
            return;
        }
        int aId = -1;
        try {
            aId = Integer.parseInt(userArgs[0]);
        }
        catch(NumberFormatException e){
            System.out.println("First argument not a number, expecting the Appointment ID");
            return;
        }
        DBSupport.HTTPResponse response = Appointment.deleteAppointment(curUser.getName(), aId);
        System.out.println(response.response);

    }

    public static void respondAppointment(String[] userArgs){
        if(curUser == null){
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(userArgs.length != 2){
            System.out.println("Invalid number of arguments");
            return;
        }
        int aId = -1;
        try {
            aId = Integer.parseInt(userArgs[0]);
        }
        catch(NumberFormatException e){
            System.out.println("First argument not a number, expecting the Appointment ID");
            return;
        }
        if(!(userArgs[1].equalsIgnoreCase("YES") ||userArgs[1].equalsIgnoreCase("NO"))){
            System.out.println("Second Argument not yes or no, expected a yes or no");
        }
        boolean accept = userArgs[1].equalsIgnoreCase("YES");
        DBSupport.HTTPResponse response = Appointment.respondAppointment(curUser.getName(), aId, accept);
        System.out.println(response.response);
    }


    //CHECKPOINT joeIter3
    private static void SetStatus(String[] userArgs){
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length == 0) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        String status = "";
        for (int i = 0; i < userArgs.length; i++) {
            status += userArgs[i] + " ";
        }
        status = status.trim();
        status = ReplaceSpecChars(status);
        DBSupport.HTTPResponse setStatus = User.setStatus(curUser.getName(), status);
        if (setStatus.code > 300) {
            System.out.println(setStatus.response);//Should never happen
        } else {
            System.out.println("Successfully changed your status to: \n" + setStatus.response);
        }
    }

    private static void DeleteStatus(String[] userArgs){
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 0) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        DBSupport.HTTPResponse deleteStatus = User.deleteStatus(curUser.getName());
        if (deleteStatus.code > 300) {
            System.out.println(deleteStatus.response);//Should never happen
        } else {
            System.out.println("Successfully deleted your status message");
        }
    }

    private static void ViewStatus(String[] userArgs){
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        DBSupport.HTTPResponse viewStatus = User.viewStatus(userArgs[0]);
        if (viewStatus.code > 300) {
            System.out.println(viewStatus.response);
        } else {
            System.out.println("Viewing " + userArgs[0] + "'s status.");
            System.out.println(userArgs[0] + "'s status: "+ viewStatus.response);
        }
    }

    private static void KickUser(String[] userArgs){
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(curWorkspace == null){
            System.out.println("You need to enter a workspace to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        DBSupport.HTTPResponse kickUser = User.kickUser(curWorkspace.getName(), curUser.getName(), userArgs[0]);
        if (kickUser.code > 300) {
            System.out.println(kickUser.response);
        } else {
            System.out.println("You have successfully kicked " + userArgs[0] + " from " + curWorkspace.getName());
        }
    }

    private static void UnkickUser(String[] userArgs){
        if(curUser == null) {
            System.out.println("You need to create a user or sign in to continue");
            return;
        }
        if(curWorkspace == null){
            System.out.println("You need to enter a workspace to continue");
            return;
        }
        if (userArgs.length != 1) {
            System.out.println("Invalid Number of Arguments");
            return;
        }
        DBSupport.HTTPResponse unkickUser = User.unkickUser(curWorkspace.getName(), curUser.getName(), userArgs[0]);
        if (unkickUser.code > 300) {
            System.out.println(unkickUser.response);
        } else {
            System.out.println("You have successfully unkicked " + userArgs[0] + " from " + curWorkspace.getName());
        }
    }

    //////////////////////
    //                  //
    //      Helpers     //
    //                  //
    //////////////////////
    /**
     * Print the base instructions for the app, just a welcome to the app and a short description on how to operate it
      */
    private static void printInstructions() {
        System.out.println("Welcome to Slack# (patent pending), our cheeky, user un-friendly, clone of Slack\n" +
                "\t\tTo run this god forsaken app, type in a command and its arguments.\n" +
                "\t\tIf you dont know the commands or need a refresher. I suggest you git gud skrub\n\n\n\n" +
                "\t\t(Or enter \"help\", I'm not your mommy lol)");
    }

    /**
     * Print the commands that have been implemented thus far
     */

    private static void printHelp() {
        System.out.println("Commands are sent in the order COMMAND - ARGUMENTS\n" +
                "using ' ' to separate arguments\n\n" +
<<<<<<< HEAD
                "create user:           create user - <name> <password>\n" +
                "login:                 login - <username> <password>\n" +
                "create workspace:      create workspace - <name of workspace>\n" +
                "join workspace:        join - <name of workspace>\n" +
                "switch workspace:      switch workspace - <workspace name>\n" +
                "search workspace:      search workspace - <name of workspace> (Search Field not required)\n" +
                "create channel:        create channel - <workspace name> <channel name>\n" +
                "switch channel:        switch channel - <channel name>\n" +
                "view mentions:         view mentions\n" +
                "view pinned:           get pinned\n" +
                "view users:            view users\n" +
                "search user:           search user - <name of user> (Search Field not required)\n"+
                "send to group:         send - <message>\n" +
                "direct message:        send to - <user> <message>\n" +
                "favorite message:      favorite message - <message>\n"+
                "unfavorite message:    unfavorite message - <message>\n"+
                "veiw favorites:        view favorites\n"+
                "pin message:           pin message - <messageId>\n" +
                "unpin message:         unpin message - <messageId>\n" +
                "send a text file:      send file - <filepath>\n" +
                "download a file:       download file - <name> (.txt only)\n" +
                "change role:           change role - <RoleName> <Username>\n" +
                "log messages:          log messages\n" +
                "view friends:          view friends\n" +
                "add friend:            add friend - <name>\n" +
                "delete friend:         delete friend - <name>\n");

=======
                "create user:               create user - <name> <password>\n" +
                "login:                     login - <username> <password>\n" +
                "delete user:               delete user\n" +
                "create workspace:          create workspace - <name of workspace>\n" +
                "join workspace:            join - <name of workspace>\n" +
                "switch workspace:          switch workspace - <workspace name>\n" +
                "search workspace:          search workspace - <name of workspace> (Search Field not required)\n" +
                "create channel:            create channel - <workspace name> <channel name>\n" +
                "switch channel:            switch channel - <channel name>\n" +
                "view mentions:             view mentions\n" +
                "view pinned:               get pinned\n" +
                "view users:                view users\n" +
                "search user:               search user - <name of user> (Search Field not required)\n"+
                "send to group:             send - <message>\n" +
                "direct message:            send to - <user> <message>\n" +
                "pin message:               pin message - <messageId>\n" +
                "unpin message:             unpin message - <messageId>\n" +
                "send a text file:          send file - <filepath>\n" +
                "download a file:           download file - <name> (.txt only)\n" +
                "change role:               change role - <RoleName> <Username>\n" +
                "log messages:              log messages\n" +
                "view friends:              view friends\n" +
                "add friend:                add friend - <name>\n" +
                "delete friend:             delete friend - <name>\n" +
                "create appointment:        create appointment (User prompts occur)\n" +
                "view appointments:         view appointments - <Type of appointment to see> (\"pending\"\\\"accepted\"\\empty argument) \n" +
                "respond to an appointment: respond appointment - \n" +
                "delete an appointment:     delete appointment - \n");
>>>>>>> master
    }

    private static void WriteFile(String[] linesToWrite, String filePath, String fileName) {
        //Below is how we'll write to a file
        try {
            //We want to put it in the source directory of the entire project so for Dylan (the author):
            //  "C:\Users\dmrz0\OneDrive\Desktop\Slack\logs\FILENAME"
            // Get that relative directory and if it doesn't exist. Make it
            File dir = new File(filePath);
            if(!dir.exists()){
                dir.mkdir();
            }
            //Get the file for to write to.
            // It shouldn't really exist unless a user logs twice within a minute
            //If it does exist, delete it, and make a new one
            File toWrite = new File(dir + fileName + ".txt");
            FileWriter fw;
            if (toWrite.exists())
                toWrite.delete();
            toWrite.createNewFile();
            //Set it to be writable
            toWrite.setWritable(true);
            //Prepare to start writing the file. Making a file Writer, and then iteration through the data
            //and writing those lines into the file.
            fw = new FileWriter(toWrite);
            for (String line : linesToWrite) {
                fw.write(line + "\n");
            }
            //Close the writer to prevent memory leaks
            fw.close();
            //set the file to read only. Gotta keep our logs pure and clean
            toWrite.setReadOnly();
            System.out.println("File " + fileName + "Written to: \n" +
                    "Absolute Path: " + toWrite.getCanonicalPath() + "\n" +
                    "Relative Path: " + toWrite.getPath() + "\n");
        } catch (IOException e) {
            //Lots of methods have the chance to throw an error (although they shouldn't now)
            //So we want to print that error.
            e.printStackTrace();
        }
    }

    private static String[] LogMessagesFormat(Message[] messages) {
        String[] file = new String[messages.length];
        //We want to show the Workspace and Channel along with sender for each
        //As channel will change (and workspace will not) we want to keep track of the channel and get its name
        // when it changes. So we'll keep track of a messages cId.
        //We also want to have the Sender's name for each message, and that's not grouped,
        // so we'll need to pull that each message :(
        int cId = -1;
        String channelName = "";
        for (int i = 0; i < messages.length; i++) {
            String messageString = "";
            Message message = messages[i];
            if (message.getcID() != cId) {
                cId = message.getcID();
                DBSupport.HTTPResponse cRepsonse = Channel.getChannelName(cId);
                if (cRepsonse.code >= 300) {
                    // as we want all messages that are public, should an issue from the backend happen,
                    // we want to still display the message. What we'll do is make just use a tab for that
                    channelName = "\t";
                } else {
                    channelName = cRepsonse.response;
                }
            }
            String senderName;
            DBSupport.HTTPResponse uRepsonse = User.getUserNameByID(message.getSenderId());
            if (uRepsonse.code >= 300) {
                // as we want all messages that are public, should an issue from the backend happen,
                // we want to still display the message. What we'll do is make just use a tab for that
                senderName = "\t";
            } else {
                senderName = uRepsonse.response;
            }
            messageString = "[" + curWorkspace.getName() + "].[" + channelName + "]\t" + "FROM: " + senderName +
                    "\n\tMESSAGE: " + message.getContent() + "\n";
            file[i] = messageString;
        }
        return file;
    }

    private static String ReplaceSpecChars(String input) {
        //For data fields that may contain "bad" data for our urls (spaces, tabs, stuff like that, we want to transform
        // it to something that url's can handle
        String content = "";
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if(isAlphaNum(c))
                content += c;
            else {
                int charcode = (int) c;
                String hex = (Integer.toHexString(charcode));
                content += "%" + (hex.length() < 2 ? "0" : "") + hex;
            }
        }
        try{
            return ReplaceBadWords(content);
        }catch(Exception e){
            return content;
        }
    }
    private static String ReplaceBadWords(String input) throws FileNotFoundException {
        String content = input;
        Scanner s = new Scanner(new File("wordFilter"));
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> replacement = new ArrayList<String>();
        String temp;
        String[] temp2 = new String[2];
        while(s.hasNext()){
            temp = s.nextLine();
            temp2 = temp.split("|");
            words.add(temp2[0]);
            replacement.add(temp2[1]);
        }
        for(int i = 0; i < words.size(); i++)
            content = content.replace(words.get(i), replacement.get(i));


        return content;
    }

    private static boolean isAlphaNum(char c){
        return ( c >= '0' && c <= '9') ||( c >= 'A' && c <= 'Z') ||( c >= 'a' && c <= 'z');
    }
}
