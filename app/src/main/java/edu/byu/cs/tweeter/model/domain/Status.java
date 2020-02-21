package edu.byu.cs.tweeter.model.domain;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class Status {

    private final User user;
    private final String message;
    private final Timestamp timeStamp;
    private final List<String> userShoutout;
    private final List<String> weblinks;

    public Status(User user, String message) {
        this.userShoutout = new ArrayList<>();
        this.weblinks = new ArrayList<>();
        this.user = user;
        this.message = message;
        findUser();
        findURL();
        this.timeStamp = new Timestamp(System.currentTimeMillis());
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public List<String> getUserMentions()
    {
        return userShoutout;
    }

    public List<String> getLinks()
    {
        return weblinks;
    }

    private void findUser(){
        int numMentions = message.length() - message.replace("@", "").length();
        if (!(numMentions <= 0)){
            char[] temp = message.toCharArray();
            String messageCopy = message;

            for(int i = 0; i < numMentions; i++){
                int j = 0;
                while(temp[j] != '@'){
                    j++;
                }
                int startIndex = j;
                j++;

                while(Character.isLetterOrDigit(temp[j])){
                    if (j == temp.length - 1){
                        j++;
                        break;
                    }
                    j++;
                }
                int endIndex = j;
                userShoutout.add(messageCopy.substring(startIndex, endIndex));
                messageCopy = messageCopy.substring(startIndex + 1);
                temp = messageCopy.toCharArray();

            }
        }
    }

    private void findURL(){
        int numLinks = (message.length() - message.replace("www", "").length())/3;
        if (!(numLinks <= 0)){
            String messageCopy = message;

            for(int i = 0; i < numLinks; i++){
                int j = 0;
                int startIndex = messageCopy.indexOf("www");
                messageCopy.substring(startIndex);
                j = startIndex;
                char[] temp = messageCopy.toCharArray();


                while(temp[j] != ' '){
                    if (j == temp.length - 1){
                        j++;
                        break;
                    }
                    j++;
                }
                int endIndex = j;
                weblinks.add(messageCopy.substring(startIndex, endIndex));
                messageCopy = messageCopy.substring(startIndex + 1);
            }
        }
    }
}

//    private void findUser(){
//        int mentions = message.length() - message.replace("@", "").length();
//        if(!(mentions <= 0)){
//            char[] temp = message.toCharArray();
//            String copy = message;
//            for(int i = 0; i < mentions; i++){
//                int j = 0;
//                while(temp[j] != '@'){
//                    j++;
//                }
//                int startIndex = j;
//                j++;
//
//                while(Character.isLetterOrDigit(temp[j])){
//                    if (j == temp.length - 1){
//                        j++;
//                        break;
//                    }
//                    j++;
//                }
//                int endIndex = j;
//                userShoutout.add(copy.substring(startIndex, endIndex));
//                copy = copy.substring(startIndex + 1);
//                temp = copy.toCharArray();
//            }
//        }
//        if(message.contains("@")){
//            int startIndex = message.indexOf("@");
//            String substring = message.substring(startIndex);
//            int endIndex;
//            if(substring.contains(" ")){
//                endIndex = substring.indexOf(" ");
//            }
//            else{
//                endIndex = substring.length();
//            }
//            userShoutout.add(message.substring(startIndex,endIndex));
//        }
//    }
//
//    private void findURL(){
//        int numLinks = (message.length() - message.replace("www", "").length())/3;
//        if (!(numLinks <= 0)){
//            String copy = message;
//
//            for(int i = 0; i < numLinks; i++){
//                int j = 0;
//                int startIndex = copy.indexOf("www");
//                copy.substring(startIndex);
//                j = startIndex;
//                char[] temp = copy.toCharArray();
//
//
//                while(temp[j] != ' '){
//                    if (j == temp.length - 1){
//                        j++;
//                        break;
//                    }
//                    j++;
//                }
//                int endIndex = j;
//                weblinks.add(copy.substring(startIndex, endIndex));
//                copy = copy.substring(startIndex + 1);
//            }
//        }
//
////        if(message.contains("@")){
////            int startIndex = message.indexOf("@");
////            String substring = message.substring(startIndex);
////            int endIndex;
////            if(substring.contains(" ")){
////                endIndex = substring.indexOf(" ");
////            }
////            else{
////                endIndex = substring.length();
////            }
////            weblinks.add(message.substring(startIndex,endIndex));
////        }
//    }
//}
