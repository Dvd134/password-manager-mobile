package ro.ase.ism.passwordmanager.services;

import android.content.Context;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.ase.ism.passwordmanager.entities.Properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PropertiesService {

    public static Properties getProperties(Context context) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read JSON from file into a Java object
            //InputStream inputStream = Properties.class.getClassLoader().getResourceAsStream("properties.json");

            InputStream inputStream = context.getAssets().open("properties.json");
            return objectMapper.readValue(inputStream, Properties.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
