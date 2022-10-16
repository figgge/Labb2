package store.order;

import store.Menu;
import store.products.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static store.products.Product.getProduct;

public class Checkout {
    private static List<Product> shoppingCart = new ArrayList<>();


    public static List<Product> getShoppingCart() {
        return shoppingCart;
    }


    public static void addProductToShoppingCart(Scanner scanner) {
        Product product = getProduct(scanner);
        int articleNumber = product.getArticleNumber();
        if (!shoppingCart.contains(getProduct(articleNumber, shoppingCart)) && product.getQuantity() > 0)
            shoppingCart.add(Product.createNewProduct(product));
        if (product.getQuantity() > 0) {
            Product productInCart = getProduct(articleNumber, shoppingCart);
            productInCart.addOne();
            product.removeOne();
            System.out.println("1 st " + product.toStringReducedNoQuantity() + " är tillagd i varukorgen\n");
        } else if (product.getQuantity() < 1)
            System.out.println("Artikeln är slutsåld.\n");
    }

    public static void removeProductFromShoppingCart(Scanner scanner, List<Product> shoppingCart) {
        Product product = getProduct(scanner, shoppingCart);
        String message = "1 st " + product.toStringReducedNoQuantity() + " är borttagen ur varukorgen.\n";
        if (product.getQuantity() > 1) {
            getProduct(product.getArticleNumber(), shoppingCart).removeOne();
            getProduct(product.getArticleNumber(), Product.getAllProducts()).addOne();
            System.out.println(message);
        } else if (product.getQuantity() > 0) {
            shoppingCart.remove(product);
            getProduct(product.getArticleNumber(), Product.getAllProducts()).addOne();
            System.out.println(message);
        } else
            System.out.println("Artikeln finns inte i varukorgen.\n");
    }

    public static BigDecimal sumOfProducts() {
        BigDecimal sum = new BigDecimal(0);
        for (Product p : shoppingCart) {
            sum = sum.add(new BigDecimal((String.valueOf(p.getRetailPrice().multiply(BigDecimal.valueOf(p.getQuantity()))))));
        }
        return sum;
    }

    public static void printReceipt(BigDecimal discount) {
        shoppingCart.forEach(product -> System.out.println(product.toStringReduced()));
        System.out.println("Totalt: " + sumOfProducts());
        System.out.println("Rabatt " + discount);
        System.out.println("Totalt inklusive rabatt: " + calculateTotalincludingDiscount(sumOfProducts(), discount) + "\n");
    }

    public static BigDecimal calculateTotalincludingDiscount(BigDecimal amount, BigDecimal discount) {
        return amount.subtract(discount);

    }

    public static void printShoppingCart() {
        shoppingCart.forEach(product -> System.out.println(product.toStringReduced()));
        System.out.println();
    }

    public static void completeTransaction(Scanner scanner, BigDecimal total) {
        if (shoppingCart.size() > 0) {
            Menu.displayDiscountMenu();
            BigDecimal discount = new BigDecimal(String.valueOf(Menu.discountMenu(scanner, total)));

            System.out.println("""
                    Tack för ditt köp!
                    Kvitto:""");
            printReceipt(discount);
            shoppingCart = new ArrayList<>();
            System.out.println();
        } else
            System.out.println("Det finns inga artiklar i varukorgen.\n");
    }

    public static BigDecimal applyBlackFridayDiscount(BigDecimal amount) {
        return Discount.blackFridayDiscount().applyDiscount(amount);
    }

    public static BigDecimal applyEmployeeDiscount(BigDecimal amount) {
        return Discount.employeeDiscount().applyDiscount(amount);
    }

    public static BigDecimal applyTenPercentDiscount(BigDecimal amount) {
        return Discount.tenPercentDiscount().applyDiscount(amount);
    }

    public static BigDecimal applyNoDiscount(BigDecimal total) {
        return total;
    }
}
