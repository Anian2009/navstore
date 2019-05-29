package app.model;

import app.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static Model instance = new Model();
    private static List<Item> model = new ArrayList<>();
    private static List<Item> newmodel = new ArrayList<>();

    public static Model getInstance(){
        return instance;
    }

    public void  addItem(Item item){
        newmodel.add(item);
    }

    public List<Item> getList(){
        return model;
    }

    public static void refreshInstance(){
        model = newmodel;
        newmodel = new ArrayList<>();
    }
}
