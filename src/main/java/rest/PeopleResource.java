/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Hupra Laptop
 */
@Path("people")
public class PeopleResource
{
    
    
    
    public String getPeopleData(int amount) throws MalformedURLException, IOException
    {
        URL url = new URL("https://randomuser.me/api/?gender=female&nat=dk&results=" + amount);
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

    @Context
    private UriInfo context;


    public PeopleResource()
    {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() throws IOException
    {
        return getPeopleData(420);
    }
}
