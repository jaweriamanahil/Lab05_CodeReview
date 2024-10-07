package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FilterTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "ayesha", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "amna", "Let's discuss Rivest.", d3);
    private static final Tweet tweet4 = new Tweet(4, "ayesha", "Some tweet without words.", d1);

//    @Test
//    public void testAssertionsEnabled() {
//        boolean assertionsEnabled = false;
//
//        // Check if assertions are enabled for the current class loader
//        try {
//            // This will throw an AssertionError if assertions are enabled
//            assert false;
//        } catch (AssertionError e) {
//            assertionsEnabled = true;
//        }
//
//        // Assert that assertions are indeed enabled
//        assertTrue("Assertions should be enabled. Run with -ea VM argument.", assertionsEnabled);
//    }

    // writtenBy() tests
    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(), "ayesha");
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testWrittenBySingleTweet() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1), "ayesha");
        assertEquals("expected 1 tweet", 1, result.size());
        assertTrue("expected to contain tweet1", result.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsSingleAuthor() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet4), "ayesha");
        assertEquals("expected 2 tweets", 2, result.size());
        assertTrue("expected to contain tweet1 and tweet4", result.containsAll(Arrays.asList(tweet1, tweet4)));
    }

    @Test
    public void testWrittenByNoMatchingAuthor() {
        List<Tweet> result = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "frank");
        assertTrue("expected empty list", result.isEmpty());
    }

    // inTimespan() tests
    @Test
    public void testInTimespanNoTweets() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T12:00:00Z");
        Timespan timespan = new Timespan(start, end);
        List<Tweet> result = Filter.inTimespan(Arrays.asList(), timespan);
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testInTimespanSomeTweetsWithinTimespan() {
        Instant start = Instant.parse("2016-02-17T09:00:00Z");
        Instant end = Instant.parse("2016-02-17T11:30:00Z");
        Timespan timespan = new Timespan(start, end);
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), timespan);
        assertEquals("expected 2 tweets", 2, result.size());
        assertTrue("expected to contain tweet1 and tweet2", result.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testInTimespanNoTweetsWithinTimespan() {
        Instant start = Instant.parse("2016-02-17T13:00:00Z");
        Instant end = Instant.parse("2016-02-17T14:00:00Z");
        Timespan timespan = new Timespan(start, end);
        List<Tweet> result = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), timespan);
        assertTrue("expected empty list", result.isEmpty());
    }

    // containing() tests
    @Test
    public void testContainingNoWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());
        assertTrue("expected empty list", result.isEmpty());
    }

    @Test
    public void testContainingOneMatchingWord() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertEquals("expected 2 tweets", 2, result.size());
        assertTrue("expected to contain tweet1 and tweet2", result.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testContainingMultipleMatchingWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet3), Arrays.asList("Rivest", "discuss"));
        assertEquals("expected 2 tweets", 2, result.size());
        assertTrue("expected to contain tweet1 and tweet3", result.containsAll(Arrays.asList(tweet1, tweet3)));
    }

    @Test
    public void testContainingNoMatchingWords() {
        List<Tweet> result = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("computer"));
        assertTrue("expected empty list", result.isEmpty());
    }
}