package org.scify.moonwalker.app.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.net.URL;

public class ResourceLocator {
    private String rootDataPath;
    private String rootDataPathDefault;
    private MoonWalkerConfiguration configuration;
    private static ResourceLocator instance;

    public static ResourceLocator getInstance() {
        if (instance == null) {
            instance = new ResourceLocator();
        }

        return instance;
    }

    private ResourceLocator() {
        configuration = new MoonWalkerConfiguration();
        this.rootDataPath = "data_packs/" + configuration.getProjectProperty("DATA_PACKAGE") + "/";
        this.rootDataPathDefault = "data_packs/" + configuration.getProjectProperty("DATA_PACKAGE_DEFAULT") + "/";
    }


    /**
     * Given a path that represents a resource, tries to find if the resource is available in the current data pack
     * If not available, loads the corresponding file from the default pack
     *
     * @param path the path of the desired file
     * @param fileName the name of the desired file
     * @return the resource path in which the file is available (current data pack or default data pack)
     */
    public String getFilePath(String path, String fileName) {
        String filePath = path + fileName;
        //check if there is a representation of the file in the additional data pack
        String additionalPackFileName = getFileNameEquivalentFromResourcePack(path + fileName);
        if(additionalPackFileName != null)
            filePath = additionalPackFileName;

        String file = this.rootDataPath + filePath;
        URL fileURL = getClass().getResource(file);
        if (fileURL == null) {
            file = this.rootDataPathDefault + filePath;
        }
        return file;
    }

    public String getFilePath(String filePath) {
        return getFilePath("", filePath);
    }

    /**
     * Searches for the file name in the additional resource pack that corresponds to the file name of the game
     * @param file the file name
     * @return the name of the file if found, otherwise null
     */
    private String getFileNameEquivalentFromResourcePack(String file) {
        //System.out.println("Trying to get mapped property: " + file);
        MoonWalkerConfiguration configuration = new MoonWalkerConfiguration();
        //When loading a resource, the "/" means root of the main/resources directory
        FileHandle fileHandle = Gdx.files.internal(this.rootDataPath + "resources_map.properties");
        //if project_additional.properties file is not found, we load the default one
        if(!fileHandle.exists()) {
            return null;
        } else {
            return configuration.getPropertyByName(fileHandle, file);
        }
    }

    public void dispose() {
        instance = null;
    }
}
