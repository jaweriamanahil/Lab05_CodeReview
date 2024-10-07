package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "navaal", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "ayesha", "@navaal let's meet later", d3);
    private static final Tweet tweet4 = new Tweet(4, "sara", "@ayesha and @navaal talk tomorrow?", d3);
    private static final Tweet tweet5 = new Tweet(5, "eve", "No mentions here.", d3);


    // getTimespan() tests
    @Test
    public void calculateTimespanWithOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }

    @Test
    public void calculateTimespanForTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void calculateTimespanForSeveralTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    // getMentionedUsers() tests
    @Test
    public void extractMentionedUsersFromNoMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet2));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void extractMentionedUsersFromSingleMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        assertEquals("expected one mention", new HashSet<>(Arrays.asList("navaal")), mentionedUsers);
    }

    @Test
    public void extractMentionedUsersFromMultipleTweets() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        assertEquals("expected two mentions", new HashSet<>(Arrays.asList("navaal", "ayesha")), mentionedUsers);
    }

    @Test
    public void extractMentionedUsersIgnoreCase() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        assertEquals("expected case-insensitive mentions", new HashSet<>(Arrays.asList("navaal", "ayesha")), mentionedUsers);
    }

    @Test
    public void ignoreInvalidMentionsInText() {
        Tweet invalidMentionTweet = new Tweet(6, "frank", "Email address test@example.com doesn't count as a mention", d3);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(invalidMentionTweet));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
}