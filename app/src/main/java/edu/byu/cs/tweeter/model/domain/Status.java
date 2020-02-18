package edu.byu.cs.tweeter.model.domain;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class Status {

    private final User user;
    private final String message;
    private final List<String> userMentions;
    private final List<String> links;
    private final Timestamp timeStamp;

    public Status(User user, String message) {
        this.user = user;
        this.message = message;
        this.userMentions = new ArrayList<>();
        this.links = new ArrayList<>();
        findUserMentions();
        findLinks();
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
        return userMentions;
    }

    public List<String> getLinks()
    {
        return links;
    }

    private void findUserMentions(){
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
                userMentions.add(messageCopy.substring(startIndex, endIndex));
                messageCopy = messageCopy.substring(startIndex + 1);
                temp = messageCopy.toCharArray();

            }
        }
    }

    private void findLinks(){
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
                links.add(messageCopy.substring(startIndex, endIndex));
                messageCopy = messageCopy.substring(startIndex + 1);
            }
        }
    }
}
