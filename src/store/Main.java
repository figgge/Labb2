package store;


import store.products.Product;

import java.math.BigDecimal;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Product.loadStartProductDataFile();
        Menu.displayMainMenu(scanner);


    }

    public static BigDecimal stringToBigDecimal(Scanner scanner) {
        double stringToDouble;
        while (true) {
            try {
                stringToDouble = Double.parseDouble(scanner.nextLine());
                if (stringToDouble < 0)
                    throw new IllegalArgumentException();
                return BigDecimal.valueOf(stringToDouble);
            } catch (NumberFormatException exception) {
                System.out.println("Endast siffror tillåtna.");
            } catch (IllegalArgumentException e) {
                System.out.println("Felaktigt värde.");
            }
        }
    }

    public static BigDecimal stringToBigDecimal(String number) {
        double stringToDouble;
        try {
            stringToDouble = Double.parseDouble(number);
            if (stringToDouble < 0)
                throw new IllegalArgumentException();
            return BigDecimal.valueOf(stringToDouble);
        } catch (IllegalArgumentException exception) {
            return BigDecimal.valueOf(0);
        }
    }

    public static int stringToInt(String number) {
        return stringToBigDecimal(number).intValue();
    }

    public static int stringToInt(Scanner scanner) {
        return stringToBigDecimal(scanner).intValue();
    }

    public static double stringToDouble(Scanner scanner) {
        return stringToBigDecimal(scanner).doubleValue();
    }


    public static void filterPriceRange(Scanner scanner) {
        System.out.print("Ange lägsta pris: ");
        double min = stringToDouble(scanner);
        System.out.print("Ange högsta pris: ");
        double max = stringToDouble(scanner);
        Product.getAllProducts().stream()
                .filter(p -> p.getRetailPrice().doubleValue() >= min && p.getRetailPrice().doubleValue() <= max)
                .forEach(System.out::println);
    }

    public static void filterCategory(Scanner scanner) {
        Menu.displayCategories();
        Product.Category category = Menu.chooseCategory(scanner);

        Product.getAllProducts().stream()
                .filter(p -> p.getCategory().equals(category))
                .forEach(System.out::println);
    }

    public static void filterInStock() {

        Product.getAllProducts().stream()
                .filter(p -> p.getQuantity() > 0)
                .forEach(System.out::println);
    }


}