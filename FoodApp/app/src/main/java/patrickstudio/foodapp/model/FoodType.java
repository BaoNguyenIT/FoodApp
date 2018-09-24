package patrickstudio.foodapp.model;

/**
 * Created by PATRICKLAGGER on 9/25/2017.
 */

public class FoodType {
    private String name;
    private String address;
    private String total;
    private int logoImg;

    public FoodType(String name, String address, String total, int logoImg) {
        this.name = name;
        this.address = address;
        this.total = total;
        this.logoImg = logoImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(int logoImg) {
        this.logoImg = logoImg;
    }
}
