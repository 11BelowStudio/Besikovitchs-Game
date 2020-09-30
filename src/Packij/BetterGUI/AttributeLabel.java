package Packij.BetterGUI;

import javax.swing.*;

public class AttributeLabel extends JLabel {

    private String attributeName;
    //holds the name of the attribute that this AttributeLabel is keeping track of

    private int value;
    //holds the value of the attribute that this AttributeLabel is keeping track of

    AttributeLabel(){
        //no-argument constructor eecks dee
        attributeName = "";
    }

    AttributeLabel(String attributeName){
        //constructor without a set value to associate to the attribute name on the label
        this.attributeName = attributeName;
        this.setText(this.attributeName);
    }

    AttributeLabel(String attributeName, int value){
        //calls attributeName constructor then showValue with the value parameter
        //package-private btw
        this(attributeName);
        showValue(value);
    }

    void setAttributeName(String attributeName){
        this.attributeName = attributeName;
        this.setText(attributeName);
    }

    void showValue(int value){
        this.value = value;
        this.setText(attributeName + this.value);
    }
    //updates the text on the label to show the new value, and updates the associated value attribute
    //package-private btw

    void increaseValue(int increaseBy){
        this.value = value+increaseBy;
        this.setText(attributeName + this.value);
    }

    public int getValue(){
        return value;
    }
    //returns the 'value' attribute of this object

    public void clearDisplayValue(){
        this.setText(attributeName);
    }
    //clears the value from the displayed text

}
