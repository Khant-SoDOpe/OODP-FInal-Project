import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Category Enum - Defines all valid clothing categories
 */
enum Category {
    UPPER("Upper", 1),
    TOP_WEAR("TopWear", 2),
    LOWER("Lower", 3),
    FULL_BODY("FullBody", 4),
    UNDERWEAR("Underwear", 5),
    FOOTWEAR("Footwear", 6);

    private final String displayName;
    private final int menuNumber;

    Category(String displayName, int menuNumber) {
        this.displayName = displayName;
        this.menuNumber = menuNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMenuNumber() {
        return menuNumber;
    }

    public static Category fromMenuNumber(int number) {
        for (Category cat : Category.values()) {
            if (cat.menuNumber == number) return cat;
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

/**
 * ClothingItem - Represents a single clothing item
 */
class ClothingItem {
    private String name;
    private Category category;
    private String color;
    private String size;
    private LocalDate purchaseDate;
    private LocalDate lastWornDate;
    private double purchasePrice;
    private int timesWorn;
    private int wearThresholdForLaundry;

    public ClothingItem(String name, Category category, String color, String size,
                        LocalDate purchaseDate, LocalDate lastWornDate,
                        double purchasePrice, int wearThresholdForLaundry) {
        this.name = name;
        this.category = category;
        this.color = color;
        this.size = size;
        this.purchaseDate = purchaseDate;
        this.lastWornDate = lastWornDate;
        this.purchasePrice = purchasePrice;
        this.timesWorn = 0;
        this.wearThresholdForLaundry = wearThresholdForLaundry;
    }

    // Getters
    public String getName() { return name; }
    public Category getCategory() { return category; }
    public String getColor() { return color; }
    public String getSize() { return size; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public LocalDate getLastWornDate() { return lastWornDate; }
    public double getPurchasePrice() { return purchasePrice; }
    public int getTimesWorn() { return timesWorn; }
    public int getWearThresholdForLaundry() { return wearThresholdForLaundry; }

    // Setters
    public void setColor(String color) { this.color = color; }
    public void setSize(String size) { this.size = size; }
    public void setLastWornDate(LocalDate lastWornDate) { this.lastWornDate = lastWornDate; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
    public void setWearThresholdForLaundry(int wearThresholdForLaundry) {
        this.wearThresholdForLaundry = wearThresholdForLaundry;
    }

    public void incrementTimesWorn() { this.timesWorn++; }

    @Override
    public String toString() {
        return String.format("%-20s %-15s %-20s %-8s %-15s",
                name, category, color, size,
                lastWornDate == null ? "Never" : lastWornDate);
    }
}

/**
 * Outfit - Represents a collection of clothing items (max 1 per category)
 */
class Outfit {
    private String name;
    private List<ClothingItem> items;

    public Outfit(String name) {
        this.name = name;
        this.items = new ArrayList<>();
    }

    public boolean addItem(ClothingItem item) {
        // Check if outfit already has an item from this category
        for (ClothingItem existing : items) {
            if (existing.getCategory() == item.getCategory()) {
                return false;
            }
        }
        items.add(item);
        return true;
    }

    public boolean hasCategory(Category category) {
        for (ClothingItem item : items) {
            if (item.getCategory() == category) {
                return true;
            }
        }
        return false;
    }

    public void display() {
        System.out.println("\n✨ Outfit: " + name);
        for (ClothingItem item : items) {
            System.out.println("  - " + item.getName() + " (" + item.getCategory() + ")");
        }
    }

    public String getName() { return name; }
    public List<ClothingItem> getItems() { return items; }
}

/**
 * Wardrobe - Manages all clothing items
 */
class Wardrobe {
    private List<ClothingItem> items;

    public Wardrobe() {
        this.items = new ArrayList<>();
    }

    public void addItem(ClothingItem item) {
        items.add(item);
    }

    public void displayAllItems() {
        if (items.isEmpty()) {
            System.out.println("\n❌ No items found.");
            return;
        }
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-20s %-15s %-20s %-8s %-15s\n",
                "Name", "Category", "Color", "Size", "Last Worn");
        System.out.println("=".repeat(90));
        for (ClothingItem item : items) {
            System.out.println(item);
        }
    }

    public ClothingItem searchByName(String name) {
        for (ClothingItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public void displayByCategory(Category category) {
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-20s %-15s %-20s %-8s %-15s\n",
                "Name", "Category", "Color", "Size", "Last Worn");
        System.out.println("=".repeat(90));
        boolean found = false;
        for (ClothingItem item : items) {
            if (item.getCategory() == category) {
                System.out.println(item);
                found = true;
            }
        }
        if (!found) {
            System.out.println("❌ No items in this category.");
        }
    }

    public void displayItemsForSelection(String title) {
        if (items.isEmpty()) {
            System.out.println("\n❌ No items available.");
            return;
        }
        System.out.println("\n" + title);
        System.out.println("-".repeat(70));
        System.out.printf("%-4s %-20s %-15s %-20s\n",
                "No.", "Name", "Category", "Color");
        System.out.println("-".repeat(70));
        for (int i = 0; i < items.size(); i++) {
            ClothingItem item = items.get(i);
            System.out.printf("%-4d %-20s %-15s %-20s\n",
                    (i + 1), item.getName(), item.getCategory(), item.getColor());
        }
    }

    public void displayLaundryList() {
        List<ClothingItem> needsLaundry = new ArrayList<>();
        for (ClothingItem item : items) {
            if (item.getTimesWorn() >= item.getWearThresholdForLaundry()) {
                needsLaundry.add(item);
            }
        }

        if (needsLaundry.isEmpty()) {
            System.out.println("\n✅ No items need laundry.");
            return;
        }
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-20s %-15s %-20s %-8s %-12s %-12s\n",
                "Name", "Category", "Color", "Size", "Times Worn", "Threshold");
        System.out.println("=".repeat(90));
        for (ClothingItem item : needsLaundry) {
            System.out.printf("%-20s %-15s %-20s %-8s %-12d %-12d\n",
                    item.getName(), item.getCategory(), item.getColor(), item.getSize(),
                    item.getTimesWorn(), item.getWearThresholdForLaundry());
        }
    }

    public Outfit generateRandomOutfit() {
        if (items.isEmpty()) return null;

        Outfit outfit = new Outfit("Random Outfit");
        Random random = new Random();
        List<Category> categories = new ArrayList<>();
        Collections.addAll(categories, Category.values());
        Collections.shuffle(categories, random);

        for (Category cat : categories) {
            List<ClothingItem> categoryItems = new ArrayList<>();
            for (ClothingItem item : items) {
                if (item.getCategory() == cat) {
                    categoryItems.add(item);
                }
            }
            if (!categoryItems.isEmpty()) {
                ClothingItem randomItem = categoryItems.get(random.nextInt(categoryItems.size()));
                outfit.addItem(randomItem);
            }
        }

        return outfit.getItems().isEmpty() ? null : outfit;
    }

    public List<ClothingItem> getItems() { return items; }
}
