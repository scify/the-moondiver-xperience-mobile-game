package org.scify.moonwalker.app.ui.actors.calculator;

public class CalculatorController {
    public static final String ADD = "add";
    public static final String SUBTRACT = "subtract";
    public static final String MULTIPLY = "multiply";
    public static final String DIVIDE = "divide";
    public static final String RESULT = "result";
    public static final String DOT = "dot";
    public static final String ZERO = "0";
    public static final String NINE = "9";
    public static final String EIGHT = "8";
    public static final String SEVEN = "7";
    public static final String SIX = "6";
    public static final String FIVE = "5";
    public static final String FOUR = "4";
    public static final String THREE = "3";
    public static final String TWO = "2";
    public static final String ONE = "1";

    protected String operator1;
    protected String operator2;
    protected String operation;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void addOperator(String operator) {
        if(!operator.equals(DOT)) {
            if(operation.equals("") || operator1.endsWith("."))
                operator1 += operator;
            else
                operator2 += operator;
        } else {
            if(!operator2.equals(""))
                operator2 += ".";
            else if(!operator1.equals(""))
                operator1 += ".";
        }
    }

    public String parseResult() {
        if(operator1.equals("") || operator2.equals("") || operation.equals(""))
            return "";
        float number1 = Float.parseFloat(operator1);
        float number2 = Float.parseFloat(operator2);
        float result = 0;
        switch (operation) {
            case "add":
                result = number1 + number2;
                break;
            case "subtract":
                result = number1 - number2;
                break;
            case "multiply":
                result = number1 * number2;
                break;
            case "divide":
                result = number1 / number2;
                break;
        }
        String resultStr = String.valueOf(result);
        // if result has no decimal places,
        // just output the integer part of it
        if(result % 1 == 0)
            resultStr = String.valueOf((int) result);
        return resultStr;
    }

    public String getCurrentΡrepresentation() {
        String value = operator1 + " " + operationToSign() + " " + operator2;
        if(operator1.equals("") && operator2.equals("") && operation.equals(""))
            value = "";
        return value;
    }

    public void resetCalculator() {
        operator1 = "";
        operator2 = "";
        operation = "";
    }

    public String operationToSign() {
        switch (operation) {
            case ADD:
                return "+";
            case SUBTRACT:
                return "-";
            case MULTIPLY:
                return "*";
            case DIVIDE:
                return "/";
        }
        return "";
    }
}
