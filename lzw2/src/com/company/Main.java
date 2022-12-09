package com.company;

import java.util.*;

public class Main
{

    public static String generateSequence(double probabilities[])
    {
        int probA = (int) (probabilities[0] * 100);
        int probB = (int) (probabilities[1] * 100);
        int probC = (int) (probabilities[2] * 100);
        int randN;
        String sequence = "";
        Random rand = new Random();

        for (int i = 1; i <= 1000; i++)
        {
            randN = 1 + rand.nextInt(100);
            if (randN <= probA)
            {
                sequence += "a";
            } else if (randN <= probA + probB)
            {
                sequence += "b";
            } else if (randN <= probA + probB + probC)
            {
                sequence += "c";
            }
            else
                sequence += "d";
        }
        return sequence;

    }

    public static List<Integer> compress(String uncompressed)
    {
        int dictSize = 256;
        //to create a map we have to loop through 0 to 256, for each integer insert inside the map its coresponding string value
        //along with its code value

        //while encoding we know the characters and want to retrieve the number of presentations  that is why we choose to set the character as the key and the code
        //as the value in the map
        Map<String, Integer> dictionary = new HashMap<>();

        // now for each character of our input text
        for (int i = 0; i < dictSize; i++)
            dictionary.put("" + (char) i, i);


        //now we need to do is check if this character or rather the _continations__ of this character and the ones following it are present in the map,
        //to include multiple characters in the same check what we did was create a string variable and initialized an empty string
        String word = "";
        List<Integer> result = new ArrayList<>();

        for (char ch : uncompressed.toCharArray())
        {
            // then the check was done on the _contactinations_ of both, this empty string and the character.
            String wordChar = word + ch;
            //the purpose of this is that if the character is found ..
            if (dictionary.containsKey(wordChar))
                // ..the empty string will be updated to store the actual character so that in the next iteration the wordChar variable will
                //represent both the previous and current characters.
                word = wordChar;
                //as soon as we stop finding an entry inside the dictionary for our character we need to do three things
            else
            {
                //first we add to the output result the code representing the previous character we had
                result.add(dictionary.get(word));
                //then we add to the dictionary a new entry for the characters that weren't found and finally
                dictionary.put(wordChar, dictSize++);
                //we reset the empty string we started with to point only to the current character we are looping over
                word = "" + ch;
            }
            //after we exit the for loop, pr if, there is one last check we need to do. we looped over all characters and appended
            //code for each one, but what if the last code was a known code? if that is the case then the loop exited withouth having the
            //chance to add the last code to the result and end it with the if statement and not the else statement..
        }
        //therefore we need to check if the string we are using to loop over the characters is empty or not. because if it isnt then thats what it happend
        if (!word.equals(""))
            //and therefore we need to append one last integer to the resulting output
            result.add(dictionary.get(word));
        return result;
    }

    public static String decompress(List<Integer> compressed) {
        //we do the same as before however because we have and know the numbers here we need to  retrieve the characters
        int dictSize = 256;
        Map<Integer, String> dictionary = new HashMap<Integer, String>();
        //so we set the integer as keys and the characters as values and that is exactly what we did in the for loop
        for (int i = 0; i < dictSize; i++) {
            dictionary.put(i,  String.valueOf((char) i));
        }
        //the first integer of the encoded list always represents the character found in the initial dictionary so out resulting string will
        //start with that character which we will retrieve and remove from the list of integers passed to us as input
        String word = ""+ (char) (int) compressed.remove(0);
        String result = word;
        //now for the remaining integers we are going zo loop over them
        for(int k:compressed) {
            String entry= "";

            //and for each one we need to check if the  key set of the map contains this particular code
            if(dictionary.containsKey(k)) {
                //if it does  we retrieve it
                entry = dictionary.get(k);
            }
            //and if not
            else if(k == dictSize) {
                //we take the first letter from the characters we just had in the previous iteration and appended to the
                //same set of characters
                entry = word + word.charAt(0);
            }
            //this retrieved entry is appended to the resulting string.
            result += entry;
            //then the first character of this entry is appended to the characters of the previous iteration and added to the dictionary
            //to generate the same entries as the encode operation
            dictionary.put(dictSize++,word + entry.charAt(0));
            //finally the characters of the previous iteration are updated to reference the current one to be able to proced with our loop
            //with the remaining integers
            word=entry;
        }
        return result;
    }
    public static void main(String[] args)
    {

        String seq1 = generateSequence(new double[] { 0.5, 0.2, 0.15, 0.15 });
        String seq2 = generateSequence(new double[] { 0.3, 0.3, 0.25, 0.15 });

        List<Integer> compressed1 = compress(seq1);
        String com = "";
        for (int num: compressed1){
            com = com + Integer.toBinaryString(num);
        }
        List<Integer> compressed2 = compress(seq2);
        String com1 = "";
        for (int num: compressed2){
            com1 = com1 + Integer.toBinaryString(num);
        }
        System.out.println(seq1);
        System.out.println("Compressed:" +compressed1);
        //System.out.println("Ratio:" + ((double) seq1.length()*8 / (double) com.length()));
        System.out.println("compressed lwngth: "+com.length());
        System.out.println("word length "+seq1.length()*8);
        System.out.println(((double)com.length()/((double)seq1.length()*8))*100);


        System.out.println(seq2);
        System.out.println("Compressed:" +compressed2);
        System.out.println("compressed lwngth: "+com1.length());
        System.out.println("word length "+seq2.length()*8);
        System.out.println(((double)com1.length()/((double)seq2.length()*8))*100);
    }
}

