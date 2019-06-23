package service;

import static utility.Parameters.*;

public class ControlService {
    private MovieService movieService;

    public ControlService(String[] params, MovieService movieService) {
        this.movieService = movieService;
        this.parseParams(params);
    }

    private void parseParams(String[] params) {
        // we just want to check the first parameter. Every other parameter can be ignored.
        // Tell the user that the params were ignored
        if (params.length > 1) {
            System.out.println("You passed too many arguments. The following arguments were ignored:");
            for (int i = 0; i < params.length; i++) {
                if (i != 0) {
                    System.out.println(i + ". -> " + params[i]);
                }
            }
            // check params
            this.checkParameter(params[0]);
        } else if (params.length == 1) {
            // check if the parameter is valid
            this.checkParameter(params[0]);
        } else {
            // no arguments, exit
            System.out.println("You did not pass any agruments. Program will exit now!");
            System.exit(1);
        }
    }

    private void checkParameter(String param) {
        // check the integrity of the parameter
        // it should look like this "--command=data"
        // data can either be a String or a integer
        if (param.contains("--")) {
            String noDashes = param.replace("--","");
            String[] splitAtEquals = noDashes.split("=");
            // this should be an array with 2 elemnts now. The first element should contain the command
            // and the second should contain the data passed to the program
            // Now check if we understand the command
            switch (splitAtEquals[0]) {
                case MOVIESEARCH: // expects a string as data
                    this.movieService.getMovieByTitle(this.checkForStringData(splitAtEquals[1], param));
                    break;
                case ACTORSEARCH: // expects a string as data
                    String data = this.checkForStringData(splitAtEquals[1], param);

                    // TODO : Pass data to the function
                    break;
                case ACTORNETWORK: // expects a int as data
                    this.checkForIntData(splitAtEquals[1], param);
                    break;
                case MOVIENETWORK: // expects a int as data

                    break;
                default: damagedParameter(param);
            }
        } else {
            this.damagedParameter(param);
        }
    }

    private int checkForIntData(String data, String param) {
        if (data != null) {
            return Integer.parseInt(data);
        } else {
            this.damagedParameter(param);
            return 0; // will never be called
        }
    }

    private String checkForStringData(String data, String param) {
        if (data != null) {
            return data.replace("\"", "");
        } else {
            this.damagedParameter(param);
            return ""; // will never be called
        }
    }

    private void damagedParameter(String param) {
        System.out.println("The passed parameter: " + param + " could not be read by the program.");
        System.out.println("Exiting now ...");
        System.exit(1);
    }
}
