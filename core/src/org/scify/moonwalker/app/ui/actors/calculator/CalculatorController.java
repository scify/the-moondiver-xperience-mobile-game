package org.scify.moonwalker.app.ui.actors.calculator;

public class CalculatorController {

    protected String operator1;
    protected String operator2;
    protected String operation;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void addOperator(String operator) {
        if(!operator.equals("dot")) {
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
            case "add":
                return "+";
            case "subtract":
                return "-";
            case "multiply":
                return "*";
            case "divide":
                return "/";
        }
        return "";
    }
}
