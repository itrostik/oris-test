module ru.kpfu.itis.sergeev.oristest {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens ru.kpfu.itis.sergeev.oristest to javafx.fxml;
    exports ru.kpfu.itis.sergeev.oristest;
}