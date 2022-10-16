package store.products;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class File {
    public static final Pattern PATTERN = Pattern.compile(",");
    private static final String HOME_FOLDER = System.getProperty("user.home");
    public static final Path PRODUCT_DATA_PATH = Path.of(HOME_FOLDER, "productData.csv");

    public static void createProductDataFile() {
        try {
            Files.createFile(PRODUCT_DATA_PATH);
        } catch (IOException e) {
            System.out.println(e.getClass());
        }
    }

    private static String productToString(Product product) {
        StringBuilder stringBuilder = new StringBuilder();
        String pattern = File.PATTERN.toString();
        stringBuilder.append(product.getArticleNumber()).append(pattern)
                .append(product.getName()).append(pattern)
                .append(product.getBrand()).append(pattern)
                .append(product.getQuantity()).append(pattern)
                .append(product.getRetailPrice()).append(pattern)
                .append(product.getCategory().toString())
                .append("\n");
        return stringBuilder.toString();
    }

    public static void saveProductFile() {
        deleteProductDataFile();
        createProductDataFile();
        List<String> productStrings = new ArrayList<>();
        Product.getAllProducts().forEach(product -> productStrings.add(productToString(Product.getProduct(product.getArticleNumber(), Product.getAllProducts()))));
        productStrings.forEach(productString -> {
            try {
                Files.writeString(PRODUCT_DATA_PATH, productString, StandardOpenOption.APPEND);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }

    private static void deleteProductDataFile() {
        try {
            Files.deleteIfExists(PRODUCT_DATA_PATH);
        } catch (IOException e) {
            System.out.println("Kunde inte spara filen.. allt ditt jobb var förgäves.");
        }
    }


}
