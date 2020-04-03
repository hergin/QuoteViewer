package domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "quotes")
public class Quote {

    @DatabaseField(id = true)
    int id;

    @DatabaseField()
    String quote;

    @DatabaseField()
    String from;

    public Quote() {
    }

    public Quote(int id, String quote, String from) {
        this.quote = quote;
        this.from = from;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quote quote1 = (Quote) o;
        return id == quote1.id &&
                Objects.equals(quote, quote1.quote) &&
                Objects.equals(from, quote1.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quote, from);
    }

    @Override
    public String toString() {
        return "domain.Quote{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                ", from='" + from + '\'' +
                '}';
    }
}
