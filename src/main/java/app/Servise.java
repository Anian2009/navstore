package app;

import app.entities.Item;
import app.model.Model;

import javax.servlet.ServletConfig;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Servise {
    public static List<String> validate(String good){
        List<String> articlList = Model.getInstance().getList().stream().map(Item::getPassword).collect(Collectors.toList());
        List<String> desiredProducts = Arrays.asList(good.split(","));
        return desiredProducts.stream().filter(s -> !articlList.contains(s)).collect(Collectors.toList());
    }

    public static List<String> orderList(List<String> articl){
        return Model.getInstance().getList().stream()
                .filter(item -> articl.contains(item.getPassword()))
                .map(item -> item.getName()+","+item.getPassword()+","+item.getPrice())
                .collect(Collectors.toList());
    }

    public static void initColllection(ServletConfig config){
        List<String> itemsList = new ArrayList<>();
        String file = config.getServletContext().getRealPath(File.separator+"data"+File.separator+"items.csv");

        try {
            itemsList = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                    .lines().collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            System.out.println("Missing file with product list. Check for file \""+file+"\";");
        }
        if (itemsList.isEmpty()) {
            System.out.println("No data in \""+config.getServletContext().getRealPath(file)+"\";");
        } else {
            for (String str:itemsList) {
                String[] array = str.split(",");
                Item item = new Item(array[0],array[1],array[2]);
                Model.getInstance().addItem(item);
            }
            Model.refreshInstance();
        }
    }
}
