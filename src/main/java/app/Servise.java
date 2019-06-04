package app;

import app.entities.Item;
import app.model.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Servise {
    public static String dataDirectoryPath;
    private static final String ITEMS_FILE = "items.csv";


    public static List<String> validate(String good) {
        List<String> articlList = Model.getInstance().getList().stream().map(Item::getPassword).collect(Collectors.toList());
        List<String> desiredProducts = Arrays.asList(good.split(","));
        return desiredProducts.stream().filter(s -> !articlList.contains(s)).collect(Collectors.toList());
    }

    public static List<String> orderList(List<String> articl) {
        return Model.getInstance().getList().stream()
                .filter(item -> articl.contains(item.getPassword()))
                .map(item -> item.getName() + ", " + item.getPassword() + ", " + item.getPrice() + ";")
                .collect(Collectors.toList());
    }

    public static void initColllection(String directory) {
        dataDirectoryPath = directory;
        List<String> itemsList = new ArrayList<>();

        if (!Files.exists(Paths.get(dataDirectoryPath)))
            new File(dataDirectoryPath).mkdir();
        if (!Files.exists(Paths.get(dataDirectoryPath + File.separator + ITEMS_FILE))){
            try {
                new File(dataDirectoryPath + File.separator + ITEMS_FILE).createNewFile();
            } catch (IOException e) {
                System.err.println("Can not crate file " + dataDirectoryPath + File.separator + ITEMS_FILE+". " +
                        "Please create it yourself");
            }
        }

        try {
            itemsList = new BufferedReader(new InputStreamReader(
                    new FileInputStream(dataDirectoryPath + File.separator + ITEMS_FILE)))
                    .lines().collect(Collectors.toList());

            if (itemsList.isEmpty()) {
                System.err.println("No data in \"" + directory + File.separator + "items.csv" + "\". " +
                        "The file is initialized by default");
                itemsList = setDefaultValueInItemsFile(dataDirectoryPath + File.separator + ITEMS_FILE);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Missing file with product list. Check for file \""
                    + dataDirectoryPath + File.separator + ITEMS_FILE + "\". " +
                    "Or change the data directory as described in README file");
        }
        for (String str : itemsList) {
            try {
                String[] array = str.split(",");
                Item item = new Item(array[0], array[1], array[2]);
                Model.getInstance().addItem(item);
            }catch (IndexOutOfBoundsException ex){
                System.err.println("Invalid data in the \"items.csv\" file. Example of the correct data see In the README file.");
            }

        }
        Model.refreshInstance();
    }

    private static List<String> setDefaultValueInItemsFile(String file) throws FileNotFoundException {
        try (FileWriter fileWriter = new FileWriter(file)) {
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
            System.out.println("Initializing " + file + " - failed");
        }
        return new BufferedReader(new InputStreamReader(new FileInputStream(file)))
                .lines().collect(Collectors.toList());
    }
}
