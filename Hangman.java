/* Christopher Nelson
 * 06 April 2018
 * Assignment 08: Cheater's Hangman
 */
package hangman;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Hangman {

    public static void main(String[] args) {
        String fileName = "dictionary.txt";
        Set <String> words = new HashSet<>();
        StringBuilder correctDisplay;
        int addedWords = 0;
        int endOfGame = 0;
        int cheatWords = 0;
        boolean win = false;
        
        
        System.out.println("Let's play a fun game of Hangman!");
        Scanner keyboard = new Scanner(System.in);
        while (endOfGame == 0) {
            try {
                Scanner scanner = new Scanner(new File(fileName));
                while (scanner.hasNextLine()) {
                    String word = scanner.nextLine();
                    words.add(word);
                    addedWords++;

                }
                scanner.close();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            
            System.out.println(addedWords + " words left");
            int wordLength = 0;
            while (cheatWords == 0) {
                System.out.println("Please enter the size of the word (# of letters) you are going to guess:\n(Enter '0' to end the game)");
                wordLength = keyboard.nextInt();
                if (wordLength == 0) {
                    endOfGame++;
                    cheatWords--;
                }
                Iterator<String> iterator = words.iterator();
                while (iterator.hasNext()) {
                    String s = iterator.next();
                    if (s.length() == wordLength) {
                        cheatWords++;
                    }
                }
                if (cheatWords == 0) {
                    System.out.println("There were no words of length " + wordLength + "\n");
                } else {
                    Iterator<String> iterator1 = words.iterator();
                    while (iterator1.hasNext()) {
                        String s = iterator1.next();
                        if (s.length() != wordLength) {
                            iterator1.remove();
                            addedWords--;
                        }
                    }
                }
            }
            if(endOfGame != 0){
                break;
            }
            int numGuesses = 0;
            while (numGuesses > 15 || numGuesses < 1){
                System.out.println("Now tell me how many wrong guesses you would like! (At most 15!)");
                numGuesses = keyboard.nextInt();
                if (numGuesses > 15 || numGuesses < 1){
                    System.out.println("Sorry, this is not a valid number of guesses.");
                }
            }
            
            System.out.println(words.size() + " words left");
            Set<Character> guesses = new HashSet<>();
            char guess = ' ';
            correctDisplay = new StringBuilder();
            for(int i = 0; i < wordLength; i++){
                correctDisplay.append("_");
            }
            
            while (!win && numGuesses > 0){
                String compare = correctDisplay.toString();
                HashMap <String, HashSet<String>> wordFamilies = new HashMap<>();
                
                System.out.print("Here is what you have so far: ");
                System.out.println(correctDisplay.toString());
                System.out.print("These are the guesses you have made so far: ");
                for (char c : guesses){
                    System.out.print(c + " ");
                }
                System.out.println("");
                if (numGuesses == 1){
                    System.out.println("You ONLY have " + numGuesses + " wrong guess left!");
                    
                } else {
                    System.out.println("You have " + numGuesses + " wrong guesses left!");
                    
                }
                System.out.println("Please guess a letter!");
                guess = keyboard.next().charAt(0);
                String currentKey = correctDisplay.toString();
                
                if (guesses.add(guess) == false){
                    System.out.println("Sorry, you already guessed this letter!");
                    continue;
                    
                } else {
                    
                    guesses.add(guess);
                    for (String word : words){
                        StringBuilder sb = new StringBuilder();
                        sb.append(correctDisplay.toString());
                        String key = sb.toString();
                        int index = 0;
                        String temp = word;
                        while (index != -1){
                            index = temp.indexOf(guess);
                            if (index == -1){
                                break;
                            }
                            sb.setCharAt(index, guess);
                            temp = temp.substring(0, index)+ "_" + temp.substring(index+1);
                            key = sb.toString();
                            
                        }
                        if (wordFamilies.get(key) == null){
                            HashSet <String> newSet = new HashSet<>();
                            newSet.add(word);
                            wordFamilies.put(key, newSet);
                            
                        } else {
                            wordFamilies.get(key).add(word);
                            
                        }
                       
                        
                    }
                    HashSet<String> largestWordFam = new HashSet<>();
                    for (String key : wordFamilies.keySet()) {
                        HashSet<String> set = wordFamilies.get(key);
                        if (set.size() > largestWordFam.size()) {
                            largestWordFam = set;
                            correctDisplay.replace(0, correctDisplay.length(), key);
                            currentKey = key;
                        }
                        words = largestWordFam;
                        
                    }
                    if (words.size() == 1){
                        System.out.println(words.size() + " word left");
                    } else {
                        System.out.println(words.size() + " words left");
                    }
                    
                    words = largestWordFam;
                    //System.out.println(currentKey + " " + compare);
                    words = largestWordFam;
              
                }
                if (currentKey.equals(compare)) {
                    System.out.println("Incorrect guess, the letter was not found");
                    numGuesses--;

                } else {

                    System.out.println("The letter " + guess + " was found in the word!");
                }
                if (words.size() == 1 && !correctDisplay.toString().contains("_")) {
                    win = true;
                }
            }
            if (win) {
                System.out.println("You won! The word was: " + correctDisplay.toString());
                words.clear();
                addedWords = 0;
                cheatWords = 0;
                System.out.println(words.size());
                
            } else {
                Iterator iter = words.iterator();
                System.out.println("Sorry, you are out of guesses... The word was: " + iter.next());
                words.clear();
                addedWords = 0;
                cheatWords = 0;
                System.out.println(addedWords);
                for (String s : words){
                    System.out.println(s);
                }
            }
        }
        System.out.println("Thank you for playing... sucker!");

    }
    
}