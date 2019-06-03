package app;

import app.entities.Item;
import app.model.Model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
                .map(item -> item.getName()+", "+item.getPassword()+", "+item.getPrice()+";")
                .collect(Collectors.toList());
    }

    public static void initColllection(ServletConfig config){
        List<String> itemsList = new ArrayList<>();
        String file = config.getServletContext().getRealPath("")+".."+File.separator+"data"+File.separator+"items.csv";

        if (!Files.exists(Paths.get(file))){
            new File(config.getServletContext().getRealPath("")+".."+File.separator+"data").mkdir();
            try {
                new File(file).createNewFile();
            } catch (IOException e) {
                System.err.println("Can not crate file \"data"+File.separator+"items.csv\"");
            }
        }

        try {
            itemsList = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                    .lines().collect(Collectors.toList());

        if (itemsList.isEmpty()) {
            System.err.println("No data in \""+file+"\";");
            itemsList = setDefaultValueInItemsFile(file);
        }
        } catch (FileNotFoundException e) {
            System.err.println("Missing file with product list. Check for file \""+file+"\";");
        }
        for (String str:itemsList) {
            String[] array = str.split(",");
            Item item = new Item(array[0],array[1],array[2]);
            Model.getInstance().addItem(item);
        }
        Model.refreshInstance();
    }

    private static List<String> setDefaultValueInItemsFile(String file) throws FileNotFoundException {
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(
                    "Anorak,A23,50.5\n" +
                    "Apron,A85,5.3\n" +
                    "Baseball cap,B12,5.0\n" +
                    "Belt,B15,10.2\n" +
                    "Blouse,B45,12.9\n" +
                    "Boots,B49,45.3\n" +
                    "Cardigan,C78,20.4\n" +
                    "Coat,C98,16.7\n" +
                    "Dress,D85,65.2");
        } catch (IOException e) {
            System.out.println("Initializing "+file+" - failed");
        }
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                .lines().collect(Collectors.toList());
    }
}
