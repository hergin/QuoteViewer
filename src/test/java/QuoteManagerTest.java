import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import domain.Quote;
import exceptions.QuoteIdExistsException;
import exceptions.QuoteIdNotExistsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class QuoteManagerTest {

    QuoteManager quoteManager;

    @BeforeEach
    void setupQuoteManagerAndAddQuotes() throws SQLException, IOException {

        // Add test data in the already tested way!
        var testConnection = new JdbcConnectionSource("jdbc:sqlite:test.db");
        TableUtils.dropTable(testConnection,Quote.class,true);
        TableUtils.createTableIfNotExists(testConnection, Quote.class);
        Dao<Quote, Integer> quoteDao = DaoManager.createDao(testConnection, Quote.class);
        for (int i = 0; i < 3; i++) {
            quoteDao.create(new Quote(i, "Quote_" + i, "From_" + i));
        }
        testConnection.close();

        quoteManager = new QuoteManager("test.db");
    }

    @Test
    void getAllQuotes() {
        var allActualQuotes = quoteManager.getAllQuotes();

        for (int i = 0; i < 3; i++) {
            var expectedQuote = new Quote(i, "Quote_" + i, "From_" + i);
            assertEquals(expectedQuote, allActualQuotes.get(i));
        }
    }

    @Test
    void addQuote_Success() {
        var newQuote = new Quote(3, "Quote_3", "From_3");
        var resultingQuote = quoteManager.addQuote(newQuote);
        assertEquals(newQuote, resultingQuote);
    }

    @Test
    void addQuote_ExistingId() {
        var newQuote = new Quote(2, "Quote_2", "From_2");
        assertThrows(QuoteIdExistsException.class, () -> {
            var resultingQuote = quoteManager.addQuote(newQuote);
        });
    }

    @Test
    void updateQuoteText_Success() {
        var expectedText = "Quote_10";
        var resultingQuote = quoteManager.updateQuoteText(2, expectedText);
        assertEquals(expectedText, resultingQuote.getQuote());
    }

    @Test
    void updateQuoteText_NotExistingId() {
        var expectedText = "Quote_10";
        assertThrows(QuoteIdNotExistsException.class, () -> {
            var resultingQuote = quoteManager.updateQuoteText(10, expectedText);
        });
    }

    @Test
    void updateQuoteFrom_Success() {
        var expectedFrom = "From_10";
        var resultingQuote = quoteManager.updateQuoteFrom(2, expectedFrom);
        assertEquals(expectedFrom, resultingQuote.getFrom());
    }

    @Test
    void updateQuoteFrom_NotExistingId() {
        var expectedFrom = "From_10";
        assertThrows(QuoteIdNotExistsException.class, () -> {
            var resultingQuote = quoteManager.updateQuoteFrom(10, expectedFrom);
        });
    }

    @Test
    void deleteQuote_Success() {
        assertEquals(3, quoteManager.getAllQuotes().size());
        var expectedQuote = new Quote(0, "Quote_0", "From_0");
        var resultingQuote = quoteManager.deleteQuote(0);
        assertEquals(expectedQuote, resultingQuote);
        assertEquals(2, quoteManager.getAllQuotes().size());
    }

    @Test
    void deleteQuote_NotExistingId() {
        assertEquals(3, quoteManager.getAllQuotes().size());
        assertThrows(QuoteIdNotExistsException.class, () -> {
            var resultingQuote = quoteManager.deleteQuote(132);
        });
    }

    @AfterEach
    void disposeDB() {
        quoteManager.disposeResources();
        File dbFile = new File(Paths.get(".").normalize().toAbsolutePath() + "\\test.db");
        dbFile.delete();
    }
}