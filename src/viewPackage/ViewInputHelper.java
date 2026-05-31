package viewPackage;

import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ViewInputHelper {

    private ViewInputHelper() {
    }

    public static String getRequiredText(TextField textField, String message) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    public static String getOptionalText(TextField textField) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    public static Double getRequiredDouble(TextField textField, String message) {
        String value = getRequiredText(textField, message);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Integer getRequiredInteger(TextField textField, String message) {
        String value = getRequiredText(textField, message);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Integer getOptionalInteger(TextField textField, String message) {
        String value = getOptionalText(textField);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    public static LocalDate getRequiredDate(TextField textField, String requiredMessage, String formatMessage) {
        String value = getRequiredText(textField, requiredMessage);

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(formatMessage);
        }
    }

    public static LocalTime getRequiredTime(TextField textField, String requiredMessage, String formatMessage) {
        String value = getRequiredText(textField, requiredMessage);

        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(formatMessage);
        }
    }

    public static String getNullableText(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }
}
