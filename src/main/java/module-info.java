module org.example.plants_vs_zombies1 {
    // وابستگی‌های مورد نیاز
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires jbcrypt;

    // صادر کردن پکیج‌ها
    exports org.example.plants_vs_zombies1;
    exports org.example.plants_vs_zombies1.controller to javafx.fxml;

    // باز کردن برای FXML
    opens org.example.plants_vs_zombies1 to javafx.fxml;
    opens org.example.plants_vs_zombies1.controller to javafx.fxml;

    // ❗ باز کردن پکیج مدل برای JavaFX TableView و PropertyValueFactory
    opens org.example.plants_vs_zombies1.model to javafx.base;
}
