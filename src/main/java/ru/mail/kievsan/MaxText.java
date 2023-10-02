package ru.mail.kievsan;

public record MaxText(String maxText,
                      int maxTextNumber,
                      int maxChars,
                      char baseChar) {
    @Override
    public String toString() {
        return "Max количество " + maxChars +
                " букв '" + baseChar +
                "' в тексте под номером " + maxTextNumber;
    }
}
