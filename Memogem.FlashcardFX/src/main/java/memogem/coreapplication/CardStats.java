
package memogem.coreapplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/** 
 *This is the class in which all the statistics about the card are stored in.
 *The objects made out of this class are to be included in the Card-class-objects
 *as "private CardStats stats."
 */
public class CardStats {
    private List<Integer> studySpeeds; // studytimes (in millis)
    private List<Integer> studyDifficulty; // user-evaluated difficulties for reviews
    private List<LocalDateTime> studyDates; // dates of study
    private int charcount; // count of characters in the card, used in calculation
    private Timestamp studyTimestamp; //timestamp-object for measuring study times
    
    
    public CardStats(int charlength) {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), charlength);
    }
    
    public CardStats(List<Integer> studySpeeds, List<LocalDateTime> studyDates, List<Integer> studyDifficulty, int charcount) {
        this.studySpeeds = studySpeeds;
        this.studyDifficulty = studyDifficulty;
        this.studyDates = studyDates;
        this.charcount = charcount;
        studyTimestamp = new Timestamp();
    }

    /**
     * if the front or back of the card are changed, count new amount of characters
     * @param length length of the card's text-fields as a sum 
     */
    public void changeCharCount(int length) { // length = front.length + back.length
        charcount = length;
    }
    /**
     * Stores the difficulty of the latest viewing of the card.
     * @param integer difficulty (-2 being difficult, -1 somewhat difficult,
     * 0 just right, 1 quite easy, 2 easy).
     */
    public void addNewDifficulty(int difficulty) {
        studyDifficulty.add(difficulty);
    }
    
    /**
     * Starts taking time.
     */
    public void startStudying() {
        studyTimestamp.start();
    }
    
    /**
     * Stops taking time and saves it to the collection. 
     */    
    public void stopStudying() {
        studyTimestamp.stop();
        int speed = studyTimestamp.calculateSpeed();
        studySpeeds.add(speed);
        studyDates.add(LocalDateTime.now());
        
    }
    /**
     * Returns the latest time lapsed during latest viewing of the card.
     * @return integer
     */
    public int latestSpeed() {
        if (!studySpeeds.isEmpty()) {
            return studySpeeds.get(studySpeeds.size()-1);
        }
        return 0;
    }
    
    /**
     * Calculates average speed used for studying a card.
     * @return returns positive integer if there are more than 0 study-times,
     * otherwise returns 0.
     */
    public int calculateAVGSpeed() {
        if (studySpeeds.size() > 0) {
            return calculateTimeStudied() / studySpeeds.size();
        } else {
            return 0;
        }
    }
    
     /**
     * Calculates average speed used for studying a card.
     * @return returns positive integer if there are more than 0 study-times,
     * otherwise returns 0.
     */
    public double calculateAVGDifficulty() {
        double varAVG = (1.0 * calculateSum(studyDifficulty.size(), studyDifficulty)) / studyDifficulty.size();
        return calculateAVG(studyDifficulty);
    }

    public double calculateAVG(List<Integer> list) {
        if (list.size() > 0) {
            return (calculateSum(list.size(), list) * 1.0) / studySpeeds.size();
        } else {
            return 0;
        }
    }
    
    /**
     * Calculates average speed for the last three times that were 
     * used to study particular card.
     * @return returns positive integer if there are more than 0 study-times,
     * otherwise returns 0.
     */
    public int calculateAVGSpeedLastThree() {
        if (studySpeeds.size() > 0) {
            return calculateSum(3, studySpeeds) / 3;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculates standard deviation of the studySpeeds.
     * @return 
     */
    public int calculateStandardDeviation() {
        return (int) Math.sqrt(calculateVariance());
    }

    /**
     * Calculates Variance of the studySpeeds.
     * @return integer
     */
    public int calculateVariance() {
        int speedAvg = calculateAVGSpeed();
        int[] diff = new int[studySpeeds.size()];
        
        for (int i = 0; i < studySpeeds.size(); i++) {
            int speedMinusMean = (speedAvg - studySpeeds.get(i));
            speedMinusMean *= speedMinusMean;
            diff[i] = speedMinusMean;
        }
        
        int sum = 0;
        for (int i : diff) {
            sum += i;
        }
        return sum / studySpeeds.size();
    }
    
    /**
     * Calculates total amount of time used to studying a particular card.
     * @return returns positive integer 
     */
    public int calculateTimeStudied() {
        return calculateSum(studySpeeds.size(), studySpeeds);
    }
    
    /**
     * For testing-purposes. Speeds up testing by making possible
     * the creation of fake practices.
     * 
     */
    public void addNewPractise(int speed) {
        //Add new difficulty
        addNewDifficulty(new Random().nextInt(4) - 2);
        
        //Create dummy timestamp
        int days1 = new Random().nextInt(28) + 1;
        int hours = (new Random().nextInt(13) + 10);
        int minutes = (new Random().nextInt(50));
        int seconds = (new Random().nextInt(50));
        
        LocalDateTime studyDate = LocalDateTime.of(2016, 2, days1, hours, minutes, seconds);  
        studyDates.add(studyDate);
        if (speed > 0) {
            studySpeeds.add(speed);
        }
    }
    /**
     * Calculates sum of given list or just part of it
     * @param times sum of how many values (counting from the
     * list's latest values.
     * @param list List of Integers that will be summed up into
     * a final value.
     * @return Summed up value as integer
     */
    private int calculateSum(int times, List<Integer> list) {
        int sum = 0;
        if (times > list.size()) {
            times = list.size();
        }
        for (int i = (times - 1); i >= 0; i--) {
            sum += list.get(i);
        }
        return sum;
    }
    
    /**
     * Adds new timestamp (LocalDateTime.now())
     */
    public void addNewTimeStamp() {
        studyDates.add(LocalDateTime.now());
    }
    
    //set- and getmethods
    public List<Integer> getHowfast() {
        return studySpeeds;
    }

    public int getCharcount() {
        return charcount;
    }

    public int getPracticeTimes() {
        return studySpeeds.size();
    }

    public List<LocalDateTime> getStudyDates() {
        return studyDates;
    }
    
    public boolean isEmpty() {
        return studySpeeds.isEmpty();
    }
    
    public void setCharcount(int charcount) {
        this.charcount = charcount;
    }
    /**
     * Finds the last time the card was studied.
     * @return 
     */
    public LocalDateTime getLastStudyDate() {
        if (!studyDates.isEmpty()) {
            int index = studyDates.size() - 1;
            return studyDates.get(index);
        }
        return null;
    }
}
