/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Hupra Laptop
 */
@Path("swapi")
public class SwapiResource
{

    public String getSwapiData(int id) throws MalformedURLException, IOException
    {
        URL url = new URL("https://swapi.co/api/people/" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "server");
        Scanner scan = new Scanner(con.getInputStream());
        String jsonStr = null;
        if (scan.hasNext())
        {
            jsonStr = scan.nextLine();
        }
        scan.close();
        return jsonStr;
    }

    public String getSwapiData(String strurl)
    {
        String jsonStr = null;
        try
        {
            URL url = new URL(strurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.setRequestProperty("User-Agent", "server");
            Scanner scan = new Scanner(con.getInputStream());

            if (scan.hasNext())
            {
                jsonStr = scan.nextLine();
            }
            scan.close();

        } catch (Exception ex)
        {
            Logger.getLogger(SwapiResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonStr;
    }

    @Context
    private UriInfo context;

    public SwapiResource()
    {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("amount/{amount}")
    public String getJson(@PathParam("amount") int amount) throws IOException
    {

        Gson GSON = new Gson();

        ArrayList<String> list = new ArrayList();

        for (int i = 0; i < amount; i++)
        {
            if (i == 16)
            {
                amount++;
            } else
            {
                list.add(getSwapiData(i + 1));
            }

        }

        return list.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("async/amount/{amount}")
    public String getJsonAsync(@PathParam("amount") int amount) throws IOException, MalformedURLException
    {

        //list of urls to fetch from
        List<String> list = new ArrayList<>();

        //there is a bug on swapi where there is no person on id 17 so we skip that
        for (int i = 0; i < amount; i++)
        {
            if (i == 16) amount++;
            else  list.add("https://swapi.co/api/people/" + (i + 1)); 
        }

        return list                             //return json
                .stream()                       //makes a stream
                .parallel()                     //makes stream parallel (async)
                .map(this::getSwapiData)        //get json from the api
                .filter(Objects::nonNull)       //remove nulls from stream
                .collect(Collectors.toList())   //collect stream to list
                .toString();                    //convert list to string

    }
    
    
    @GET
    @Path("async")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonAsync() throws IOException, MalformedURLException
    {
        return getJsonAsync(86);                //there is no other way to get all from swapi than to use hardcoded 86
    }

}
