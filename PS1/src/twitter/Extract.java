//package twitter;
//
//import java.time.Instant;
//import java.util.List;
//import java.util.Set;
//import java.util.HashSet;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

//public class Extract {
//
//    // Faulty implementation: Assumes there will always be at least one tweet.
//    public static Timespan getTimespan(List<Tweet> tweets) {
//        Instant start = Instant.now(); // Incorrectly set start to the current time
//        Instant end = start; // Incorrectly set end to the current time
//        
//        // Incorrect logic: Not checking if the list is empty
//        for (Tweet tweet : tweets) {
//            Instant timestamp = tweet.getTimestamp();
//            if (timestamp.isBefore(start)) {
//                start = timestamp; // May not be accurate if the list is empty
//            }
//            if (timestamp.isBefore(end) || timestamp.equals(end)) { // Incorrect comparison logic
//                end = timestamp; // This logic can result in incorrect end time
//            }
//        }
//        return new Timespan(start, end); // May return invalid timespan
//    }
//
//    // Faulty implementation: Fails to detect valid mentions and case sensitivity issues.
//    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
//        Set<String> mentionedUsers = new HashSet<>();
//        Pattern mentionPattern = Pattern.compile("@(\\w+)"); // Bug: Missing negative lookbehind and lookahead
//        
//        for (Tweet tweet : tweets) {
//            Matcher matcher = mentionPattern.matcher(tweet.getText());
//            while (matcher.find()) {
//                // Faulty: Incorrectly adds mention without checking for duplicates
//                mentionedUsers.add(matcher.group(1)); // Fails to normalize to lowercase
//            }
//        }
//        return mentionedUsers;
//    }
//}

//public class Extract {
//
//    public static Timespan getTimespan(List<Tweet> tweets) {
//        Instant start = tweets.get(0).getTimestamp();
//        Instant end = start;
//        
//        for (Tweet tweet : tweets) {
//            Instant timestamp = tweet.getTimestamp();
//            if (timestamp.isBefore(start)) {
//                start = timestamp;
//            }
//            if (timestamp.isAfter(end)) {
//                end = timestamp;
//            }
//        }
//        return new Timespan(start, end);
//    }
//
//    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
//        Set<String> mentionedUsers = new HashSet<>();
//        Pattern mentionPattern = Pattern.compile("(?<!\\w)@(\\w+)(?!\\w)");
//        
//        for (Tweet tweet : tweets) {
//            Matcher matcher = mentionPattern.matcher(tweet.getText());
//            while (matcher.find()) {
//                mentionedUsers.add(matcher.group(1).toLowerCase());
//            }
//        }
//        return mentionedUsers;
//    }
//}




//public class Extract {
//
//    /**
//     * Get the timespan from the earliest to the latest tweet in the list.
//     * 
//     * @param tweets
//     *            list of tweets with distinct ids, not modified by this method.
//     * @return a minimum-length timespan that contains the timestamp of every tweet in the list.
//     */
//    public static Timespan getTimespan(List<Tweet> tweets) {
//        if (tweets == null || tweets.isEmpty()) {
//            throw new IllegalArgumentException("Tweet list cannot be null or empty");
//        }
//
//        Instant start = tweets.get(0).getTimestamp();
//        Instant end = tweets.get(0).getTimestamp();
//
//        for (Tweet tweet : tweets) {
//            Instant timestamp = tweet.getTimestamp();
//            if (timestamp.isBefore(start)) {
//                start = timestamp;
//            }
//            if (timestamp.isAfter(end)) {
//                end = timestamp;
//            }
//        }
//
//        return new Timespan(start, end);
//    }
//
//    /**
//     * Get the set of usernames who are mentioned in the list of tweets.
//     * 
//     * @param tweets
//     *            list of tweets with distinct ids, not modified by this method.
//     * @return a set of usernames mentioned in the tweets. The username in a mention is case-insensitive.
//     */
//    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
//        Set<String> mentionedUsers = new HashSet<>();
//        Pattern mentionPattern = Pattern.compile("(?<!\\w)@(\\w+)(?!\\w)");
//
//        for (Tweet tweet : tweets) {
//            String text = tweet.getText();
//            Matcher matcher = mentionPattern.matcher(text);
//
//            while (matcher.find()) {
//                String mentionedUser = matcher.group(1).toLowerCase();  // Ensure case-insensitive matching
//                mentionedUsers.add(mentionedUser);
//            }
//        }
//
//        return mentionedUsers;
//    }
//}


package twitter;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Extract {

    /**
     * Get the timespan from the earliest to the latest tweet in the list.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length timespan that contains the timestamp of every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets == null || tweets.isEmpty()) {
            throw new IllegalArgumentException("Tweet list cannot be null or empty");
        }

        Instant start = tweets.stream()
                              .map(Tweet::getTimestamp)
                              .min(Instant::compareTo)
                              .orElseThrow(() -> new IllegalArgumentException("No timestamps found"));

        Instant end = tweets.stream()
                            .map(Tweet::getTimestamp)
                            .max(Instant::compareTo)
                            .orElseThrow(() -> new IllegalArgumentException("No timestamps found"));

        return new Timespan(start, end);
    }

    /**
     * Get the set of usernames who are mentioned in the list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a set of usernames mentioned in the tweets. The username in a mention is case-insensitive.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Pattern mentionPattern = Pattern.compile("(?<!\\w)@(\\w+)(?!\\w)");

        return tweets.stream()
                     .flatMap(tweet -> {
                         Matcher matcher = mentionPattern.matcher(tweet.getText());
                         Set<String> mentions = new HashSet<>();
                         while (matcher.find()) {
                             mentions.add(matcher.group(1).toLowerCase());  // Ensure case-insensitive matching
                         }
                         return mentions.stream();
                     })
                     .collect(Collectors.toSet());
    }
}

