package com.mygdx.game;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import com.badlogic.gdx.Gdx;
import org.json.*;







public class Card {
    private Integer cardID; //ID integer for each card
    private String name; //name of the card
    private String description; //description of what the card does in battle
    private Integer shopCost; //how much the card costs to buy from a shop
    private Integer goldCost; //how much gold it costs to use the card in battle
    private Integer manaCost; //how much mana it costs to use the card in battle
    private String effect; //actual effect on battle statistics such as health or hit rate


    protected static Integer numberOfCards = 0; //total number of cards available in order to generate a new cardID

    //returns costs of cards
    public List<Integer> getBattleCost(){
        return Arrays.asList(shopCost, goldCost, manaCost);
    }

    public Card(){
        numberOfCards++;
    }

    public Integer getManaCost(){
        return manaCost;
    }

    public String getEffect(){
        return effect;
    }


    //----------------------------These are here while there are no cards, just so that there is something to test with.
    public void setManaCost(Integer manaCost) {
        this.manaCost = manaCost;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
    //----------------------------Remove the stuff above once the card database is in action.

    public void JSONTest(){ //Testing to see how to parse JSON files into variables.
        String content = null;
        try {
            content = new Scanner(new File("C:\\Github\\All-Hands-On-Deck\\core\\assets\\cardDatabase.json")).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //JSONObject test = new JSONObject(filename);
        //JSONObject outputTest = test.getJSONObject("CID_01");

        JSONObject json = new JSONObject(content);
        JSONObject outputTest = json.getJSONObject("CardBase");
        JSONObject outputTest2 = json.getJSONObject("CardBase").getJSONObject("CID_01");
        String testingString = json.getJSONObject("CardBase").getJSONObject("CID_01").getString("name");
        System.out.println(testingString);
    }
}