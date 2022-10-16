package store;


import java.math.BigDecimal;
import java.util.Scanner;

import store.order.Checkout;
import store.products.File;
import store.products.Product.Category;
import store.products.Product;


public class Menu {

    public static void mainMenu(Scanner scanner) {
        System.out.println();
        System.out.print("Menyval: ");
        String userChoice = scanner.nextLine();

        switch (userChoice.toLowerCase()) {
            case "1" -> Product.createNewProduct(scanner);
            case "2" -> editProductInfoMenu(scanner);
            case "3" -> displayFilterMenu(scanner);
            case "4" -> Main.filterInStock();
            case "5" -> Menu.displayCheckoutMenu(scanner);
            case "e" -> saveAndExit();
            // Testmeny:
            case "s" -> File.saveProductFile();
            case "a" -> Product.getAllProducts().forEach(System.out::println);

        }
        displayMainMenu(scanner);

    }


    private static void saveAndExit() {
        File.saveProductFile();
        System.exit(0);
    }

    public static void displayMainMenu(Scanner scanner) {
        System.out.println("""
                Huvudmeny
                =========
                1. Skapa ny artikel
                2. Ändra artikel
                3. Filtrera och sök
                4. Lista alla produkter i lager
                5. Gå till kassameny
                e. Avsluta och spara
                """);
        mainMenu(scanner);
    }

    private static void displayFilterMenu(Scanner scanner) {
        System.out.println("""
                Filtrera och sök
                ================
                1. Sök på artikelnummer
                2. Filtrera på kategori
                3. Filtrera på pris
                4. Lista alla produkter
                5. Till huvudmeny
                """);
        filterMenu(scanner);
    }

    private static void filterMenu(Scanner scanner) {
        System.out.print("Menyval: ");
        String userInput = scanner.nextLine();

        switch (userInput.toLowerCase()) {
            case "1" -> System.out.println(Product.getProduct(scanner));
            case "2" -> Main.filterCategory(scanner);
            case "3" -> Main.filterPriceRange(scanner);
            case "4" -> Product.getAllProducts().forEach(System.out::println);
        }
        displayMainMenu(scanner);
    }

    public static void displayCategories() {
        System.out.println("Kategorier: ");
        for (int i = 0; i < Product.Category.values().length - 1; i++) {
            System.out.println(i + 1 + ". " + Product.Category.values()[i]);
        }
    }

    public static Category chooseCategory(Scanner scanner) {
        System.out.print("Kategori: ");
        String userChoice = scanner.nextLine();

        switch (userChoice.toLowerCase()) {
            case "1" -> {
                return Category.ACCESSORIES;
            }
            case "2" -> {
                return Category.FOOTWEAR;
            }
            case "3" -> {
                return Category.PANTS;
            }
            case "4" -> {
                return Category.SWEATERS;
            }
            default -> {
                return Category.UNSPECIFIED;
            }

        }
    }

    public static void displayEditProductInfoMeny() {
        System.out.println("""
                Ändra artikel
                =============
                1. Namn
                2. Märke
                3. Antal
                4. Pris
                5. Kategori
                6. Tillbaka till huvudmeny
                """);

    }

    public static void editProductInfoMenu(Scanner scanner) {
        Product product = Product.getProduct(scanner);
        System.out.println(product);
        displayEditProductInfoMeny();
        System.out.print("Menyval: ");
        String userChoice = scanner.nextLine();
        System.out.println();

        switch (userChoice.toLowerCase()) {
            case "1" -> Product.editName(product, scanner);
            case "2" -> Product.editBrand(product, scanner);
            case "3" -> Product.editQuantity(product, scanner);
            case "4" -> Product.editRetailPrice(product, scanner);
            case "5" -> Product.editCategory(product, scanner);
            case "6" -> displayMainMenu(scanner);
        }
    }

    public static void displayCheckoutMenu(Scanner scanner) {
        System.out.println("""
                Kassameny
                =============
                1. Lägg till artikel i kundkorg
                2. Ta bort artikel ur kundkorg
                3. Visa kundkorg
                4. Genomför köp
                5. Tillbaka till huvudmeny
                """);
        checkoutMenu(scanner);
    }

    public static void checkoutMenu(Scanner scanner) {
        System.out.print("Menyval:");
        String userChoice = scanner.nextLine();
        System.out.println();

        switch (userChoice.toLowerCase()) {
            case "1" -> Checkout.addProductToShoppingCart(scanner);
            case "2" -> Checkout.removeProductFromShoppingCart(scanner, Checkout.getShoppingCart());
            case "3" -> Checkout.printShoppingCart();
            case "4" -> Checkout.completeTransaction(scanner, Checkout.sumOfProducts());
            case "5" -> Menu.displayMainMenu(scanner);
        }
        displayCheckoutMenu(scanner);
    }

    public static void displayDiscountMenu() {
        System.out.println("""
                Kampanj/Rabatt
                ==============
                1. Personalrabatt
                2. Black Friday
                3. 10 % rabatt
                4. Ingen rabatt
                """);
    }

    public static BigDecimal discountMenu(Scanner scanner, BigDecimal total) {
        System.out.print("Menyval: ");
        String userChoice = scanner.nextLine();
        System.out.println();

        switch (userChoice.toLowerCase()) {
            case "1" -> {
                return Checkout.applyEmployeeDiscount(total);
            }
            case "2" -> {
                return Checkout.applyBlackFridayDiscount(total);
            }
            case "3" -> {
                return Checkout.applyTenPercentDiscount(total);
            }
            default -> {
                return Checkout.applyNoDiscount(total);
            }
        }
    }

}
