//package twitter;
//
//import java.util.List;
//import java.util.stream.Collectors;

//public class Filter {
//
//    
//    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
//        // Bug: Incorrect case-sensitive comparison instead of case-insensitive
//        return tweets.stream()
//                     .filter(tweet -> tweet.getAuthor().equals(username)) // Should be equalsIgnoreCase()
//                     .collect(Collectors.toList());
//    }
//
//    
//    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
//        // Bug: Incorrect comparison logic that excludes tweets exactly at the timespan boundaries
//        return tweets.stream()
//                     .filter(tweet -> tweet.getTimestamp().isAfter(timespan.getStart()) && // Incorrectly excludes start time
//                                      tweet.getTimestamp().isBefore(timespan.getEnd())) // Incorrectly excludes end time
//                     .collect(Collectors.toList());
//    }
//
//   
//    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        // Bug: Incorrect logic that checks if all words must be in the tweet instead of any one word
//        return tweets.stream()
//                     .filter(tweet -> words.stream()
//                                           .allMatch(word -> tweet.getText().toLowerCase().contains(word.toLowerCase()))) // Should be anyMatch()
//                     .collect(Collectors.toList());
//    }
//}




//public class Filter {
//
//  
//    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
//        return tweets.stream()
//                     .filter(tweet -> tweet.getAuthor().equalsIgnoreCase(username))
//                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
//    }
//
//    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
//        return tweets.stream()
//                     .filter(tweet -> !tweet.getTimestamp().isBefore(timespan.getStart()) 
//                                   && !tweet.getTimestamp().isAfter(timespan.getEnd()))
//                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
//    }
//
//    
//    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        List<String> lowerCaseWords = words.stream()
//                                           .map(String::toLowerCase)
//                                           .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
//        return tweets.stream()
//                     .filter(tweet -> {
//                         String tweetText = tweet.getText().toLowerCase();
//                         return lowerCaseWords.stream().anyMatch(tweetText::contains);
//                     })
//                     .collect(Collectors.toList());  // Use Collectors.toList() for Java 8+ compatibility
//    }
//
//}



//package twitter;
//
//import java.util.List;
//import java.util.ArrayList;
//
//public class Filter {
//
//    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
//        List<Tweet> result = new ArrayList<>();
//        for (Tweet tweet : tweets) {
//            if (tweet.getAuthor().equalsIgnoreCase(username)) {
//                result.add(tweet);
//            }
//        }
//        return result;
//    }
//
//    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
//        List<Tweet> result = new ArrayList<>();
//        for (Tweet tweet : tweets) {
//            if (!tweet.getTimestamp().isBefore(timespan.getStart()) &&
//                !tweet.getTimestamp().isAfter(timespan.getEnd())) {
//                result.add(tweet);
//            }
//        }
//        return result;
//    }
//
//    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
//        List<Tweet> result = new ArrayList<>();
//        List<String> lowerCaseWords = new ArrayList<>();
//        for (String word : words) {
//            lowerCaseWords.add(word.toLowerCase());
//        }
//
//        for (Tweet tweet : tweets) {
//            String tweetText = tweet.getText().toLowerCase();
//            for (String word : lowerCaseWords) {
//                if (tweetText.contains(word)) {
//                    result.add(tweet);
//                    break;
//                }
//            }
//        }
//        return result;
//    }
//}


package twitter;
import java.util.ArrayList;
import java.util.List;
public class Filter {
  
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        return writtenByRecursive(tweets, username, new ArrayList<>(), 0);
    }
    private static List<Tweet> writtenByRecursive(List<Tweet> tweets, String username, List<Tweet> result, int index) {
        if (index >= tweets.size()) {
            return result;
        }
        if (tweets.get(index).getAuthor().equalsIgnoreCase(username)) {
            result.add(tweets.get(index));
        }
        return writtenByRecursive(tweets, username, result, index + 1);
    }
  
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        return inTimespanRecursive(tweets, timespan, new ArrayList<>(), 0);
    }

    private static List<Tweet> inTimespanRecursive(List<Tweet> tweets, Timespan timespan, List<Tweet> result, int index) {
        if (index >= tweets.size()) {
            return result;
        }
        if (!tweets.get(index).getTimestamp().isBefore(timespan.getStart()) 
            && !tweets.get(index).getTimestamp().isAfter(timespan.getEnd())) {
            result.add(tweets.get(index));
        }
        return inTimespanRecursive(tweets, timespan, result, index + 1);
    }

    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<String> lowerCaseWords = new ArrayList<>();
        for (String word : words) {
            lowerCaseWords.add(word.toLowerCase());
        }
        return containingRecursive(tweets, lowerCaseWords, new ArrayList<>(), 0);
    }

    private static List<Tweet> containingRecursive(List<Tweet> tweets, List<String> words, List<Tweet> result, int index) {
        if (index >= tweets.size()) {
            return result;
        }
        String tweetText = tweets.get(index).getText().toLowerCase();
        for (String word : words) {
            if (tweetText.contains(word)) {
                result.add(tweets.get(index));
                break;
            }
        }
        return containingRecursive(tweets, words, result, index + 1);
    }
}
