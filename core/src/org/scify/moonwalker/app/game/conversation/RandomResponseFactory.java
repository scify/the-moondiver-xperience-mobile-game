package org.scify.moonwalker.app.game.conversation;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import org.scify.moonwalker.app.helpers.ResourceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomResponseFactory {

    protected static RandomResponseFactory factory = null;
    private Json json;
    private ResourceLocator resourceLocator;
    private String jsonFilePathCorrectResponses = "json_DB/correct_responses.json";
    private String jsonFilePathWrongResponses = "json_DB/wrong_responses.json";
    private String jsonFilePathBoringResponses = "json_DB/boring_responses.json";
    private List<RandomResponse> correctResponses;
    private List<RandomResponse> wrongResponses;
    private List<RandomResponse> boringResponses;
    private List<RandomResponse> visitedCorrectResponses;
    private List<RandomResponse> visitedWrongResponses;
    private List<RandomResponse> visitedBoringResponses;

    public static RandomResponseFactory getInstance() {
        if(factory == null)
            factory = new RandomResponseFactory();
        return factory;
    }

    private RandomResponseFactory() {
        resourceLocator = new ResourceLocator();
        json = new Json();
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(false);
        correctResponses = new ArrayList<>();
        wrongResponses = new ArrayList<>();
        boringResponses = new ArrayList<>();

        visitedCorrectResponses = new ArrayList<>();
        visitedWrongResponses = new ArrayList<>();
        visitedBoringResponses = new ArrayList<>();

        correctResponses = json.fromJson(ArrayList.class, RandomResponse.class, Gdx.files.internal(resourceLocator.getFilePath(jsonFilePathCorrectResponses)));
        wrongResponses = json.fromJson(ArrayList.class, RandomResponse.class, Gdx.files.internal(resourceLocator.getFilePath(jsonFilePathWrongResponses)));
        boringResponses = json.fromJson(ArrayList.class, RandomResponse.class, Gdx.files.internal(resourceLocator.getFilePath(jsonFilePathBoringResponses)));
    }

    public String getRandomResponseFor(String randomResponseCode) throws Exception {
        switch (randomResponseCode) {
            case "random_correct":
                return getRandomCorrectResponse();
            case "random_wrong":
                return getRandomWrongResponse();
            case "random_boring":
                return getRandomBoringResponse();
            default:
                throw new Exception("Unsupported response code: " + randomResponseCode);
        }
    }

    protected String getRandomCorrectResponse() {
        String text= processListAndGetText(correctResponses, visitedCorrectResponses);
        if(visitedCorrectResponses.size() == correctResponses.size()) {
            resetAllResponsesFor(correctResponses);
            visitedCorrectResponses = new ArrayList<>();
        }
        return text;
    }

    protected String getRandomWrongResponse() {
        String text= processListAndGetText(wrongResponses, visitedWrongResponses);
        if(visitedWrongResponses.size() == wrongResponses.size()) {
            resetAllResponsesFor(wrongResponses);
            visitedWrongResponses = new ArrayList<>();
        }
        return text;
    }

    protected String getRandomBoringResponse() {
        String text= processListAndGetText(boringResponses, visitedBoringResponses);
        if(visitedBoringResponses.size() == boringResponses.size()) {
            resetAllResponsesFor(boringResponses);
            visitedBoringResponses = new ArrayList<>();
        }
        return text;
    }

    protected String processListAndGetText(List<RandomResponse> list, List<RandomResponse> visitedList) {
        RandomResponse response = getRandomResponseFrom(list);
        visitedList.add(response);
        return response.getText();
    }

    protected RandomResponse getRandomResponseFrom(List list) {
        Random randomGenerator = new Random();
        RandomResponse item = null;
        boolean found = false;
        while (!found) {
            int index = randomGenerator.nextInt(list.size());
            item = (RandomResponse) list.get(index);
            if(!item.isVisited()) {
                item.setVisited(true);
                found = true;
            }
        }
        return item;
    }

    private void resetAllResponsesFor(List<RandomResponse> list) {
        for(RandomResponse randomResponse: list) {
            randomResponse.setVisited(false);
        }
    }
}
