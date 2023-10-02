package ru.mail.kievsan;

public record MaxText(String maxText,
                      int maxTextNumber,
                      int maxChars,
                      char baseChar) {
    @Override
    public String toString() {
        return "Max количество " + this.maxChars +
                " букв '" + this.baseChar +
                "' в тексте под номером " + maxTextNumber;
    }

    public void print() {
        System.out.println(this);
    }
}
