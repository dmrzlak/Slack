import Controllers.DBSupport;
import Models.Message;
import Models.Channel;
import Models.User;
import Models.Workspace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;


/**
 * This will be the main controller for the application.
 * <p>
 * It will take the initial input for User Input
 * and then pass it along to other classes to handle the actual functionality
 *
 * @Author Dylan Mrzlak
 * Original Framework and Use handle for CREATE_WORKSPACE and JOIN_WORKSPACE
 */
public class InputController {
    private static final String CREATE_WORKSPACE = "create workspace";
    private static final String JOIN_WORKSPACE = "join";
    private static final String CREATE_CHANNEL = "create channel";
    private static final String VIEW_USERS = "view users";
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
    private static Gson gson = new Gson();
    private static User curUser = null;
    private static Workspace curWorkspace = null;
    private static Channel curChannel = null;

    public static void main(String[] args) {
        //If this line get mad, check your dependencies, may have dropped
        Scanner input = new Scanner(System.in);
        String userInput = "";

        printInstructions();
        do {
            userInput = input.nextLine();
            //By forcing commands to be in a format of COMMAND - ARGUMENT
            //We can easily manage the input and decide what is needed
            int substringBegin = userInput.indexOf('-');
            if (substringBegin == -1) substringBegin = userInput.length();
            String command = "";
            String[] userArgs = {};
            if (userInput.length() == substringBegin) {
                command = userInput;
            } else {
                command = userInput.substring(0, substringBegin).trim();
                userArgs = userInput.substring(substringBegin + 1).trim().split(" ");
            }

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
                case GET_PINNED:
                    GetPinned(userArgs);
                    break;
                case UNPIN_MESSAGE:
                    UnpinMessage(userArgs);
                    break;
                case CHANGE_ROLE:
                    ChangeRole(userArgs);
                    break;
                default:
                    System.out.println("Invalid Input please try again :(");
                    break;
            }
        } while (input.hasNextLine());

    }

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
        DBSupport.HTTPResponse response = Channel.viewMentions(curUser.getName(), curWorkspace.getName(), curChannel.getName());
        if (response.code >= 300) {
            System.out.println(response.response);
        } else {
            Message[] mentions = gson.fromJson(response.response, Message[].class);
            System.out.println("These are the your mentions:");
            for (Message mention : mentions) {
                String printMention = "\t" + mention.getContent().replaceAll("_SS_", " ");
                System.out.println(printMention);
            }
        }

    }

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
        // but we do some lifting here as well to properly make the strings we need;
        System.out.println("Getting the messages for: " + curWorkspace.getName());
        DBSupport.HTTPResponse response = Message.getAllMessages(curWorkspace.getName());
        if (response.code >= 300) {
            System.out.println(response.response);
        }
        else {
            System.out.println("Retrieval for: " + curWorkspace.getName() + " successful");
            Message[] messages = gson.fromJson(response.response, Message[].class);
            String workspaceName = curWorkspace.getName();
            //There is a real possibility that this could take a long time (and it's , so I'm going to run it asynchronously maybe later)
            //For the log file, as of now, we'll put it into the out folder under a folder logs
            //and with the name:
            //      "LOG_<WORKSPACENAME>_<DATE>
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
            Date date = new Date();
            String filePath = "\\LOG_" + workspaceName + "_" + dateFormat.format(date);
            System.out.println("Formatting");
            String[] linesToWrite = LogMessagesFormat(messages);
            System.out.println("Writing");
            WriteFile(linesToWrite, filePath);
        }
    }

    private static void SignIn(String[] userArgs) {
        if (userArgs.length != 2) {
            System.out.println("Invalid Number or Arguments");
            return;
        }
        DBSupport.HTTPResponse uResponse = User.signIn(userArgs[0], userArgs[1]);
        if (uResponse.code > 300) {
            System.out.println(uResponse.response);
        } else {
            System.out.println("Saved User");
            User u = gson.fromJson(uResponse.response, User.class);
            curUser = u;
        }
    }

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
            JoinWorkspace(new String[]{w.getName()});
            System.out.println("Creating Default Channel");
            CreateChannel(new String[]{w.getName(), "Welcome"});
        }
    }

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

    private static void ViewUsers(String[] userArgs) {
        if (curWorkspace == null) {
            System.out.println("User not in workspace");
            return;
        }
        DBSupport.HTTPResponse viewUsers = Workspace.getUsersInWorkspace(curWorkspace.getName());
        if (viewUsers.code > 300) {
            System.out.println("There are no users in this workspace");
        } else {
            String[] userList = gson.fromJson(viewUsers.response, String[].class);
            System.out.println("\nUsers in workspace: " + curWorkspace.getName());
            for (int i = 0; i < userList.length; i++) {
                System.out.println("\t" + userList[i]);
            }
            System.out.println("\n");
        }
    }

    private static void GetPinned(String[] userArgs) {//TODO
        if (userArgs.length != 0) {
            System.out.println("Invalid number of arguments\n");
            return;
        }
        if(curChannel == null || curWorkspace == null){
            if (curWorkspace == null) {
                System.out.println("You are not in a workspace\n");
            }
            if (curChannel == null) {
                System.out.println("You are not in a channel\n");
            }
            return;
        }
        else{
            //get messages, then return them
            System.out.println("Getting the pinned messages for: " + curChannel.getName());
            DBSupport.HTTPResponse response = User.getPinnedMessages(curWorkspace.getName() ,curChannel.getName());
            if (response.code >= 300) {
                System.out.println(response.response);
            }
            else {
                System.out.println("Retrieval for: " + curChannel.getName() + " successful");
                Message[] messages = gson.fromJson(response.response, Message[].class);
                int i = 0;
                while(i < messages.length){
                    System.out.println("[" + m.getwId() + "." + m.getcID() + "." + m.getId() + "]"
                            + m.getContent().replaceAll("_SS_", " "));
                    i++;
                }

            }
    }

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
                    + m.getContent().replaceAll("_SS_", " "));
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
            DBSupport.HTTPResponse pinMessage = Workspace.unpinMessage(userArgs[0]);
            if (unpinMessage.code > 300) {
                System.out.println(unpinMessage.response);
            } else {
                System.out.println("Unpinned message");
                Message m = gson.fromJson(unpinMessage.response, Message.class);
                System.out.println("Message Unpinned: \n\t" + "[" + m.getwId() + "." + m.getcID() + "." + m.getId() + "]"
                        + m.getContent().replaceAll("_SS_", " "));
            }
        }

        private static void ChangeRole(String[] userArgs) {
            if (userArgs.length != 2) {
                System.out.println("Invalid Number or Arguments");
                return;
            }//1 mute, 2 user, 3 moderator, 4 admin
            int role;
            String strRole;
            String toComp = userArgs[0].toLowerCase();
            if (toComp.equals("mute")){
                role = 1;
                strRole = "Mute";
            }
            else if (toComp.equals("user")) {
                role = 2;
                strRole = "User";
            }
            else if(toComp.equals("mod")){
                role = 3;
                strRole = "Moderator";
            }
            else if(toComp.equals("admin")){
                role = 4;
                strRole = "Admin";
            }
            else{
                System.out.println("Invalid Role name input");
                return;
            }
            DBSupport.HTTPResponse changeRole = Workspace.changeRole(rId, userArgs[1]);
            if (changeRole.code > 300) {
                System.out.println(changeRole.response);
            } else {
                System.out.println("Changed role of " + userArgs[1] + " to " + strRole);
            }
        }

        private static void SendMessage(String[] userArgs) {
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
        String message = "";
        for (int i = 0; i < userArgs.length; i++) {
            message += userArgs[i] + "_SS_";
        }
        message = message.trim();
        DBSupport.HTTPResponse sendMessage = Message.sendMessage(curUser.getName(), curWorkspace.getName(), curChannel.getName(), message);
        if (sendMessage.code > 300) {
            System.out.println(sendMessage.response);
        } else {
            System.out.println("Joining Workspace");
            Message m = gson.fromJson(sendMessage.response, Message.class);

            System.out.println("Message Sent: \n\t" + m.getContent().replaceAll("_SS_", " "));
        }
    }

    private static void SendDM(String[] userArgs) {
        if (userArgs.length < 2) {
            System.out.println("Invalid number of arguments");
            return;
        }
        String directMessage = "";
        for (int i = 1; i < userArgs.length; i++) {
            directMessage += userArgs[i] + "_SS_";
        }
        directMessage = directMessage.trim();
        DBSupport.HTTPResponse dm = Message.sendDirectMessage(curUser.getName(), userArgs[0], directMessage);
        if (dm.code > 300) {
            System.out.println(dm.response);
        } else {
            System.out.println("Joining Workspace");
            Message m = gson.fromJson(dm.response, Message.class);

            System.out.println("Message Sent: \n\t" + m.getContent().replaceAll("_SS_", " "));
        }
    }

    private static void printInstructions() {
        System.out.println("Welcome to Slack# (patent pending), our cheeky, user un-friendly, clone of Slack\n" +
                "\t\tTo run this god forsaken app, type in a command and its arguments.\n" +
                "\t\tIf you dont know the commands or need a refresher. I suggest you git gud skrub\n\n\n\n" +
                "\t\t(Or enter \"help\", I'm not your mommy lol)");
    }

    private static void printHelp() {
        System.out.println("Commands are sent in the order COMMAND - ARGUMENTS\n" +
                "using ' ' to separate arguments\n\n" +
                "create user: create user - <name> <password>\n" +
                "login: login - <username> <password>\n" +
                "create workspace: create workspace - <name of workspace>\n" +
                "join workspace: join - <name of workspace>\n" +
                "create channel: create channel - <workspace name> <channel name>\n" +
                "view users: view users\n" +
                "send to group: send - <message>\n" +
                "direct message: send to - <user> <message>\n" +
                "pin message: pin message - <message>\n" +
                "log messages: log messages\n" +
                "view mentions: view mentions\n");
    }





    private static void WriteFile(String[] linesToWrite, String filePath) {
        //Below is how we'll write to a file
        try {
            //We want to put it in the source directory of the entire project so for Dylan (the author):
            //  "C:\Users\dmrz0\OneDrive\Desktop\Slack\logs\FILENAME"
            // Get that relative directory and if it doesn't exist. Make it
            File dir = new File("..\\..\\logs\\");
            if(!dir.exists()){
                dir.mkdir();
            }
            //Get the file for to write to.
            // It shouldn't really exist unless a user logs twice within a minute
            //If it does exist, delete it, and make a new one
            File toWrite = new File(dir + filePath + ".txt");
            FileWriter fw;
            if(toWrite.exists())
                toWrite.delete();
            toWrite.createNewFile();
            //Set it to be writable
            toWrite.setWritable(true);
            //Prepare to start writing the file. Making a file Writer, and then iteration through the data
            //and writing those lines into the file.
            fw = new FileWriter(toWrite);
            for(String line: linesToWrite){
                fw.write(line);
            }
            //Close the writer to prevent memory leaks
            fw.close();
            //set the file to read only. Gotta keep our logs pure and clean
            toWrite.setReadOnly();
            System.out.println("File " + filePath + "Written to: \n" +
                    "Absolute Path: " + toWrite.getCanonicalPath() + "\n" +
                    "Relative Path: " + toWrite.getPath() + "\n");
        }
        catch (IOException e) {
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
                    "\n\tMESSAGE: " + message.getContent().replaceAll("_SS_", " ") + "\n";
            file[i] = messageString;
        }
        return file;
    }
}
