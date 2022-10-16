package store.products;


import store.Main;
import store.Menu;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

import static store.Main.*;


public class Product {
    private static int uniqueProductsCreated;
    private static final List<Product> allProducts = new ArrayList<>();
    private int articleNumber;
    private String name;
    private String brand;
    private int quantity;
    private BigDecimal retailPrice;
    private Category category;
    public static final Product emptyProduct = new Product();

    private Product() {
        this.articleNumber = 0;
        this.name = "";
        this.brand = "";
        this.quantity = 0;
        this.retailPrice = BigDecimal.valueOf(0);
        this.category = Category.UNSPECIFIED;
    }

    private Product(Product product) {
        this.articleNumber = product.articleNumber;
        this.name = product.name;
        this.brand = product.brand;
        this.quantity = 0;
        this.retailPrice = product.retailPrice;
        this.category = product.category;
    }

    private Product(String name, String brand, int quantity, double retailPrice, Category category) {
        uniqueProductsCreated += 1;
        this.articleNumber = uniqueProductsCreated;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.retailPrice = BigDecimal.valueOf(retailPrice);
        this.category = category;
        allProducts.add(this);
    }

    private Product(String articleNumber, String name, String brand, String quantity, String retailPrice, String category) {
        this.articleNumber = Main.stringToInt(articleNumber);
        this.name = name;
        this.brand = brand;
        this.quantity = Main.stringToInt(quantity);
        this.retailPrice = Main.stringToBigDecimal(retailPrice);
        this.category = Category.valueOf(category);
        allProducts.add(this);
    }

    public static Product loadProduct(String[] product) {
        return new Product(product[0], product[1], product[2], product[3], product[4], product[5]);
    }


    public static void createNewProduct(Scanner scanner) {
        System.out.print("Produktnamn: ");
        String name = scanner.nextLine();
        System.out.print("Varumärke: ");
        String brand = scanner.nextLine();
        System.out.print("Antal: ");
        int quantity = stringToInt(scanner);
        System.out.print("Pris: ");
        double retailPrice = stringToDouble(scanner);
        Menu.displayCategories();
        Category category = Menu.chooseCategory(scanner);
        new Product(name, brand, quantity, retailPrice, category);
        System.out.println("Produkten är tillagd: " + allProducts.get(allProducts.size() - 1));
    }

    public static Product createNewProduct(Product product) {
        return new Product(product);
    }


    public static List<Product> getAllProducts() {
        return allProducts;
    }

    public static void loadStartProductDataFile() {
        if (!Files.exists(File.PRODUCT_DATA_PATH))
            File.createProductDataFile();
        try (Stream<String> lines = Files.lines(File.PRODUCT_DATA_PATH)) {

            List<Product> list = lines.map(line -> {
                String[] arr = File.PATTERN.split(line);
                return loadProduct(new String[]{arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]});
            }).toList();
            uniqueProductsCreated = getUniqueProductsCreated(list);
            fixFirstArticleNumber(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void fixFirstArticleNumber(List<Product> list) {
        if (list.size() != 0) {
            Product product = findZeroArticleNumber();
            product.articleNumber = 1;
        }

    }


    public int getArticleNumber() {
        return articleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addOne() {
        this.quantity += 1;
    }

    public void removeOne() {
        this.quantity -= 1;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;

        if (articleNumber != product.articleNumber) return false;
        if (quantity != product.quantity) return false;
        if (!Objects.equals(name, product.name)) return false;
        if (!Objects.equals(brand, product.brand)) return false;
        if (!Objects.equals(retailPrice, product.retailPrice)) return false;
        return category == product.category;
    }

    @Override
    public int hashCode() {
        int result = articleNumber;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (retailPrice != null ? retailPrice.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Article Number: " + articleNumber +
                ", Name: " + name +
                ", Brand: " + brand +
                ", Quantity: " + quantity +
                ", Retail Price: " + retailPrice +
                ", Category: " + category;
    }

    public String toStringReduced() {
        return "Arikelnummer: " + articleNumber +
                ", Namn: " + name +
                ", Märke: " + brand +
                ", Antal: " + quantity +
                ", Pris: " + retailPrice;
    }

    public String toStringReducedNoQuantity() {
        return "Arikelnummer: " + articleNumber +
                ", Namn: " + name +
                ", Märke: " + brand +
                ", Pris: " + retailPrice;
    }

    public enum Category {
        ACCESSORIES("ACCESSORIES"),
        FOOTWEAR("FOOTWEAR"),
        PANTS("PANTS"),
        SWEATERS("SWEATERS"),
        UNSPECIFIED("UNSPECIFIED");

        Category(String categoryName) {
        }
    }

    public static Product findZeroArticleNumber() {
        return allProducts.stream()
                .filter(p -> p.getArticleNumber() == 0)
                .reduce((a, b) -> {
                    throw new IllegalArgumentException("Fel i databasen");
                })
                .orElse(emptyProduct);
    }

    public static Product getProduct(Scanner scanner) {
        System.out.print("Ange artikelkod: ");
        int articleNumber = stringToInt(scanner);
        return allProducts.stream()
                .filter(p -> p.getArticleNumber() == articleNumber)
                .reduce((a, b) -> {
                    throw new IllegalArgumentException("Fel i databasen. Artikel " + articleNumber + " är inte unik");
                })
                .orElse(emptyProduct);
    }

    public static Product getProduct(int articleNumber, List<Product> list) {
        return list.stream()
                .filter(p -> p.getArticleNumber() == articleNumber)
                .reduce((a, b) -> {
                    throw new IllegalArgumentException("Fel i databasen");
                })
                .orElse(emptyProduct);
    }

    public static Product getProduct(Scanner scanner, List<Product> list) {
        System.out.print("Ange artikelkod: ");
        int articleNumber = stringToInt(scanner);
        return list.stream()
                .filter(p -> p.getArticleNumber() == articleNumber)
                .reduce((a, b) -> {
                    throw new IllegalArgumentException("Fel i databasen");
                })
                .orElse(emptyProduct);
    }

    public static void editName(Product product, Scanner scanner) {
        System.out.print("Ange nytt namn: ");
        String name = scanner.nextLine();
        product.setName(name);
        System.out.println(product);
    }

    public static void editBrand(Product product, Scanner scanner) {
        System.out.print("Ange nytt varumärke: ");
        String brand = scanner.nextLine();
        product.setBrand(brand);
        System.out.println(product);
    }

    public static void editQuantity(Product product, Scanner scanner) {
        System.out.print("Ange nytt saldo: ");
        int quantity = stringToInt(scanner);
        product.setQuantity(quantity);
        System.out.println(product);
    }

    public static void editRetailPrice(Product product, Scanner scanner) {
        System.out.print("Ange nytt pris: ");
        BigDecimal retailPrice = stringToBigDecimal(scanner);
        product.setRetailPrice(retailPrice);
        System.out.println(product);
    }


    public static void editCategory(Product product, Scanner scanner) {
        Menu.displayCategories();
        Category category = Menu.chooseCategory(scanner);
        product.setCategory(category);
        System.out.println(product);
    }

    private static int getUniqueProductsCreated(List<Product> products) {
        if (products.size() == 0)
            return 0;
        return products.stream()
                .skip(products.size() - 1)
                .map(Product::getArticleNumber)
                .findFirst()
                .orElse(0);
    }

}
