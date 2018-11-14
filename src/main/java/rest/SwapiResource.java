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
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
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

    public String getSwapiData2(String strurl)
    {
        String jsonStr = "";
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

        List<String> list = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            if (i == 16) amount++;
            else list.add("https://swapi.co/api/people/" + (i + 1));

        }
        
        
        list = list.stream()
                .parallel()
                .map(url -> getSwapiData2(url))
                .collect(Collectors.toList());

        return list.toString();
    }

}
