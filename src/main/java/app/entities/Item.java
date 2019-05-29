package app.entities;

public class Item {
    private String name;
    private String password;
    private String price;

    public Item(String name, String password, String price) {
        this.name = name;
        this.password = password;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name) &&
                password.equals(item.password) &&
                price.equals(item.price);
    }

    @Override
    public int hashCode() {
        int result = name != null? name.hashCode():0;
        result = result+(password != null?password.hashCode():0);
        result = 31*result+(price != null?price.hashCode():0);
        return result;
    }

    @Override
    public String toString() {
        return "{\"name\":\""+name+"\",\"password\":\""+password+"\",\"price\":"+price+"}";
    }
}
