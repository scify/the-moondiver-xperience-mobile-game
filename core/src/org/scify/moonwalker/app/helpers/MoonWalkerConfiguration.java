package org.scify.moonwalker.app.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.util.Properties;

public class MoonWalkerConfiguration {

    /**
     * Get a variable from project.properties file (given an input stream)
     * @param propertyName the name of the property
     * @return the value of the given property
     */
    public String getPropertyByName(FileHandle fileHandle, String propertyName) {
        Properties props = new Properties();
        try {
            props.load(fileHandle.read());
            return props.getProperty(String.valueOf(propertyName));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Given a property key, gets a value from resources/project.properties file
     * @param propertyKey the property key
     * @return the property value
     */
    public String getProjectProperty(String propertyKey) {
        return this.getDataPackProperty(propertyKey, "project_additional.properties");
    }

    public String getDataPackProperty(String propertyKey, String propertyFileName) {
        //When loading a resource, the "/" means root of the main/resources directory
        FileHandle fileHandle = Gdx.files.internal(propertyFileName);
        //if project_additional.properties file is not found, we load the default one
        if(!fileHandle.exists()) {
            fileHandle = Gdx.files.internal("project.properties");
        }
        String propertyValue = this.getPropertyByName(fileHandle, propertyKey);
        if(propertyValue == null) {
            fileHandle = Gdx.files.internal("project.properties");
            propertyValue = this.getPropertyByName(fileHandle, propertyKey);
        }
        return propertyValue;
    }
}
