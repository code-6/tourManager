package com.stas.tourManager.backend.persistance.pojos;

import com.stas.tourManager.util.Id;

import java.util.Objects;

public abstract class AbstractEntity {
    protected long id;

    public AbstractEntity() {
        id = Id.getNext();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractEntity)) return false;
        AbstractEntity that = (AbstractEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                '}';
    }
}
