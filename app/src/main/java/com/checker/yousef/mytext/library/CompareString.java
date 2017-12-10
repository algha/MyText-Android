package com.checker.yousef.mytext.library;

/**
 * Created by yousef on 2/12/2017.
 */

public class CompareString {

    private String sourceText;
    private String checkText;

    private String outputText = "";

    private Integer count = 0;

    public CompareString(String source, String check){
        sourceText = source;
        checkText = check;

        outputText = checkText();
    }

    private String checkText(){
        String tempText = sourceText;
        String compoment[] = checkText.split(",");

        for (String str: compoment){
            if (sourceText.toLowerCase().contains(str.toLowerCase())){
                Integer length = tempText.split(str, -1).length-1;
                count = count+length;
                tempText = tempText.replace(str,"<font color='red'>"+str+"</font>");
            }
        }
        return tempText;
    }


    public String getOutput(){
        return outputText;
    }

    public Integer getCount(){
        return count;
    }
}
