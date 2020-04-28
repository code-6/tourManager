package com.stas.tourManager.backend.persistance.pojos;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Guide extends Participant {
    private Language language;

    public Guide() {
        super();
    }

    public Guide(@NotNull String firstName, @NotNull String lastName) {
        super(firstName, lastName);
    }

    public Guide(@NotNull String firstName, String middleName, @NotNull String lastName) {
        super(firstName, middleName, lastName);
    }

    public Guide withLanguage(Language language) {
        this.setLanguage(language);
        return this;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guide)) return false;
        if (!super.equals(o)) return false;
        Guide guide = (Guide) o;
        return Objects.equals(getLanguage(), guide.getLanguage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLanguage());
    }

    public String toStringFull() {
        return "Guide{" +
                "language='" + language + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public String toString() {
        return "Guide{" +
                "language='" + (language == null ? "" : language.getLang())+ '\'' +
                ", fullName='" + fullName + '\'' +
                ", id=" + id +
                '}';
    }


}
