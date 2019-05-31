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
        List<String> itemsList;
        try {
            String file = config.getServletContext().getRealPath(File.separator+"data"+File.separator+"items.csv");

            itemsList = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                    .lines().collect(Collectors.toList());

            if (itemsList.isEmpty()) {
                System.out.println("[EXCEPTIONAL SITUATIONS] - No data in \""+file+"\";");

                itemsList = Collections.singletonList("Anorak,A23,50.5");
            }
            for (String str:itemsList) {
                String[] array = str.split(",");
                Item item = new Item(array[0],array[1],array[2]);
                Model.getInstance().addItem(item);
            }
                Model.refreshInstance();
        } catch (FileNotFoundException e) {
            System.out.println("[EXCEPTIONS] - Missing file with product list. Check for file \"/data/items.csv\";");
        }
    }
}
