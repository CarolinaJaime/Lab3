package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private final List<CountryTranslation> countryTranslations = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.
            // Parse the JSON data and populate the countryTranslations list
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObj = jsonArray.getJSONObject(i);
                String country = countryObj.getString("country");
                JSONArray languagesArray = countryObj.getJSONArray("languages");

                List<String> languages = new ArrayList<>();
                List<String> translations = new ArrayList<>();

                // Loop through each language and its translation
                for (int j = 0; j < languagesArray.length(); j++) {
                    JSONObject languageObj = languagesArray.getJSONObject(j);
                    String language = languageObj.getString("language");
                    String translation = languageObj.getString("translation");

                    languages.add(language);
                    translations.add(translation);
                }

                // Create and add a new CountryTranslation object to the list
                countryTranslations.add(new CountryTranslation(country, languages, translations));
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        for (CountryTranslation ct : countryTranslations) {
            if (ct.getCountry().equals(country)) {
                return new ArrayList<>(ct.getLanguages());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        // Return the list of countries (deep copy to avoid aliasing)
        List<String> countries = new ArrayList<>();
        for (CountryTranslation ct : countryTranslations) {
            countries.add(ct.getCountry());
        }
        return countries;
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        // Find the country and return the translation for the specified language
        for (CountryTranslation ct : countryTranslations) {
            if (ct.getCountry().equals(country)) {
                List<String> languages = ct.getLanguages();
                List<String> translations = ct.getTranslations();

                int index = languages.indexOf(language);
                if (index != -1) {
                    return translations.get(index);
                }
            }
        }
        return null;
    }

    /**
     * A class representing the translation information for a specific country.
     * It holds the country code, a list of language codes, and their corresponding translations.
     */
    public static class CountryTranslation {
        private final String country;
        private final List<String> languages;
        private final List<String> translations;

        /**
         * Constructs a CountryTranslation object.
         *
         * @param country      the country code, should not be null
         * @param languages    the list of language codes, should not be null
         * @param translations the list of corresponding translations, should not be null
         * @throws NullPointerException if any argument is null
         */
        public CountryTranslation(String country, List<String> languages, List<String> translations) {
            if (country == null || languages == null || translations == null) {
                throw new NullPointerException("Country, languages, and translations must not be null.");
            }
            this.country = country;
            this.languages = languages;
            this.translations = translations;
        }

        /**
         * Gets the country code.
         *
         * @return the country code, never null
         */
        public String getCountry() {
            return country;
        }

        /**
         * Gets the list of language codes for the country.
         *
         * @return the list of language codes, never null
         */
        public List<String> getLanguages() {
            return languages;
        }

        /**
         * Gets the list of translations corresponding to the languages.
         *
         * @return the list of translations, never null
         */
        public List<String> getTranslations() {
            return translations;
        }
    }
}

