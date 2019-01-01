package com.example.anton.syshelper;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Pattern;

public class InfoClient {
    private final Pattern p = Pattern.compile("^\\/([^\\s]*)\\s?(?:\\[([^\\]]*)\\])?\\s*(.*)$");
    private Socket socket;
    private  boolean watch;
    private final String TAG="CLIENT";
    private String username="";
    private ServerInfo serverInfo;
    private PrintWriter printWriter;
    private ServerInfoActivity mainActivity;
    private static  InfoClient instance=new InfoClient();


    public InfoClient() {
        return;
    }

    public static InfoClient getInstance(){
        return instance;
    }


    public void setClient(Socket socket, ServerInfo serverInfo,ServerInfoActivity mainActivity){
        this.socket=socket;

        this.mainActivity=mainActivity;
        this.serverInfo=serverInfo;
        try {
            printWriter=new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance=this;
    }

    private void getReady(){
        sendCommand("serverUserName", "USER");
        sendCommand("serverServerName", serverInfo.getName());
        sendCommand("serverHost", serverInfo.getHost());
        sendCommand("serverPort", serverInfo.getPort());
        sendCommand("serverPassword", serverInfo.getPassword());
    }


    public void watchForConnectionInput() throws IOException {
        watch=true;
        BufferedReader reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (watch){
            String message = reader.readLine();
            Log.d(TAG, "watchForConnectionInput: "+message);
            if (!message.equals("")){
                final Command command=parseCommand(message);
                switch (command.Command){
                    case "ready":
                        getReady();
                        break;
                    case "postDiskInfo":
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.setDiskInfoText(new DiskInfo(command.Body));
                            }
                        });
                        break;

                    case "postExe":
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.setConsoleLog(command.Body);
                            }
                        });
                        break;
                    case "postFilesExe":
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.setFiles(command.Body);
                            }
                        });
                        break;
                    case "remove":
                        watch=false;
                        socket.close();
                        break;
                }
            }
        }
    }

    public void watchForConsoleInput(String message){
        Log.d(TAG, "watchForConsoleInput: "+message);
        //
        if (!message.isEmpty()){
            Log.d(TAG, "watchForConsoleInput: "+username);
            Command command=parseInput(message);
            if (command.Command=="") {
                sendCommand("message", message);
            }else {
                switch (command.Command){
                    case "getDiskInfo":
                        sendCommand("getDiskInfo",command.Body);
                        break;
                    case "exe":
                        sendCommand("exe",command.Body);
                        break;
                    case "filesExe" :
                        sendCommand("filesExe",command.Body);
                        break;
                }
            }
        }
    }

    private class Command{
        private String Command;
        private String Body;

        Command(String command,String body,String userName){
            this.Command=command;
            this.Body=body;
        }

    }

    private Command parseCommand(String message){
        String res[] =message.split(" ");
        if (message.charAt(1)=='p'){
            switch (message.charAt(5)){
                case 'D':
                    return new Command("postDiskInfo",message.substring(message.indexOf("]")+2),"");
                case 'E':
                    return new Command("postExe",message.substring(message.indexOf("]")+2),"");
                case 'F':
                    return new Command("postFilesExe",message.substring(message.indexOf("]")+2),"");
            }
        }
        if (res.length==2){
            return new Command("connect","","{Anton}");
        }
         if(res[0].charAt(1)=='r'){
                return new Command("ready","","");
        }
        return null;
    }

    private Command parseInput(String message) {
        String res[] = message.split(" ");
        String args;
        switch (res[0].charAt(1)){
            case 'e':
                args = message.substring(message.indexOf(" ") + 1);
                return new Command("exe", args, username);
            case 'f':
                args = message.substring(message.indexOf(" ") + 1);
                return new Command("filesExe", args, username);
            case 'g':
                return new Command("getDiskInfo", "", username);
            default:
                return null;
        }
    }

    private void sendCommand(String command,String body){
        String message = String.format("/%s %s\n",command,body);
        Log.d(TAG, "SendCommand: "+message);
        while (printWriter==null){
            ;
        }
        printWriter.write(message);
        printWriter.flush();
    }

    public void closeConnection(){
        watch=false;
        sendCommand("close","");
    }
}
