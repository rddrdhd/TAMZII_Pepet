package com.example.thegathering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyParser {


/*
    v -num -num num
     to
    {..., -numf, -numf,  numf, ...}
 */

    public static float[] ObjToVerticees(String Obj){
        float[] vertices;
       /* float[] vertices = {  // Vertices for the square
                -1.0f, -1.0f,  0.0f,  // 0. left-bottom
                1.0f, -1.0f,  0.0f,  // 1. right-bottom
                -1.0f,  1.0f,  0.0f,  // 2. left-top
                1.0f,  1.0f,  0.0f   // 3. right-top
        };*/
        BufferedReader reader;
        List<Float> vertList = new ArrayList<Float>();
        try {
            reader = new BufferedReader(new FileReader(
                    "/Users/leonazurkova/Documents/AndroidStudio/TheGathering/app/sampledata/models/opice.obj"));
            String line = reader.readLine();
            Boolean isNegative = false;
            int linePos = 0;
            int numLen = 8;
            while (line != null) {
                if(line.charAt(0) == 'v'){

                    if(line.charAt(2) == '-') isNegative=true;
                    linePos += isNegative?3:2;

                    vertList.add(Float.parseFloat(line.substring(linePos, linePos+=numLen)));



                    // string.Float.parseFloat();
                }
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float[] floatArray = new float[vertList.size()];
        int i = 0;

        for (Float f : vertList) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }

        return vertices;
    }
}
